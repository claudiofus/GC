package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBBuilding;
import gc.model.Building;
import gc.model.types.job.Job;

public class BuildingDaoImpl {
	private static final Logger logger = LogManager.getLogger(BuildingDaoImpl.class.getName());

	public List<Building> getBuildings() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Building> buildingData = new ArrayList<>();

		try {
			buildingData = DBBuilding.queryBuilding(conn);
		} catch (SQLException e) {
			logger.error("Error in method getBuildings: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
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
			logger.error("Error in method getBuildingDetails: ", e);
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

		return building;
	}

	public Building insertBuilding(Building building) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBBuilding.insertBuilding(conn, building);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertBuilding: ", e);
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

		return building;
	}

	public List<Job> getJobs(int id) {
		List<Job> jobs = new ArrayList<Job>();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			jobs = DBBuilding.getJobs(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getBuildingDetails: ", e);
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
		return jobs;
	}

	public boolean deleteJob(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBBuilding.deleteJob(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getBuildingDetails: ", e);
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
		return true;
	}
}