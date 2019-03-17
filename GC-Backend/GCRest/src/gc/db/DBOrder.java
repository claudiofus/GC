package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Order;
import gc.model.Product;
import gc.model.types.BaseOrder;

public class DBOrder {
	private static final Logger logger = LogManager
			.getLogger(DBOrder.class.getName());

	public static List<Order> findOrder(Connection conn, String buildingName)
			throws SQLException {
		String sql = "SELECT ord.id, ord.product_name, ord.building_id, ord.product_name, ord.um, ord.quantity, "
				+ "ord.price, ord.discount, ord.no_iva_price, ord.iva, ord.iva_price, ord.date_ins "
				+ "FROM gestione_cantieri.order ord "
				+ "INNER JOIN gestione_cantieri.building bui ON ord.building_id = bui.id WHERE bui.name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, buildingName);
		logger.info("findOrder: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Order> list = new ArrayList<Order>();
		while (rs.next()) {
			BaseOrder ord = new BaseOrder();
			ord.setId(rs.getInt("id"));
			ord.setName(rs.getString("product_name"));
			ord.setUm(rs.getString("um"));
			ord.setQuantity(rs.getBigDecimal("quantity"));
			ord.setPrice(rs.getBigDecimal("price"));
			ord.setDiscount(rs.getBigDecimal("discount"));
			ord.setNoIvaPrice(rs.getBigDecimal("no_iva_price"));
			ord.setIva(rs.getBigDecimal("iva"));
			ord.setIvaPrice(rs.getBigDecimal("iva_price"));
			ord.setDate_order(rs.getDate("date_ins"));
			ord.setBuilding_id(rs.getInt("building_id"));
			list.add(ord);
		}
		pstm.close();
		return list;
	}

	public static int insertOrdine(Connection conn, Order ordine,
			boolean building) throws SQLException {
		String sql = null;

		if (building) {
			sql = "INSERT INTO gestione_cantieri.order (product_name, building_id, um, quantity, price, discount,"
					+ " no_iva_price, iva, iva_price, date_ins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "INSERT INTO gestione_cantieri.order (product_name, um, quantity, price, discount, no_iva_price,"
					+ " iva, iva_price, date_ins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		}

		PreparedStatement pstm = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, ordine.getName());

		int index = 2;
		if (building) {
			pstm.setLong(index++, ordine.getBuilding_id());
		}

		pstm.setString(index++, ordine.getUm());
		pstm.setBigDecimal(index++, ordine.getQuantity());
		pstm.setBigDecimal(index++, ordine.getPrice());
		pstm.setBigDecimal(index++, ordine.getDiscount());
		pstm.setBigDecimal(index++, ordine.getNoIvaPrice());
		pstm.setBigDecimal(index++, ordine.getIva());
		pstm.setBigDecimal(index++, ordine.getIvaPrice());
		pstm.setDate(index++, ordine.getDate_order());

		logger.info("insertOrdine: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting order failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				ordine.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Inserting order failed, no ID obtained.");
			}
		}
		pstm.close();
		return ordine.getId();
	}

	public static Order selectOrdine(Connection conn, Order ordine)
			throws Exception {
		String sql = "SELECT id, building_id, product_name, um, quantity, price, discount, no_iva_price, iva, iva_price, date_ins "
				+ "FROM gestione_cantieri.order WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, ordine.getId());
		logger.info("selectOrdine: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Order ord = null;
		while (rs.next()) {
			ord = new BaseOrder();
			ord.setId(rs.getInt("id"));
			ord.setName(rs.getString("product_name"));
			ord.setUm(rs.getString("um"));
			ord.setQuantity(rs.getBigDecimal("quantity"));
			ord.setPrice(rs.getBigDecimal("price"));
			ord.setDiscount(rs.getBigDecimal("discount"));
			ord.setNoIvaPrice(rs.getBigDecimal("no_iva_price"));
			ord.setIva(rs.getBigDecimal("iva"));
			ord.setIvaPrice(rs.getBigDecimal("iva_price"));
			ord.setDate_order(rs.getDate("date_ins"));
			ord.setBuilding_id(rs.getInt("building_id"));
		}
		pstm.close();
		return ord;
	}

	public static void updateOrdine(Connection conn, Order ordine)
			throws SQLException {
		String sql = "UPDATE gestione_cantieri.order SET um = ?, quantity = ?, price = ?, discount = ?, no_iva_price = ?,"
				+ " iva = ?, iva_price = ?, building_id = ? WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, ordine.getUm());
		pstm.setBigDecimal(2, ordine.getQuantity());
		pstm.setBigDecimal(3, ordine.getPrice());
		pstm.setBigDecimal(4, ordine.getDiscount());
		pstm.setBigDecimal(5, ordine.getNoIvaPrice());
		pstm.setBigDecimal(6, ordine.getIva());
		pstm.setBigDecimal(7, ordine.getIvaPrice());

		if (ordine.getBuilding_id() != null) {
			pstm.setInt(8, ordine.getBuilding_id());
		} else {
			pstm.setNull(8, java.sql.Types.INTEGER);
		}
		pstm.setInt(9, ordine.getId());

		logger.info("updateOrdine: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static List<Product> queryProductPrice(Connection connection)
			throws SQLException {
		String sql = "SELECT prices.product_name, prices.provider_name, AVG(prices.price) AS medPrice "
				+ "FROM (SELECT ord.product_name, ord.quantity, prd.provider_name, (ord.iva_price/quantity) AS price "
				+ "FROM gestione_cantieri.order ord inner join gestione_cantieri.product prd on ord.product_name = prd.name) AS prices "
				+ "GROUP BY prices.product_name order by prices.provider_name, prices.product_name;";

		PreparedStatement pstm = connection.prepareStatement(sql);
		logger.info("queryProductPrice: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			String name = rs.getString("product_name");
			String providerName = rs.getString("provider_name");
			float medPrice = rs.getFloat("medPrice");
			Product product = new Product(name, providerName);
			product.setMedPrice(medPrice);
			list.add(product);
		}
		pstm.close();
		return list;
	}

	public static List<HashMap<String, Object>> queryPricesHistory(
			Connection connection, String prdName) throws SQLException {
		String sql = "SELECT (iva_price/quantity) AS price, date_ins FROM gestione_cantieri.order WHERE product_name = ?"
				+ " ORDER BY date_ins ASC";

		PreparedStatement pstm = connection.prepareStatement(sql);
		pstm.setString(1, prdName);
		logger.info("queryPricesHistory: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;
		while (rs.next()) {
			map = new HashMap<>();
			float price = rs.getFloat("price");
			Date date_ins = rs.getTimestamp("date_ins");
			map.put("date_ins", date_ins);
			map.put("price", price);
			list.add(map);
		}
		pstm.close();
		return list;
	}
}
