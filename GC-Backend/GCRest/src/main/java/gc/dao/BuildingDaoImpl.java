package gc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBBuilding;
import gc.model.Building;
import gc.model.types.job.Job;
import gc.utils.Constants;

public class BuildingDaoImpl implements Dao<Building> {
	private static final Logger logger = LogManager.getLogger(BuildingDaoImpl.class.getName());
	
	@Override
	public Building get(int id) {
		Building building = new Building();

		try {
			building = DBBuilding.findBuilding(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getBuildingDetails: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}

		return building;
	}

	@Override
	public List<Building> getAll() {
		return DBBuilding.queryBuilding(conn);
	}

	@Override
	public void save(Building building) {
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
		}
	}

	@Override
	public void update(Building building, String[] params) {
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
		}
	}

	@Override
	public void delete(Building t) {
		// TODO Auto-generated method stub
	}
	
	public List<Job> getJobs(int id) {
		List<Job> jobs = new ArrayList<>();

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
		} 
		return jobs;
	}

	public boolean deleteJob(int id) {		
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
		}
		return true;
	}
}