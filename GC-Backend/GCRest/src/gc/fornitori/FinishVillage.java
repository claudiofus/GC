package gc.fornitori;

import java.awt.Rectangle;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.collections4.map.LinkedMap;

import gc.model.Order;
import gc.model.Product;
import gc.model.UM;
import gc.utils.DBUtils;
import gc.utils.Utils;

public class FinishVillage extends Order {

	public static final String DDT_DESCR = "Ns. doc.";
	public static final Rectangle PDFBOX_RECT = new Rectangle(21, 317, 554, 338);
	public static final String DB_CODE = "finishVillage";
	public static final String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public FinishVillage() {
	}

	public LinkedMap<String, ArrayList<Order>> parseOrder(File file,
			Connection conn) {
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
		String esito = extractData(file, PDFBOX_RECT);
		String[] righe = esito.split("\n");
		java.sql.Date sqlDate = null;
		for (String riga : righe) {
			if (riga.startsWith(DDT_DESCR)) {
				sqlDate = extractOrderDate(DATE_ORDER_REGEX, riga, DATE_FORMAT);
				map.put(riga, null);
			} else if (map.size() > 0) {
				String lastKey = map.lastKey();
				ArrayList<Order> items = map.get(lastKey) == null
						? new ArrayList<>()
						: map.get(lastKey);
				String[] itemParts = riga.trim().replaceAll(" +", ";")
						.split(";");
				Order ord = new Order();
				try {
					if (itemParts.length > 5) {
						StringBuilder strBuild = new StringBuilder();
						int indice = 0;
						for (int i = 1; i < itemParts.length; i++) {
							if (!Utils.isInEnum(itemParts[i], UM.class,
									false)) {
								strBuild.append(itemParts[i] + " ");
								indice = i + 1;
							} else {
								break;
							}
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
							ord = new Order(productID, productDesc, um,
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
							ord = new Order(productID, productDesc, um,
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
							ord = new Order(productID, productDesc, um,
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
				} catch (ParseException | SQLException e) {
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
}