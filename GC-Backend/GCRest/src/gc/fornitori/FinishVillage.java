package gc.fornitori;

import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.pdfbox.pdmodel.PDDocument;

import gc.model.Order;
import gc.model.Product;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.model.types.Scadenza;
import gc.utils.DBUtils;
import gc.utils.Utils;

public class FinishVillage extends BaseOrder {
	private static final Rectangle ID_FATT = new Rectangle(439, 248, 135, 14);
	private static final Rectangle DATA_FATT = new Rectangle(437, 276, 139, 19);
	private static final Rectangle SCADENZE_FATT = new Rectangle(21, 733, 226,
			68);
	private static final String DDT_DESCR = "Ns. doc.";
	private static final Rectangle ORDERS_AREA = new Rectangle(21, 317, 554,
			338);
	private static final String DB_CODE = "finishVillage";
	private static final String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	public FinishVillage() {
		super();
	}

	public FinishVillage(int id, String productID, String productDesc,
			String um, float quantity, float price, float discount,
			float adj_price, float iva, java.sql.Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public FinishVillage(String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, java.sql.Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price,
				iva, sqlDate);
	}

	@Override
	public LinkedMap<String, ArrayList<Order>> parseOrder(PDDocument document,
			Connection conn, int page, LinkedMap<String, ArrayList<Order>> map) {
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		String esito = Utils.extractDataNoSpaces(document, ORDERS_AREA, page);
		String[] righe = esito.split("\n");
		java.sql.Date sqlDate = null;
		for (String riga : righe) {
			if (riga.startsWith(DDT_DESCR)) {
				sqlDate = Utils.extractOrderDate(DATE_ORDER_REGEX, riga,
						DATE_FORMAT);
				map.put(riga, null);
			} else if (map.size() > 0) {
				String lastKey = map.lastKey();
				ArrayList<Order> items = map.get(lastKey) == null
						? new ArrayList<>()
						: map.get(lastKey);
				String[] itemParts = riga.trim().replaceAll(" +", ";")
						.split(";");
				Order ord = new FinishVillage();
				try {
					if (itemParts.length > 5) {
						// Itero a partire dalla fine della stringa per cercare
						// l'ultima occorrenza di UM, salvo l'indice e tutto ciò
						// che c'è prima diventerà descrizione del prodotto
						StringBuilder strBuild = new StringBuilder();
						int indice = 0;
						for (int i = itemParts.length - 1; i > 0; i--) {
							if (Utils.isInEnum(itemParts[i], UM.class, false)) {
								indice = i;
								break;
							}
						}

						for (int j = 0; j < indice; j++) {
							strBuild.append(itemParts[j] + " ");
						}

						String productID = itemParts[0].trim();
						String productDesc = strBuild.toString().trim();
						Product prd = new Product(productID, productDesc,
								DB_CODE);
						String um = itemParts[indice];
						float quantity = format.parse(itemParts[indice + 1])
								.floatValue();
						float price = format.parse(itemParts[indice + 2])
								.floatValue();
						// 0-based
						if (itemParts.length == indice + 6 + 1) {
							float adj_price = format
									.parse(itemParts[indice + 5]).floatValue();
							float iva = format.parse(itemParts[indice + 6])
									.floatValue();
							float discount = Utils.round(Utils.calcDiscount(
									price, adj_price / quantity), 2);
							ord = new FinishVillage(productID, productDesc, um,
									quantity, price,
									discount < 1 ? 0 : discount, adj_price, iva,
									sqlDate);
						} else if (itemParts.length == indice + 5 + 1) {
							float adj_price = format
									.parse(itemParts[indice + 4]).floatValue();
							float iva = format.parse(itemParts[indice + 5])
									.floatValue();
							float discount = Utils.round(Utils.calcDiscount(
									price, adj_price / quantity), 2);
							ord = new FinishVillage(productID, productDesc, um,
									quantity, price,
									discount < 1 ? 0 : discount, adj_price, iva,
									sqlDate);
						} else if (itemParts.length == indice + 4 + 1) {
							float adj_price = format
									.parse(itemParts[indice + 3]).floatValue();
							float iva = format.parse(itemParts[indice + 4])
									.floatValue();
							float discount = Utils.round(Utils.calcDiscount(
									price, adj_price / quantity), 2);
							ord = new FinishVillage(productID, productDesc, um,
									quantity, price,
									discount < 1 ? 0 : discount, adj_price, iva,
									sqlDate);
						}
						Product prdToFind = DBUtils.findProduct(conn, prd);
						if (prdToFind == null) {
							DBUtils.insertProduct(conn, prd);
						}
						DBUtils.insertOrdine(conn, ord, false);
						conn.commit();
						items.add(ord);
						map.put(lastKey, items);
					}
				} catch (Exception e) {
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	@Override
	public String getNumber(PDDocument document, int page) {
		return Utils.extractDataNoSpaces(document, ID_FATT, page);
	}

	@Override
	public java.sql.Date getDate(PDDocument document, int page) {
		try {
			String dateStr = Utils.extractDataNoSpaces(document, DATA_FATT, page);
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			return sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Scadenza> getDeadlines(PDDocument document, int page) {
		String scad = Utils.extractDataNoSpaces(document, SCADENZE_FATT, page);
		List<Scadenza> scadList = new ArrayList<Scadenza>();
		List<String> dateList = Utils.getDateFromString(scad);
		List<Float> amount = getAmountFromString(scad);

		try {
			for (int i = 0; i < dateList.size(); i++) {
				Scadenza sc = new Scadenza();
				java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
						.parse(dateList.get(i));
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				sc.setDeadlineDate(sqlDate);
				sc.setAmount(amount.get(i));
				scadList.add(sc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scadList;
	}

	private List<Float> getAmountFromString(String str) {
		List<Float> allMatches = new ArrayList<>();
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALY);
		Arrays.stream(str.split("\\r?\\n")).forEach(line -> {
			try {
				if (line != null && !line.isEmpty()) {
					Pattern p = Pattern
							.compile("([0-9]{1,3}[.])*[0-9]{1,3},[0-9]{1,2}");
					Matcher m = p.matcher(line);
					while (m.find()) {
						allMatches.add(
								numberFormat.parse(m.group()).floatValue());
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return allMatches;
	}
}