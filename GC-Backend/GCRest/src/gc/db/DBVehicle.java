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

import gc.model.Event;
import gc.model.Vehicle;
import gc.model.types.CarTax;
import gc.model.types.Insurance;
import gc.model.types.Penalty;
import gc.model.types.Revision;

public class DBVehicle {
	private static final Logger logger = LogManager.getLogger(DBVehicle.class.getName());

	public static List<Vehicle> queryVehicle(Connection conn) throws SQLException {
		String sql = "SELECT brand, model, color, first_registration, plate FROM gestione_cantieri.vehicle";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryVehicle: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Vehicle> list = new ArrayList<Vehicle>();
		Vehicle vehicle;
		while (rs.next()) {
			String brand = rs.getString("brand");
			String model = rs.getString("model");
			String color = rs.getString("color");
			java.sql.Date firstReg = rs.getDate("first_registration");
			String plate = rs.getString("plate");

			vehicle = new Vehicle(brand, model, color, firstReg, plate);
			list.add(vehicle);
		}

		pstm.close();
		return list;
	}

	public static List<Vehicle> queryInsurance(Connection conn) throws SQLException {
		String sql = "SELECT i.plate, i.eventID, e.start_date, i.amount, e.paid from gestione_cantieri.insurance i "
				+ "INNER JOIN gestione_cantieri.event e ON i.eventID = e.id";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryInsurance: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Vehicle> list = new ArrayList<Vehicle>();
		Vehicle vehicle;
		while (rs.next()) {
			String plate = rs.getString("i.plate");
			int eventID = rs.getInt("i.eventID");
			java.sql.Date deadlineDate = rs.getDate("e.start_date");
			float amount = rs.getFloat("i.amount");
			boolean paid = rs.getBoolean("e.paid");

			vehicle = new Vehicle();
			vehicle.setPlate(plate);
			Insurance ins = new Insurance();
			ins.setDeadlineDate(deadlineDate);
			ins.setEventID(eventID);
			ins.setAmount(amount);
			ins.setPaid(paid);
			vehicle.setInsurance(ins);
			list.add(vehicle);
		}

		pstm.close();
		return list;
	}

	public static List<Vehicle> queryCarTax(Connection conn) throws SQLException {
		String sql = "SELECT c.plate, c.eventID, e.start_date, c.amount, e.paid FROM gestione_cantieri.car_tax c"
				+ " INNER JOIN gestione_cantieri.event e ON c.eventID = e.id";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryCarTax: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Vehicle> list = new ArrayList<Vehicle>();
		Vehicle vehicle;
		while (rs.next()) {
			String plate = rs.getString("c.plate");
			int eventID = rs.getInt("c.eventID");
			java.sql.Date deadlineDate = rs.getDate("e.start_date");
			float amount = rs.getFloat("c.amount");
			boolean paid = rs.getBoolean("e.paid");

			vehicle = new Vehicle();
			vehicle.setPlate(plate);
			CarTax ct = new CarTax();
			ct.setDeadlineDate(deadlineDate);
			ct.setEventID(eventID);
			ct.setAmount(amount);
			ct.setPaid(paid);
			vehicle.setCarTax(ct);
			list.add(vehicle);
		}

		pstm.close();
		return list;
	}

	public static List<Vehicle> queryRevision(Connection conn) throws SQLException {
		String sql = "SELECT r.plate, r.eventID, e.start_date, r.amount, e.paid FROM gestione_cantieri.revision r "
				+ "INNER JOIN gestione_cantieri.event e ON r.eventID = e.id";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryRevision: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Vehicle> list = new ArrayList<Vehicle>();
		Vehicle vehicle;
		while (rs.next()) {
			String plate = rs.getString("r.plate");
			int eventID = rs.getInt("r.eventID");
			java.sql.Date deadlineDate = rs.getDate("e.start_date");
			float amount = rs.getFloat("r.amount");
			boolean paid = rs.getBoolean("e.paid");

			vehicle = new Vehicle();
			vehicle.setPlate(plate);
			Revision rev = new Revision();
			rev.setDeadlineDate(deadlineDate);
			rev.setEventID(eventID);
			rev.setAmount(amount);
			rev.setPaid(paid);
			vehicle.setRevision(rev);
			list.add(vehicle);
		}

		pstm.close();
		return list;
	}

	public static List<Vehicle> queryPenalty(Connection conn) throws SQLException {
		String sql = "SELECT p.plate, p.eventID, p.date, p.amount, p.description, p.points, e.paid"
				+ " FROM gestione_cantieri.penalty p INNER JOIN gestione_cantieri.event e ON p.eventID = e.id";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryPenalty: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Vehicle> list = new ArrayList<Vehicle>();
		List<Penalty> penaltyList = new ArrayList<Penalty>();
		Vehicle vehicle = new Vehicle();
		while (rs.next()) {
			String plate = rs.getString("p.plate");
			int eventID = rs.getInt("p.eventID");
			java.sql.Date deadlineDate = rs.getDate("p.date");
			float amount = rs.getFloat("p.amount");
			String description = rs.getString("p.description");
			int points = rs.getInt("p.points");
			boolean paid = rs.getBoolean("e.paid");

			Penalty penalty = new Penalty();
			penalty.setEventID(eventID);
			penalty.setDeadlineDate(deadlineDate);
			penalty.setAmount(amount);
			penalty.setDescription(description);
			penalty.setPoints(points);
			penalty.setPaid(paid);
			if (!plate.equalsIgnoreCase(vehicle.getPlate())) {
				penaltyList = new ArrayList<Penalty>();
				vehicle = new Vehicle();
				vehicle.setPlate(plate);
				vehicle.setPenalty(penaltyList);
				list.add(vehicle);
			}
			penaltyList.add(penalty);
		}

		pstm.close();
		return list;
	}

	public static Vehicle insertVehicle(Connection conn, Vehicle vehicleData) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.vehicle (brand, model, color, first_registration, plate) "
				+ "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		try {
			pstm.setString(1, vehicleData.getBrand());
			pstm.setString(2, vehicleData.getModel());
			pstm.setString(3, vehicleData.getColor());
			pstm.setDate(4, vehicleData.getFirstRegistration());
			pstm.setString(5, vehicleData.getPlate());

			logger.info("insertVehicle: " + pstm.toString());

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting vehicle failed, no rows affected.");
			}
		} catch (Exception e) {
			logger.error("Error in method insertVehicle: ", e);
		} finally {
			pstm.close();
		}

		return vehicleData;
	}

	public static Vehicle updateInsurance(Connection conn, Vehicle vehicleData) throws SQLException {
		String sql = "UPDATE gestione_cantieri.insurance SET eventID = ?, amount = ? WHERE plate = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		try {
			pstm.setInt(1, vehicleData.getInsurance().getEventID());
			pstm.setFloat(2, vehicleData.getInsurance().getAmount());
			pstm.setString(3, vehicleData.getPlate());

			logger.info("updateInsurance: " + pstm.toString());

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating insurance failed, no rows affected.");
			}

			Event ev = DBEvent.selectEvent(conn, vehicleData.getInsurance().getEventID());
			ev.setStart_date(vehicleData.getInsurance().getDeadlineDate());
			ev.setPaid(vehicleData.getInsurance().isPaid());
			DBEvent.updateEvent(conn, ev);
		} catch (Exception e) {
			logger.error("Error in method updateInsurance: ", e);
		} finally {
			pstm.close();
		}

		return vehicleData;
	}

	public static Vehicle updateCarTax(Connection conn, Vehicle vehicleData) throws SQLException {
		String sql = "UPDATE gestione_cantieri.car_tax SET eventID = ?, amount = ? WHERE plate = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		try {
			pstm.setInt(1, vehicleData.getCarTax().getEventID());
			pstm.setFloat(2, vehicleData.getCarTax().getAmount());
			pstm.setString(3, vehicleData.getPlate());

			logger.info("updateCarTax: " + pstm.toString());

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating car tax failed, no rows affected.");
			}

			Event ev = DBEvent.selectEvent(conn, vehicleData.getCarTax().getEventID());
			ev.setStart_date(vehicleData.getCarTax().getDeadlineDate());
			ev.setPaid(vehicleData.getCarTax().isPaid());
			DBEvent.updateEvent(conn, ev);
		} catch (Exception e) {
			logger.error("Error in method updateCarTax: ", e);
		} finally {
			pstm.close();
		}

		return vehicleData;
	}

	public static Vehicle updateRevision(Connection conn, Vehicle vehicleData) throws SQLException {
		String sql = "UPDATE gestione_cantieri.revision SET eventID = ?, amount = ? WHERE plate = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		try {
			pstm.setInt(1, vehicleData.getRevision().getEventID());
			pstm.setFloat(2, vehicleData.getRevision().getAmount());
			pstm.setString(3, vehicleData.getPlate());

			logger.info("updateRevision: " + pstm.toString());

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating revision failed, no rows affected.");
			}

			Event ev = DBEvent.selectEvent(conn, vehicleData.getRevision().getEventID());
			ev.setStart_date(vehicleData.getRevision().getDeadlineDate());
			ev.setPaid(vehicleData.getRevision().isPaid());
			DBEvent.updateEvent(conn, ev);
		} catch (Exception e) {
			logger.error("Error in method updateRevision: ", e);
		} finally {
			pstm.close();
		}

		return vehicleData;
	}

	public static Insurance insertInsurance(Connection conn, Insurance ins, String plate) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.insurance (plate, amount, eventID) VALUES (?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, plate);
		pstm.setFloat(2, ins.getAmount());
		pstm.setInt(3, ins.getEventID());

		logger.info("insertInsurance: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting insurance failed, no rows affected.");
		}
		pstm.close();
		return ins;
	}

	public static CarTax insertCarTax(Connection conn, CarTax cartax, String plate) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.car_tax (plate, amount, eventID) VALUES (?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, plate);
		pstm.setFloat(2, cartax.getAmount());
		pstm.setInt(3, cartax.getEventID());

		logger.info("insertCarTax: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting car tax failed, no rows affected.");
		}
		pstm.close();
		return cartax;
	}

	public static Revision insertRevision(Connection conn, Revision rev, String plate) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.revision (plate, amount, eventID) VALUES (?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, plate);
		pstm.setFloat(2, rev.getAmount());
		pstm.setInt(3, rev.getEventID());

		logger.info("insertRevision: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting revision failed, no rows affected.");
		}

		pstm.close();
		return rev;
	}

	public static Penalty insertPenalty(Connection conn, List<Penalty> penalties, String plate) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.penalty (plate, date, amount, eventID, description, points, paid)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		Penalty penalty = penalties.get(penalties.size() - 1);
		pstm.setString(1, plate);
		pstm.setDate(2, penalty.getDeadlineDate());
		pstm.setFloat(3, penalty.getAmount());
		pstm.setInt(4, penalty.getEventID());
		pstm.setString(5, penalty.getDescription());
		pstm.setInt(6, penalty.getPoints());
		pstm.setBoolean(7, penalty.isPaid());

		logger.info("insertPenalty: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting penalty failed, no rows affected.");
		}

		pstm.close();
		return penalty;
	}
}
