package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.model.Vehicle;
import gc.utils.DBUtils;

public class VehicleDaoImpl {

	public List<Vehicle> getVehicles() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBUtils.queryVehicle(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vehiclesData;
	}

	public Vehicle insertVehicle(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBUtils.insertVehicle(conn, vehicle);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return vehicle;
	}
	
	public Vehicle updateVehicle(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBUtils.updateVehicle(conn, vehicle);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return vehicle;
	}
}