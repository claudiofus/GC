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

import gc.model.Order;
import gc.model.Product;
import gc.model.types.BaseOrder;

public class DBOrder {

	public static List<Order> findOrder(Connection conn, String buildingName)
			throws SQLException {
		String sql = "SELECT ord.id, ord.product_id, ord.building_id, ord.product_name, ord.um, ord.quantity, "
				+ "ord.price, ord.discount, ord.adj_price, ord.iva, ord.date_ins FROM gestione_cantieri.order ord "
				+ "INNER JOIN gestione_cantieri.building bui ON ord.building_id = bui.id WHERE bui.name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, buildingName);
		System.out.println("findOrder: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Order> list = new ArrayList<Order>();
		while (rs.next()) {
			BaseOrder ord = new BaseOrder();
			ord.setId(rs.getInt("id"));
			ord.setCode(rs.getString("product_id"));
			ord.setName(rs.getString("product_name"));
			ord.setUm(rs.getString("um"));
			ord.setQuantity(rs.getFloat("quantity"));
			ord.setPrice(rs.getFloat("price"));
			ord.setDiscount(rs.getFloat("discount"));
			ord.setAdj_price(rs.getFloat("adj_price"));
			ord.setIva(rs.getFloat("iva"));
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
			sql = "INSERT INTO gestione_cantieri.order (product_id, building_id, product_name, um, quantity, price, discount,"
					+ " adj_price, iva, date_ins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "INSERT INTO gestione_cantieri.order (product_id, product_name, um, quantity, price, discount, adj_price,"
					+ " iva, date_ins) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		}

		PreparedStatement pstm = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, ordine.getCode());

		int index = 2;
		if (building) {
			pstm.setLong(index++, ordine.getBuilding_id());
		}

		pstm.setString(index++, ordine.getName());
		pstm.setString(index++, ordine.getUm());
		pstm.setFloat(index++, ordine.getQuantity());
		pstm.setFloat(index++, ordine.getPrice());
		pstm.setFloat(index++, ordine.getDiscount());
		pstm.setFloat(index++, ordine.getAdj_price());
		pstm.setFloat(index++, ordine.getIva());
		pstm.setDate(index++, ordine.getDate_order());

		System.out.println("insertOrdine: " + pstm.toString());

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
		String sql = "SELECT id, product_id, building_id, product_name, um, quantity, price, discount, adj_price, iva,"
				+ " date_ins FROM gestione_cantieri.order WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, ordine.getId());
		System.out.println("selectOrdine: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Order ord = null;
		while (rs.next()) {
			ord = ordine.getClass()
					.getConstructor(int.class, String.class, String.class,
							String.class, float.class, float.class, float.class,
							float.class, float.class, java.sql.Date.class)
					.newInstance(rs.getInt("id"), rs.getString("product_id"),
							rs.getString("product_name"), rs.getString("um"),
							rs.getFloat("quantity"), rs.getFloat("price"),
							rs.getFloat("discount"), rs.getFloat("adj_price"),
							rs.getFloat("iva"), rs.getDate("date_ins"));
			ord.setBuilding_id(rs.getInt("building_id"));
		}
		pstm.close();
		return ord;
	}

	public static void updateOrdine(Connection conn, Order ordine)
			throws SQLException {
		String sql = "UPDATE gestione_cantieri.order SET um = ?, quantity = ?, price = ?, discount = ?, adj_price = ?,"
				+ " iva = ?, building_id = ? WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, ordine.getUm());
		pstm.setFloat(2, ordine.getQuantity());
		pstm.setFloat(3, ordine.getPrice());
		pstm.setFloat(4, ordine.getDiscount());
		pstm.setFloat(5, ordine.getAdj_price());
		pstm.setFloat(6, ordine.getIva());

		if (ordine.getBuilding_id() != null) {
			pstm.setInt(7, ordine.getBuilding_id());
		} else {
			pstm.setNull(7, java.sql.Types.INTEGER);
		}
		pstm.setInt(8, ordine.getId());

		System.out.println("updateOrdine: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static List<Product> queryProductPrice(Connection connection)
			throws SQLException {
		String sql = "SELECT prices.product_id, prices.product_name, prices.provider_code, AVG(prices.price) AS medPrice "
				+ "FROM (SELECT ord.product_id, ord.product_name, ord.quantity, prd.provider_code, (ord.adj_price/quantity) AS price "
				+ "FROM gestione_cantieri.order ord inner join gestione_cantieri.product prd on ord.product_id = prd.id) AS prices "
				+ "GROUP BY prices.product_id, prices.product_name order by prices.provider_code, prices.product_id, prices.product_name;";

		PreparedStatement pstm = connection.prepareStatement(sql);
		System.out.println("queryProductPrice: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			String code = rs.getString("product_id");
			String name = rs.getString("product_name");
			String providerCode = rs.getString("provider_code");
			float medPrice = rs.getFloat("medPrice");
			Product product = new Product(code, name, providerCode);
			product.setMedPrice(medPrice);
			list.add(product);
		}
		pstm.close();
		return list;
	}

	public static List<HashMap<String, Object>> queryPricesHistory(
			Connection connection, String prdName) throws SQLException {
		String sql = "SELECT (adj_price/quantity) AS price, date_ins FROM gestione_cantieri.order WHERE product_name = ?"
				+ " ORDER BY date_ins ASC";

		PreparedStatement pstm = connection.prepareStatement(sql);
		pstm.setString(1, prdName);
		System.out.println("queryPricesHistory: " + pstm.toString());

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
