package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Order;
import gc.model.Product;
import gc.model.types.BaseOrder;
import gc.utils.Utils;

public class DBOrder {
	private static final Logger logger = LogManager.getLogger(DBOrder.class.getName());

	private DBOrder() {
		throw new IllegalStateException("DBOrder class");
	}

	public static List<Order> findOrder(Connection conn, int buildingId) {
		String sql = "SELECT ord.id AS ord_id, inv_ddt.invoice_id AS invoice_id, inv_ddt.ddt_id AS ddt_id, "
				+ "ord.product_name, ord.building_id, ord.product_name, ord.um, ord.quantity, "
				+ "ord.price, ord.discount, ord.no_iva_price, ord.iva, ord.iva_price, ord.date_ins "
				+ "FROM gestione_cantieri.order ord "
				+ "INNER JOIN gestione_cantieri.inv_ddt_order inv_ddt ON ord.id = inv_ddt.order_id "
				+ "INNER JOIN gestione_cantieri.building bui ON ord.building_id = bui.id WHERE bui.id = ?";

		List<Order> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, buildingId);

			String query = pstm.toString();
			logger.info("findOrder: {}", query);

			list = getOrderList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method findOrder: {}", e);
		}

		return list;
	}

	public static int insertOrdine(Connection conn, Order ordine, boolean building) {
		String sql = null;

		if (building) {
			sql = "INSERT INTO gestione_cantieri.order (product_name, building_id, um, quantity, price, discount,"
					+ " no_iva_price, iva, iva_price, date_ins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "INSERT INTO gestione_cantieri.order (product_name, um, quantity, price, discount, no_iva_price,"
					+ " iva, iva_price, date_ins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		}

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setString(1, ordine.getName());

			int index = 2;
			if (building) {
				pstm.setLong(index++, ordine.getBuildingId());
			}

			pstm.setString(index++, ordine.getUm());
			pstm.setBigDecimal(index++, ordine.getQuantity());
			pstm.setBigDecimal(index++, ordine.getPrice());
			pstm.setBigDecimal(index++, ordine.getDiscount());
			pstm.setBigDecimal(index++, ordine.getNoIvaPrice());
			pstm.setBigDecimal(index++, ordine.getIva());
			pstm.setBigDecimal(index++, ordine.getIvaPrice());
			pstm.setDate(index++, ordine.getDateOrder());

			String query = pstm.toString();
			logger.info("insertOrdine: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting order failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				ordine.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertOrdine: {}", e);
		}

		return ordine.getId();
	}

	public static Order selectOrdine(Connection conn, Order ordine) {
		String sql = "SELECT invoice_id, ddt_id, ord.id AS ord_id, building_id, product_name, um, quantity, price, "
				+ "discount, no_iva_price, iva, iva_price, date_ins " + "FROM gestione_cantieri.order ord "
				+ "INNER JOIN gestione_cantieri.inv_ddt_order inv ON ord.id = inv.order_id " + "WHERE ord.id = ?";

		Order ord = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, ordine.getId());

			String query = pstm.toString();
			logger.info("selectOrdine: {}", query);

			List<Order> orderList = getOrderList(pstm);
			if (!orderList.isEmpty()) {
				ord = orderList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method selectOrdine: {}", e);
		}

		return ord;
	}

	public static void updateOrdine(Connection conn, Order ordine) {
		String sql = "UPDATE gestione_cantieri.order SET um = ?, quantity = ?, price = ?, discount = ?, no_iva_price = ?,"
				+ " iva = ?, iva_price = ?, building_id = ? WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, ordine.getUm());
			pstm.setBigDecimal(2, ordine.getQuantity());
			pstm.setBigDecimal(3, ordine.getPrice());
			pstm.setBigDecimal(4, ordine.getDiscount());
			pstm.setBigDecimal(5, ordine.getNoIvaPrice());
			pstm.setBigDecimal(6, ordine.getIva());
			pstm.setBigDecimal(7, ordine.getIvaPrice());

			if (ordine.getBuildingId() != null) {
				pstm.setInt(8, ordine.getBuildingId());
			} else {
				pstm.setNull(8, java.sql.Types.INTEGER);
			}

			pstm.setInt(9, ordine.getId());

			String query = pstm.toString();
			logger.info("updateOrdine: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating order failed, no rows affected.");
			}
		} catch (SQLException e) {
			logger.error("Error in method updateOrdine - gestione_cantieri.order: {}", e);
		}

		sql = "UPDATE gestione_cantieri.inv_ddt_order SET ddt_id = ? WHERE order_id = ?";
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {

			if (ordine.getDdtId() == null || ordine.getDdtId() == 0) {
				pstm.setNull(1, Types.INTEGER);
			} else {
				pstm.setInt(1, ordine.getDdtId());
			}
			pstm.setInt(2, ordine.getId());

			String query = pstm.toString();
			logger.info("updateOrdine: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating inv_ddt_order order failed, no rows affected.");
			}
		} catch (SQLException e) {
			logger.error("Error in method updateOrdine - gestione_cantieri.inv_ddt_order: {}", e);
		}
	}

	public static List<Product> queryProductPrice(Connection connection) {
		String sql = "SELECT product_name, provider_name, MAX(price) AS latestPrice FROM ( "
				+ "SELECT ord.product_name, prd.provider_name, iva_price/quantity AS price FROM gestione_cantieri.order ord INNER JOIN "
				+ "(SELECT product_name, MAX(date_ins) AS maxdate FROM gestione_cantieri.order GROUP BY product_name) self "
				+ "ON ord.product_name = self.product_name AND ord.date_ins = self.maxdate "
				+ "INNER JOIN gestione_cantieri.product prd ON ord.product_name = prd.name "
				+ "ORDER BY ord.product_name ASC, price DESC) t GROUP BY product_name ORDER BY provider_name;";

		List<Product> list = new ArrayList<>();
		try (PreparedStatement pstm = connection.prepareStatement(sql)) {

			String query = pstm.toString();
			logger.info("queryProductPrice: {}", query);

			list = getLatestPrice(pstm);
		} catch (SQLException e) {
			logger.error("Error in method queryProductPrice: {}", e);
		}
		return list;
	}

	public static List<HashMap<String, Object>> queryPricesHistory(Connection connection, String prdName) {
		String sql = "SELECT ddt.number, ddt.date, b.name, (iva_price/quantity) AS price FROM gestione_cantieri.order ord "
				+ "LEFT JOIN gestione_cantieri.building b ON ord.building_id = b.id "
				+ "LEFT JOIN gestione_cantieri.inv_ddt_order inv_ddt ON ord.id = inv_ddt.order_id "
				+ "LEFT JOIN gestione_cantieri.ddt ddt ON ddt.id = inv_ddt.ddt_id "
				+ "WHERE ord.product_name = ? ORDER BY ord.date_ins DESC, price DESC;";

		List<HashMap<String, Object>> list = new ArrayList<>();
		try (PreparedStatement pstm = connection.prepareStatement(sql)) {
			pstm.setString(1, prdName);

			String query = pstm.toString();
			logger.info("queryPricesHistory: {}", query);

			list = getPriceHistory(pstm);
		} catch (SQLException e) {
			logger.error("Error in method queryPricesHistory: {}", e);
		}

		return list;
	}

	private static List<Order> getOrderList(PreparedStatement pstm) {
		List<Order> list = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				BaseOrder ord = new BaseOrder();
				ord.setId(rs.getInt("ord_id"));
				ord.setInvoiceId(rs.getInt("invoice_id"));
				ord.setDdtId(rs.getInt("ddt_id"));
				ord.setName(rs.getString("product_name"));
				ord.setUm(rs.getString("um"));
				ord.setQuantity(rs.getBigDecimal("quantity"));
				ord.setPrice(rs.getBigDecimal("price"));
				ord.setDiscount(rs.getBigDecimal("discount"));
				ord.setNoIvaPrice(rs.getBigDecimal("no_iva_price"));
				ord.setIva(rs.getBigDecimal("iva"));
				ord.setIvaPrice(rs.getBigDecimal("iva_price"));
				ord.setDateOrder(rs.getDate("date_ins"));
				ord.setBuildingId(rs.getInt("building_id"));
				list.add(ord);
			}
		} catch (SQLException e) {
			logger.error("Error in method getOrderList: {}", e);
		}

		return list;
	}

	private static List<Product> getLatestPrice(PreparedStatement pstm) {
		List<Product> list = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				String name = rs.getString("product_name");
				String providerName = rs.getString("provider_name");
				float latestPrice = rs.getFloat("latestPrice");
				Product product = new Product(name, providerName);
				product.setLatestPrice(latestPrice);
				list.add(product);
			}
		} catch (SQLException e) {
			logger.error("Error in method getLatestPrice: {}", e);
		}

		return list;
	}

	private static List<HashMap<String, Object>> getPriceHistory(PreparedStatement pstm) {
		List<HashMap<String, Object>> list = new ArrayList<>();
		HashMap<String, Object> map;
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				map = new HashMap<>();
				String ddtNumb = rs.getString("ddt.number");
				Date ddtDate = rs.getDate("ddt.date");
				String building = rs.getString("b.name");
				float price = rs.getFloat("price");
				map.put("ddt_numb", ddtNumb);
				map.put("ddt_date", ddtDate);
				map.put("price", price);
				map.put("building", building);

				list.add(map);
			}
		} catch (SQLException e) {
			logger.error("Error in method queryPricesHistory: {}", e);
		}

		return list;
	}
}
