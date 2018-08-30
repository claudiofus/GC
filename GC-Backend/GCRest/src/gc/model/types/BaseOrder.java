package gc.model.types;

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
import gc.utils.DBUtils;
import gc.utils.Utils;

public class BaseOrder extends Order {
	public BaseOrder() {
		super();
	}

	public BaseOrder(int id, String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public BaseOrder(String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price,
				iva, sqlDate);
	}

	@Override
	public String getDDT() {
		return null;
	}

	@Override
	public Rectangle getPDFRECT() {
		return null;
	}

	@Override
	public String getDBCODE() {
		return null;
	}

	@Override
	public String getDATEORDER() {
		return null;
	}

	@Override
	public String getDATEFORMAT() {
		return null;
	}

	@Override
	public LinkedMap<String, ArrayList<Order>> parseOrder(File file,
			Connection conn) {
		String descDDT = getDDT(), dateOrderRegex = getDATEORDER(),
				dateFormat = getDATEFORMAT();
		Rectangle rect = getPDFRECT();
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
		String esito = Utils.extractData(file, rect);
		java.sql.Date sqlDate = null;
		String[] righe = esito.split("\n");
		for (String riga : righe) {
			if (riga.startsWith(descDDT) && !map.containsKey(riga)) {
				sqlDate = Utils.extractOrderDate(dateOrderRegex, riga,
						dateFormat);
				map.put(riga, null);
			} else if (map.size() > 0) {
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

		String dbCode = getDBCODE();
		String lastKey = map.lastKey();
		ArrayList<Order> items = map.get(lastKey) == null
				? new ArrayList<>()
				: map.get(lastKey);
		String[] itemParts = riga.trim().replaceAll(" +", ";").split(";");

		if (itemParts.length > 5) {
			Order ord = this.getClass().getConstructor().newInstance();
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
			
			/*
			 * TODO CONTROLLARE QUANTI ELEMENTI DI UM CI SONO NELLA RIGA E
			 * PRENDERE L'ULTIMO COME UM TUTTO CIO' QUELLO PRIMA FA PARTE DELLA
			 * DESCRIZIONE
			 */

//			for (int i = 1; i < itemParts.length; i++) {
//				if (!Utils.isInEnum(itemParts[i], UM.class, useDot)) {
//					strBuild.append(itemParts[i] + " ");
//					indice = i + 1;
//				} else {
//					break;
//				}
//			}

			int val = 0;
			if (itemParts.length != indice + 5 + 1
					&& itemParts.length != indice + 4 + 1) {
				return;
			} else if (itemParts.length == indice + 5 + 1) {
				val = 1;
			}

			String productID = itemParts[0].trim();
			String productDesc = strBuild.toString().trim();
			Product prd = new Product(productID, productDesc, dbCode);
			// 0-based
			String um = itemParts[indice];
			float quantity = format.parse(itemParts[indice + 1]).floatValue();
			float price = format.parse(itemParts[indice + 2]).floatValue();
			float adj_price = format.parse(itemParts[indice + val + 3])
					.floatValue();
			float iva = format.parse(itemParts[indice + val + 4]).floatValue();
			float discount = Utils
					.round(Utils.calcDiscount(price, adj_price / quantity), 2);
			ord = this.getClass()
					.getConstructor(String.class, String.class, String.class,
							float.class, float.class, float.class, float.class,
							float.class, java.sql.Date.class)
					.newInstance(productID, productDesc, um, quantity, price,
							discount < 1 ? 0 : discount, adj_price, iva,
							sqlDate);

			Product prdToFind = DBUtils.findProduct(conn, prd);
			if (prdToFind == null) {
				DBUtils.insertProduct(conn, prd);
			}
			DBUtils.insertOrdine(conn, ord, false);
			items.add(ord);
			map.put(lastKey, items);
		}
	}
}
