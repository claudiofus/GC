package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.db.DBVehicle;
import gc.model.Vehicle;
import gc.model.types.CarTax;
import gc.model.types.Insurance;
import gc.model.types.Penalty;
import gc.model.types.Revision;

public class VehicleDaoImpl {

	public List<Vehicle> getVehicles() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryVehicle(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vehiclesData;
	}

	public List<Vehicle> getInsurances() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryInsurance(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vehiclesData;
	}

	public List<Vehicle> getCarTaxes() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryCarTax(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vehiclesData;
	}

	public List<Vehicle> getRevisions() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryRevision(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vehiclesData;
	}
	
	public List<Vehicle> getPenalties() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryPenalty(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vehiclesData;
	}

	public Vehicle insertVehicle(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			if (vehicle.getInsurance() != null) {
				Insurance ins = DBVehicle.insertInsurance(conn,
						vehicle.getInsurance(), vehicle.getPlate());
				vehicle.setInsurance(ins);
			}

			if (vehicle.getCarTax() != null) {
				CarTax cartax = DBVehicle.insertCarTax(conn,
						vehicle.getCarTax(), vehicle.getPlate());
				vehicle.setCarTax(cartax);
			}

			if (vehicle.getRevision() != null) {
				Revision rev = DBVehicle.insertRevision(conn,
						vehicle.getRevision(), vehicle.getPlate());
				vehicle.setRevision(rev);
			}

			DBVehicle.insertVehicle(conn, vehicle);
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

	public Vehicle insertPenalty(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			List<Penalty> penalty = new ArrayList<Penalty>();
			if (vehicle.getPenalty() != null) {
				penalty.add(DBVehicle.insertPenalty(conn, vehicle.getPenalty(), vehicle.getPlate()));
				vehicle.setPenalty(penalty);
			}

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
	
	public Vehicle updateInsurance(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBVehicle.updateInsurance(conn, vehicle);
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
	
	public Vehicle updateCarTax(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBVehicle.updateCarTax(conn, vehicle);
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
	
	public Vehicle updateRevision(Vehicle vehicle) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBVehicle.updateRevision(conn, vehicle);
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