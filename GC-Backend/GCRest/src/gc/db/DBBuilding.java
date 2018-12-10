package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Building;
import gc.model.types.Address;

public class DBBuilding {
	private static final Logger logger = LogManager.getLogger(DBBuilding.class.getName());

	public static List<Building> queryBuilding(Connection conn) throws SQLException {
		String sql = "SELECT id, name, open, start_date, end_date, address_type, address_name, address_number, cap, city,"
				+ " province, state, req_amount FROM gestione_cantieri.building";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryBuilding: " + pstm.toString());

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

	public static Building findBuilding(Connection conn, String nameToFind) throws SQLException {
		String sql = "SELECT id, name, start_date, end_date, open, address_type, address_name, address_number, cap, city,"
				+ " province, state, req_amount FROM gestione_cantieri.building where name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, nameToFind);
		logger.info("findBuilding: " + pstm.toString());

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

	public static int insertBuilding(Connection conn, Building buildingData) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.building (name, start_date, end_date, open, address_type, address_name,"
				+ " address_number, cap, city, province, state, req_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

		logger.info("insertBuilding: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting building failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				buildingData.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Inserting building failed, no ID obtained.");
			}
		}
		pstm.close();
		return buildingData.getId();
	}
}
