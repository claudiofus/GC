package gc.fornitori;

import java.awt.Rectangle;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.collections4.map.LinkedMap;

import gc.model.Order;
import gc.model.Product;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.utils.DBUtils;
import gc.utils.Utils;

public class AutofficinaLippolis extends BaseOrder {
	private static final Rectangle PDFBOX_RECT = new Rectangle(33, 269, 519,
			343);
	private static final Rectangle NUM_FATT_RECT = new Rectangle(316, 239, 121,
			16);
	private static final String DB_CODE = "autoffLippolis";
	private static final String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	public AutofficinaLippolis() {
	}

	public AutofficinaLippolis(int id, String productID, String productDesc,
			String um, float quantity, float price, float discount,
			float adj_price, float iva, Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public AutofficinaLippolis(String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price,
				iva, sqlDate);
	}

	@Override
	public Rectangle getPDFRECT() {
		return PDFBOX_RECT;
	}

	@Override
	public String getDBCODE() {
		return DB_CODE;
	}

	@Override
	public String getDATEORDER() {
		return DATE_ORDER_REGEX;
	}

	@Override
	public String getDATEFORMAT() {
		return DATE_FORMAT;
	}

	@Override
	public LinkedMap<String, ArrayList<Order>> parseOrder(File file,
			Connection conn) {
		LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
		String num_fatt = Utils.extractData(file, NUM_FATT_RECT);
		String esito = Utils.extractData(file, PDFBOX_RECT);
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
}