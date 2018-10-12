package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.db.DBBuilding;
import gc.model.Building;

public class BuildingDaoImpl {

	public List<Building> getBuildings() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Building> buildingData = new ArrayList<>();

		try {
			buildingData = DBBuilding.queryBuilding(conn);
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
			building = DBBuilding.findBuilding(conn, name);
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
			DBBuilding.insertBuilding(conn, building);
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