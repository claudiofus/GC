package gc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBVehicle;
import gc.model.Vehicle;
import gc.model.types.CarTax;
import gc.model.types.Insurance;
import gc.model.types.Penalty;
import gc.model.types.Revision;
import gc.utils.Constants;

public class VehicleDaoImpl implements Dao<Vehicle> {
	private static final Logger logger = LogManager.getLogger(VehicleDaoImpl.class.getName());

	@Override
	public Vehicle get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vehicle> getAll() {
		List<Vehicle> vehiclesData = DBVehicle.queryVehicle(conn);
		return vehiclesData;
	}

	@Override
	public void save(Vehicle vehicle) {
		try {
			if (vehicle.getInsurance() != null) {
				Insurance ins = DBVehicle.insertInsurance(conn, vehicle.getInsurance(), vehicle.getPlate());
				vehicle.setInsurance(ins);
			}

			if (vehicle.getCarTax() != null) {
				CarTax cartax = DBVehicle.insertCarTax(conn, vehicle.getCarTax(), vehicle.getPlate());
				vehicle.setCarTax(cartax);
			}

			if (vehicle.getRevision() != null) {
				Revision rev = DBVehicle.insertRevision(conn, vehicle.getRevision(), vehicle.getPlate());
				vehicle.setRevision(rev);
			}

			DBVehicle.insertVehicle(conn, vehicle);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertVehicle: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}

	@Override
	public void update(Vehicle t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Vehicle t) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Vehicle> getInsurances() {
		List<Vehicle> vehiclesData = DBVehicle.queryInsurance(conn);
		return vehiclesData;
	}

	public List<Vehicle> getCarTaxes() {
		List<Vehicle> vehiclesData = DBVehicle.queryCarTax(conn);
		return vehiclesData;
	}

	public List<Vehicle> getRevisions() {
		List<Vehicle> vehiclesData = DBVehicle.queryRevision(conn);
		return vehiclesData;
	}

	public List<Vehicle> getPenalties() {
		List<Vehicle> vehiclesData = DBVehicle.queryPenalty(conn);
		return vehiclesData;
	}

	public Vehicle insertPenalty(Vehicle vehicle) {
		try {
			List<Penalty> penalty = new ArrayList<>();
			if (vehicle.getPenalty() != null) {
				penalty.add(DBVehicle.insertPenalty(conn, vehicle.getPenalty(), vehicle.getPlate()));
				vehicle.setPenalty(penalty);
			}

			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertPenalty: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}

		return vehicle;
	}

	public Vehicle updateInsurance(Vehicle vehicle) {
		try {
			DBVehicle.updateInsurance(conn, vehicle);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method updateInsurance: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}

		return vehicle;
	}

	public Vehicle updateCarTax(Vehicle vehicle) {
		try {
			DBVehicle.updateCarTax(conn, vehicle);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method updateCarTax: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}

		return vehicle;
	}

	public Vehicle updateRevision(Vehicle vehicle) {
		try {
			DBVehicle.updateRevision(conn, vehicle);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method updateRevision: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}

		return vehicle;
	}
}