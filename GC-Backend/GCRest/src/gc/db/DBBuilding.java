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

import gc.model.Building;
import gc.model.types.Address;
import gc.model.types.job.Job;
import gc.utils.Utils;

public class DBBuilding {
	private static final Logger logger = LogManager.getLogger(DBBuilding.class.getName());

	private DBBuilding() {
		throw new IllegalStateException("DBBuilding class");
	}

	public static List<Building> queryBuilding(Connection conn) {
		String sql = "SELECT id, name, open, start_date, end_date, address_type, address_name, address_number, cap, city,"
				+ " province, state, req_amount FROM gestione_cantieri.building";

		List<Building> buildingList = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			String message = pstm.toString();
			logger.info("queryBuilding: {}", message);

			buildingList = getBuildingList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method queryBuilding: {}", e);
		}
		return buildingList;
	}

	public static Building findBuilding(Connection conn, String nameToFind) {
		String sql = "SELECT id, name, start_date, end_date, open, address_type, address_name, address_number, cap, city,"
				+ " province, state, req_amount FROM gestione_cantieri.building where name = ?";

		Building building = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, nameToFind);
			String query = pstm.toString();
			logger.info("findBuilding: {}", query);

			List<Building> buildingList = getBuildingList(pstm);
			if (!buildingList.isEmpty()) {
				building = buildingList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method findBuilding: {}", e);
		}
		return building;
	}

	public static int insertBuilding(Connection conn, Building buildingData) {
		String sql = "INSERT INTO gestione_cantieri.building (name, start_date, end_date, open, address_type, address_name,"
				+ " address_number, cap, city, province, state, req_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			createPstm(pstm, buildingData, false);

			String query = pstm.toString();
			logger.info("insertBuilding: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting building failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				buildingData.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertBuilding: {}", e);
		}
		return buildingData.getId();
	}

	public static int updateBuilding(Connection conn, Building buildingData) {
		String sql = "UPDATE gestione_cantieri.building SET name = ?, start_date = ?, end_date = ?, open = ?, address_type = ?,"
				+ " address_name = ?, address_number = ?, cap = ?, city = ?, province = ?, state = ?, req_amount = ? WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			createPstm(pstm, buildingData, true);

			String query = pstm.toString();
			logger.info("updateBuilding: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating building failed, no rows affected.");
			}
		} catch (SQLException e) {
			logger.error("Error in method updateBuilding: {}", e);
		}
		return buildingData.getId();
	}

	public static List<Job> getJobs(Connection conn, int id) {
		String sql = "SELECT id, worker_id, dateOfWork, hoursOfWork FROM gestione_cantieri.building_worker "
				+ "WHERE building_id = ? ORDER BY dateOfWork DESC";

		List<Job> jobs = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("getJobs: {}", query);

			jobs = getJobsList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method getJobs: {}", e);
		}
		return jobs;
	}

	public static void deleteJob(Connection conn, int id) {
		String sql = "DELETE FROM gestione_cantieri.building_worker WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);

			int result = pstm.executeUpdate();

			if (result != 0) {
				logger.info("Event with id = {} deleted", id);
			} else {
				logger.info("No event was deleted with id {}", id);
			}
		} catch (SQLException e) {
			logger.error("Error in method deleteJob: {}", e);
		}
	}

	private static List<Building> getBuildingList(PreparedStatement pstm) {
		List<Building> buildingList = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				boolean open = rs.getBoolean("open");
				java.sql.Date startDate = rs.getDate("start_date");
				java.sql.Date endDate = rs.getDate("end_date");
				String addressType = rs.getString("address_type");
				String addressName = rs.getString("address_name");
				String addressNumber = rs.getString("address_number");
				String cap = rs.getString("cap");
				String city = rs.getString("city");
				String province = rs.getString("province");
				String state = rs.getString("state");
				Float reqAmount = rs.getFloat("req_amount");

				Building building = new Building(name);
				building.setId(id);
				building.setOpen(open);
				building.setStartDate(startDate);
				building.setEndDate(endDate);
				building.setReqAmount(reqAmount);

				Address addr = new Address();
				addr.setAddressType(addressType);
				addr.setAddressName(addressName);
				addr.setAddressNumber(addressNumber);
				addr.setCap(cap);
				addr.setCity(city);
				addr.setProvince(province);
				addr.setState(state);
				building.setAddress(addr);

				buildingList.add(building);
			}
		} catch (SQLException e) {
			logger.error("Error in method getBuildingList: {}", e);
		}

		return buildingList;
	}

	private static List<Job> getJobsList(PreparedStatement pstm) {
		List<Job> jobs = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			Job job = null;
			while (rs.next()) {
				job = new Job();
				job.setId(rs.getInt("id"));
				job.setWorkerId(rs.getInt("worker_id"));
				job.setDateOfWork(rs.getDate("dateOfWork"));
				job.setHoursOfWork(rs.getInt("hoursOfWork"));
				jobs.add(job);
			}
		} catch (SQLException e) {
			logger.error("Error in method getJobs: {}", e);
		}

		return jobs;
	}
	
	private static void createPstm(PreparedStatement pstm, Building buildingData, boolean setID) throws SQLException {
		pstm.setString(1, buildingData.getName());
		pstm.setDate(2, buildingData.getStartDate());
		pstm.setDate(3, buildingData.getEndDate());
		pstm.setBoolean(4, buildingData.isOpen());
		pstm.setString(5, buildingData.getAddress().getAddressType());
		pstm.setString(6, buildingData.getAddress().getAddressName());
		pstm.setString(7, buildingData.getAddress().getAddressNumber());
		pstm.setString(8, buildingData.getAddress().getCap());
		pstm.setString(9, buildingData.getAddress().getCity());
		pstm.setString(10, buildingData.getAddress().getProvince());
		pstm.setString(11, buildingData.getAddress().getState());
		pstm.setFloat(12, buildingData.getReqAmount());
		if (setID) {
			pstm.setInt(13, buildingData.getId());
		}
	}
}
