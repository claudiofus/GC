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
import gc.utils.Constants;

public class BuildingDaoImpl {
	private static final Logger logger = LogManager.getLogger(BuildingDaoImpl.class.getName());

	public List<Building> getBuildings() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		return DBBuilding.queryBuilding(conn);
	}

	public Building getBuildingDetails(String name) {
		Building building = new Building();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			building = DBBuilding.findBuilding(conn, name);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getBuildingDetails: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
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
			logger.error("Error in method insertBuilding: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return building;
	}
	
	public Building updateBuilding(Building building) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBBuilding.updateBuilding(conn, building);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method updateBuilding: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return building;
	}

	public List<Job> getJobs(int id) {
		List<Job> jobs = new ArrayList<>();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			jobs = DBBuilding.getJobs(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getJobs: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
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
			logger.error("Error in method deleteJob: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}
		return true;
	}
}