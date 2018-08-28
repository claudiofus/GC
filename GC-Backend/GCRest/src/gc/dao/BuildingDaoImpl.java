package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.model.Building;
import gc.utils.DBUtils;

public class BuildingDaoImpl {

	public List<Building> getBuildings() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Building> buildingData = new ArrayList<>();

		try {
			buildingData = DBUtils.queryBuilding(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return buildingData;
	}

	public Building getBuildingDetails(String name) {
		Building building = new Building();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		
		try {
			building = DBUtils.findBuilding(conn, name);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return building;
	}

	public Building insertBuilding(Building building) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBUtils.insertBuilding(conn, building);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return building;
	}
}