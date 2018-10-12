package gc.model.types;

import java.awt.Rectangle;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gc.db.DBOrder;
import gc.db.DBProduct;
import gc.interfaces.IInvoice;
import gc.model.Order;
import gc.model.Product;
import gc.model.UM;
import gc.utils.Utils;

public class BaseOrder extends Order implements IInvoice {
	public BaseOrder() {
		super();
	}

	public BaseOrder(int id, String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, java.sql.Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public BaseOrder(String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, java.sql.Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price,
				iva, sqlDate);
	}

	@Override
	public String getDDT() {
		return null;
	}

	@Override
	public Rectangle getORDERS_AREA() {
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
	public LinkedMap<String, ArrayList<Order>> parseOrder(PDDocument document,
			Connection conn, int page, LinkedMap<String, ArrayList<Order>> map)
			throws InvalidPasswordException, IOException {
		String descDDT = getDDT(), dateOrderRegex = getDATEORDER(),
				dateFormat = getDATEFORMAT();
		Rectangle rect = getORDERS_AREA();
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		String esito = Utils.extractDataNoSpaces(document, rect, page);
		java.sql.Date sqlDate = null;
		String[] righe = esito.split("\n");
		for (String riga : righe) {
			if (riga.startsWith(descDDT)) {
				sqlDate = Utils.extractOrderDate(dateOrderRegex, riga,
						dateFormat);
				if (!map.containsKey(riga)) {
					map.put(riga, null);
				}
			} else if (map.size() > 0) {
				/**
				 * nel caso in cui la seconda pagina faccia riferimento al ddt
				 * definito nella prima pagina sqlDate = null
				 */
				if (sqlDate == null) {
					List<Order> arr = map.getValue(map.values().size() - 1);
					Order ord = arr.get(arr.size() - 1);
					sqlDate = ord.getDate_order();
				}
				try {
					populateMap(sqlDate, map, conn, riga);
					conn.commit();
				} catch (Exception e) {
					System.err.println("Error parsing : " + riga);
					System.out.println(
							"Strategy changed, retry to parse : " + riga);
					try {
						conn.rollback();
						populateMap(sqlDate, map, conn, riga);
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
			String riga) throws Exception {
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

			if (sqlDate == null) {
				Date today = new Date();
				sqlDate = new java.sql.Date(today.getTime());
			}

			for (int i = itemParts.length - 1; i > 0; i--) {
				if (Utils.isInEnum(itemParts[i], UM.class, false)) {
					indice = i;
					break;
				}
			}

			for (int j = 0; j < indice; j++) {
				strBuild.append(itemParts[j] + " ");
			}

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

			Product prdToFind = DBProduct.findProduct(conn, prd);
			if (prdToFind == null) {
				DBProduct.insertProduct(conn, prd);
			}
			DBOrder.insertOrdine(conn, ord, false);
			items.add(ord);
			map.put(lastKey, items);
		}
	}

	@Override
	@JsonIgnore
	public Rectangle getIdFatt() {
		return null;
	}

	@Override
	public String getNumber(PDDocument file, int page) {
		return null;
	}

	@Override
	public java.sql.Date getDate(PDDocument file, int page) {
		return null;
	}

	@Override
	public List<Deadline> getDeadlines(PDDocument file, int page) {
		return null;
	}
}
