package gc.fornitori;

import java.awt.Rectangle;
import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.LinkedMap;

import gc.model.Order;
import gc.model.Product;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.model.types.Scadenza;
import gc.utils.DBUtils;
import gc.utils.Utils;

public class AutofficinaLippolis extends BaseOrder {
	/**
	 * The coordinates of the Rectangles are expressed in points (ppt) without
	 * rotation nor reflection of the page.
	 */
	private static final Rectangle ID_FATT = new Rectangle(351, 240, 82, 14);
	private static final Rectangle DATA_FATT = new Rectangle(463, 240, 44, 12);
	private static final Rectangle SCADENZE_FATT = new Rectangle(35, 723, 515,
			11);
	private static final Rectangle ORDERS_AREA = new Rectangle(33, 269, 519,
			343);
	private static final Rectangle NUM_FATT_RECT = new Rectangle(316, 239, 121,
			16);
	private static final String DB_CODE = "autoffLippolis";
	private static final String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	public AutofficinaLippolis() {
		super();
	}

	public AutofficinaLippolis(int id, String productID, String productDesc,
			String um, float quantity, float price, float discount,
			float adj_price, float iva, java.sql.Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public AutofficinaLippolis(String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, java.sql.Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price,
				iva, sqlDate);
	}

	@Override
	public LinkedMap<String, ArrayList<Order>> parseOrder(File file,
			Connection conn) {
		LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
		String num_fatt = Utils.extractData(file, NUM_FATT_RECT);
		String esito = Utils.extractData(file, ORDERS_AREA);
		String[] righe = esito.split("\n");
		map.put(num_fatt, null);
		java.sql.Date sqlDate = null;
		for (String riga : righe) {
			if (map.size() > 0) {
				try {
					populateMap(sqlDate, map, conn, riga, false);
					conn.commit();
				} catch (Exception e) {
					System.err.println("Error parsing : " + riga);
					System.out.println(
							"Strategy changed, retry to parse : " + riga);
					try {
						conn.rollback();
						populateMap(sqlDate, map, conn, riga, true);
						conn.commit();
					} catch (Exception e1) {
						System.err.println("Unable to parse : " + riga);
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	public void populateMap(java.sql.Date sqlDate,
			LinkedMap<String, ArrayList<Order>> map, Connection conn,
			String riga, boolean useDot) throws Exception {
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		sqlDate = Utils.extractOrderDate(DATE_ORDER_REGEX, riga, DATE_FORMAT);
		String lastKey = map.lastKey();
		ArrayList<Order> items = map.get(lastKey) == null
				? new ArrayList<>()
				: map.get(lastKey);
		String[] itemParts = riga.trim().replaceAll(" +", ";").split(";");
		Order ord = new AutofficinaLippolis();
		if (itemParts.length > 5) {
			StringBuilder strBuild = new StringBuilder();
			int indice = 0;

			for (int idx = itemParts.length - 1; idx > 0; idx--) {
				if (Utils.isInEnum(itemParts[idx], UM.class, useDot)) {
					indice = idx;
					break;
				}
			}

			for (int j = 0; j < indice; j++) {
				strBuild.append(itemParts[j] + " ");
			}

			String productID = itemParts[0].trim();
			String productDesc = strBuild.toString().trim();
			Product prd = new Product(productID, productDesc, DB_CODE);
			String um = itemParts[indice];
			float quantity = format.parse(itemParts[indice + 1]).floatValue();
			float price = format.parse(itemParts[indice + 2]).floatValue();
			// 0-based
			if (itemParts.length == indice + 5 + 1) {
				float adj_price = format.parse(itemParts[indice + 4])
						.floatValue();
				float iva = format.parse(itemParts[indice + 5]).floatValue();
				float discount = Utils.round(
						Utils.calcDiscount(price, adj_price / quantity), 2);
				ord = new AutofficinaLippolis(productID, productDesc, um,
						quantity, price, discount < 1 ? 0 : discount, adj_price,
						iva, sqlDate);
			} else if (itemParts.length == indice + 4 + 1) {
				float adj_price = format.parse(itemParts[indice + 3])
						.floatValue();
				float iva = format.parse(itemParts[indice + 4]).floatValue();
				float discount = Utils.round(
						Utils.calcDiscount(price, adj_price / quantity), 2);
				ord = new AutofficinaLippolis(productID, productDesc, um,
						quantity, price, discount < 1 ? 0 : discount, adj_price,
						iva, sqlDate);
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
	}

	@Override
	public String getNumber(File file) {
		return Utils.extractData(file, ID_FATT);
	}

	@Override
	public java.sql.Date getDate(File file) {
		try {
			String dateStr = Utils.extractData(file, DATA_FATT);
			Date date = new SimpleDateFormat("dd/MM/yy").parse(dateStr);
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			return sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Scadenza> getDeadlines(File file) {
		String scad = Utils.extractData(file, SCADENZE_FATT);
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
		// Covering case str = lorem ipsum € 111,11
		String newStr = str;
		try {
			if (str.indexOf("€") != -1) {
				str = str.substring(str.indexOf("€"));
			}
			Pattern p = Pattern.compile(
					"^(?!€*$)(€ ?(?!.*€)(?=,?\\d))?\\-?([1-9]{1,3}( \\d{3})*|[1-9]{1,3}(\\.\\d{3})*|(0|([1-9]\\d*)?))(,[0-9]{2})?( ?€)?$");
			Matcher m = p.matcher(str);
			while (m.find()) {
				String tmp = m.group().replaceAll("€", "").trim();
				allMatches.add(numberFormat.parse(tmp).floatValue());
			}

			// Covering case str = lorem ipsum 111,11€
			if (allMatches.isEmpty() && newStr.indexOf("€") != -1) {
				newStr = newStr.substring(newStr.lastIndexOf(" "),
						newStr.indexOf("€"));
				Matcher m2 = p.matcher(newStr);
				while (m2.find()) {
					String tmp = m2.group().replaceAll("€", "").trim();
					allMatches.add(numberFormat.parse(tmp).floatValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allMatches;
	}
}