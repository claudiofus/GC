package gc.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gc.model.Building;
import gc.model.Event;
import gc.model.Order;
import gc.model.Product;
import gc.model.Provider;
import gc.model.types.Address;
import gc.model.types.BaseOrder;

public class DBUtils {

	/* PRODUCT */
	public static void insertProduct(Connection conn, Product product)
			throws SQLException {
		String sql = "Insert into product(id, name, provider_code) values (?,?,?)";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, product.getCode());
		pstm.setString(2, product.getName());
		pstm.setString(3, product.getProviderCode());
		System.out.println("insertProduct: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static List<Product> queryProduct(Connection conn)
			throws SQLException {
		String sql = "Select id, name, provider_code from gestione_cantieri.product";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryProduct: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			String code = rs.getString("id");
			String name = rs.getString("name");
			String providerCode = rs.getString("provider_code");
			Product product = new Product(code, name, providerCode);
			list.add(product);
		}

		pstm.close();
		return list;
	}

	public static Product findProduct(Connection conn, Product prd)
			throws SQLException {
		String sql = "Select id, name, provider_code from gestione_cantieri.product where id = ? and name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, prd.getCode());
		pstm.setString(2, prd.getName());
		System.out.println("findProduct: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Product product = null;
		while (rs.next()) {
			String name = rs.getString("Name");
			String providerCode = rs.getString("provider_code");
			product = new Product(prd.getCode(), name, providerCode);
		}

		pstm.close();
		return product;
	}

	public static void deleteProduct(Connection conn, String code)
			throws SQLException {
		String sql = "Delete From Product where Code= ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, code);
		System.out.println("deleteProduct: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	/* PROVIDER */
	public static List<Provider> queryProvider(Connection conn)
			throws SQLException {
		String sql = "Select id, name, code from gestione_cantieri.provider";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryProvider: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Provider> list = new ArrayList<Provider>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String code = rs.getString("code");
			Provider provider = new Provider(id, name, code);
			list.add(provider);
		}
		pstm.close();
		return list;
	}

	public static Provider findProvider(Connection connection,
			String codeToFind) throws SQLException {
		String sql = "Select id, name, code from gestione_cantieri.provider where code = ?";

		PreparedStatement pstm = connection.prepareStatement(sql);
		pstm.setString(1, codeToFind);
		System.out.println("findProvider: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Provider provider = null;
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String code = rs.getString("code");
			provider = new Provider(id, name, code);
		}
		pstm.close();
		return provider;
	}

	/* ORDER */
	public static List<Order> findOrder(Connection conn, String buildingName)
			throws SQLException {
		String sql = "Select ord.id, ord.product_id, ord.building_id, ord.product_name, ord.um, ord.quantity, "
				+ "ord.price, ord.discount, ord.adj_price, ord.iva, ord.date_ins from gestione_cantieri.order ord "
				+ "inner join gestione_cantieri.building bui on ord.building_id = bui.id where bui.name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, buildingName);
		System.out.println("queryOrder: " + pstm.toString());

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
			sql = "INSERT INTO gestione_cantieri.order (product_id, building_id, product_name, um, quantity, price, discount, adj_price, iva, date_ins) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "INSERT INTO gestione_cantieri.order (product_id, product_name, um, quantity, price, discount, adj_price, iva, date_ins) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				ordine.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating user failed, no ID obtained.");
			}
		}
		pstm.close();
		return ordine.getId();
	}

	public static Order selectOrdine(Connection conn, Order ordine)
			throws Exception {
		String sql = "Select id, product_id, building_id, product_name, um, quantity, price, discount, adj_price, iva, date_ins from gestione_cantieri.order where id = ?";

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
		String sql = "Update gestione_cantieri.order set um = ?, quantity = ?, price = ?, discount = ?, adj_price = ?, iva = ?, building_id = ? where id = ?";

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
		String sql = "SELECT prices.product_id, prices.product_name, prices.provider_code, AVG(prices.price) as medPrice "
				+ "FROM (SELECT ord.product_id, ord.product_name, ord.quantity, prd.provider_code, (ord.adj_price/quantity) as price "
				+ "FROM gestione_cantieri.order ord inner join gestione_cantieri.product prd on ord.product_id = prd.id) as prices "
				+ "group by prices.product_id, prices.product_name order by prices.provider_code, prices.product_id, prices.product_name;";

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
		String sql = "select (adj_price/quantity) as price, date_ins FROM gestione_cantieri.order where product_name = ? order by date_ins asc";

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

	/* BUILDING */
	public static List<Building> queryBuilding(Connection conn)
			throws SQLException {
		String sql = "Select id, name, open, start_date, end_date, address_type, address_name, address_number, cap, city, province, state, req_amount "
				+ "from gestione_cantieri.building";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryBuilding: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Building> list = new ArrayList<Building>();
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String name = rs.getString("name");
			boolean open = rs.getBoolean("open");
			java.sql.Date start_date = rs.getDate("start_date");
			java.sql.Date end_date = rs.getDate("end_date");
			String address_type = rs.getString("address_type");
			String address_name = rs.getString("address_name");
			String address_number = rs.getString("address_number");
			String cap = rs.getString("cap");
			String city = rs.getString("city");
			String province = rs.getString("province");
			String state = rs.getString("state");
			Float req_amount = rs.getFloat("req_amount");

			Building building = new Building(name);
			building.setId(id);
			building.setOpen(open);
			building.setStart_date(start_date);
			building.setEnd_date(end_date);
			building.setReq_amount(req_amount);

			Address addr = new Address();
			addr.setAddressType(address_type);
			addr.setAddressName(address_name);
			addr.setAddressNumber(address_number);
			addr.setCap(cap);
			addr.setCity(city);
			addr.setProvince(province);
			addr.setState(state);
			building.setAddress(addr);

			list.add(building);
		}

		pstm.close();
		return list;
	}

	public static Building findBuilding(Connection conn, String nameToFind)
			throws SQLException {
		String sql = "Select id, name, start_date, end_date, open, address_type, address_name, address_number, cap, city, province, state, req_amount from gestione_cantieri.building where name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, nameToFind);
		System.out.println("findBuilding: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Building building = null;
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String name = rs.getString("name");
			java.sql.Date start_date = rs.getDate("start_date");
			java.sql.Date end_date = rs.getDate("end_date");
			boolean open = rs.getBoolean("open");
			String address_type = rs.getString("address_type");
			String address_name = rs.getString("address_name");
			String address_number = rs.getString("address_number");
			String cap = rs.getString("cap");
			String city = rs.getString("city");
			String province = rs.getString("province");
			String state = rs.getString("state");
			Float req_amount = rs.getFloat("req_amount");

			building = new Building(name);
			building.setId(id);
			building.setOpen(open);
			building.setStart_date(start_date);
			building.setEnd_date(end_date);
			building.setReq_amount(req_amount);

			Address addr = new Address();
			addr.setAddressType(address_type);
			addr.setAddressName(address_name);
			addr.setAddressNumber(address_number);
			addr.setCap(cap);
			addr.setCity(city);
			addr.setProvince(province);
			addr.setState(state);
			building.setAddress(addr);
		}
		pstm.close();
		return building;
	}

	public static int insertBuilding(Connection conn, Building buildingData)
			throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.building (name, start_date, end_date, open, address_type, address_name, "
				+ "address_number, cap, city, province, state, req_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, buildingData.getName());
		pstm.setDate(2, buildingData.getStart_date());
		pstm.setDate(3, buildingData.getEnd_date());
		pstm.setBoolean(4, buildingData.isOpen());
		pstm.setString(5, buildingData.getAddress().getAddressType());
		pstm.setString(6, buildingData.getAddress().getAddressName());
		pstm.setString(7, buildingData.getAddress().getAddressNumber());
		pstm.setString(8, buildingData.getAddress().getCap());
		pstm.setString(9, buildingData.getAddress().getCity());
		pstm.setString(10, buildingData.getAddress().getProvince());
		pstm.setString(11, buildingData.getAddress().getState());
		pstm.setFloat(12, buildingData.getReq_amount());

		System.out.println("insertBuilding: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				buildingData.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating user failed, no ID obtained.");
			}
		}
		pstm.close();
		return buildingData.getId();
	}

	/* EVENT */
	public static List<Event> queryEvent(Connection conn) throws SQLException {
		String sql = "Select id, title, start_date, end_date, executed from gestione_cantieri.event";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryEvents: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Event> list = new ArrayList<Event>();
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String title = rs.getString("title");
			Timestamp start_date = rs.getTimestamp("start_date");
			Timestamp end_date = rs.getTimestamp("end_date");
			boolean executed = rs.getBoolean("executed");

			Event ev = new Event(title);
			ev.setId(id);
			ev.setStart_date(new java.sql.Date(start_date.getTime()));
			ev.setEnd_date(new java.sql.Date(end_date.getTime()));
			ev.setExecuted(executed);
			list.add(ev);
		}

		pstm.close();
		return list;
	}

	public static int insertEvent(Connection conn, Event eventData)
			throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.event (title, start_date, end_date, executed) VALUES (?, ?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, eventData.getTitle());
		pstm.setDate(2, eventData.getStart_date());
		pstm.setDate(3, eventData.getEnd_date());
		pstm.setBoolean(4, eventData.isExecuted());

		System.out.println("insertEvent: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				eventData.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating user failed, no ID obtained.");
			}
		}
		pstm.close();
		return eventData.getId();
	}
}
