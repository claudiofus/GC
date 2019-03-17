package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBVehicle;
import gc.model.Vehicle;
import gc.model.types.CarTax;
import gc.model.types.Insurance;
import gc.model.types.Penalty;
import gc.model.types.Revision;

public class VehicleDaoImpl {
	private static final Logger logger = LogManager
			.getLogger(VehicleDaoImpl.class.getName());

	public List<Vehicle> getVehicles() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryVehicle(conn);
		} catch (SQLException e) {
			logger.error("Error in method getVehicles: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return vehiclesData;
	}

	public List<Vehicle> getInsurances() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryInsurance(conn);
		} catch (SQLException e) {
			logger.error("Error in method getInsurances: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return vehiclesData;
	}

	public List<Vehicle> getCarTaxes() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryCarTax(conn);
		} catch (SQLException e) {
			logger.error("Error in method getCarTaxes: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return vehiclesData;
	}

	public List<Vehicle> getRevisions() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryRevision(conn);
		} catch (SQLException e) {
			logger.error("Error in method getRevisions: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return vehiclesData;
	}

	public List<Vehicle> getPenalties() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Vehicle> vehiclesData = new ArrayList<>();

		try {
			vehiclesData = DBVehicle.queryPenalty(conn);
		} catch (SQLException e) {
			logger.error("Error in method getPenalties: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
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
			logger.error("Error in method insertVehicle: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
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
				penalty.add(DBVehicle.insertPenalty(conn, vehicle.getPenalty(),
						vehicle.getPlate()));
				vehicle.setPenalty(penalty);
			}

			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertPenalty: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
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
			logger.error("Error in method updateInsurance: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
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
			logger.error("Error in method updateCarTax: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
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
			logger.error("Error in method updateRevision: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return vehicle;
	}
}