package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Worker;
import gc.model.types.Contacts;
import gc.model.types.job.ContractTerms;
import gc.model.types.job.ContractType;
import gc.model.types.job.Job;
import gc.model.types.job.Qualification;
import gc.model.types.job.Salary;
import gc.utils.Utils;

public class DBWorker {
	private static final Logger logger = LogManager.getLogger(DBWorker.class.getName());

	private DBWorker() {
		throw new IllegalStateException("DBWorker class");
	}

	public static int insertWorker(Connection conn, Worker workerData) {
		String sql = "INSERT INTO gestione_cantieri.worker (name, surname, gender, fiscalCode, birthDate, "
				+ "birthPlace, birthProvPlace, married, telephone, email) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setString(1, workerData.getName());
			pstm.setString(2, workerData.getSurname());
			pstm.setString(3, workerData.getGender());
			pstm.setString(4, workerData.getFiscalCode());
			pstm.setDate(5, workerData.getBirthDate());
			pstm.setString(6, workerData.getBirthPlace());
			pstm.setString(7, workerData.getBirthProvPlace());
			pstm.setBoolean(8, workerData.isMarried());
			if (workerData.getContacts() != null) {
				pstm.setString(9, workerData.getContacts().getTelephone());
				pstm.setString(10, workerData.getContacts().getEmail());
			}

			String query = pstm.toString();
			logger.info("insertWorker: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting worker failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				workerData.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertWorker: {}", e);
		}

		return workerData.getId();
	}

	public static List<Worker> queryWorker(Connection conn) {
		String sql = "SELECT id, name, surname, gender, fiscalCode, birthDate, birthPlace, birthProvPlace, "
				+ "married, telephone, email FROM gestione_cantieri.worker";

		List<Worker> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {

			String query = pstm.toString();
			logger.info("queryWorker: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Worker worker = new Worker();
					worker.setId(rs.getInt("id"));
					worker.setName(rs.getString("name"));
					worker.setSurname(rs.getString("surname"));
					worker.setGender(rs.getString("gender"));
					worker.setFiscalCode(rs.getString("fiscalCode"));
					worker.setBirthDate(rs.getDate("birthDate"));
					worker.setBirthPlace(rs.getString("birthPlace"));
					worker.setBirthProvPlace(rs.getString("birthProvPlace"));
					worker.setMarried(rs.getBoolean("married"));
					Contacts contacts = new Contacts();
					contacts.setTelephone(rs.getString("telephone"));
					contacts.setEmail(rs.getString("email"));
					worker.setContacts(contacts);
					list.add(worker);
				}
			} catch (SQLException e) {
				logger.error("Error in method queryWorker: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method queryWorker: {}", e);
		}

		return list;
	}

	public static void updateWorker(Connection conn, Worker worker) {
		String sql = "UPDATE gestione_cantieri.worker "
				+ "SET name = ?, surname = ?, gender = ?, fiscalCode = ?, birthDate = ?, "
				+ "birthPlace = ?, birthProvPlace = ?, married = ?, telephone = ?, email = ? " + "WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, worker.getName());
			pstm.setString(2, worker.getSurname());
			pstm.setString(3, worker.getGender());
			pstm.setString(4, worker.getFiscalCode());
			pstm.setDate(5, worker.getBirthDate());
			pstm.setString(6, worker.getBirthPlace());
			pstm.setString(7, worker.getBirthProvPlace());
			pstm.setBoolean(8, worker.isMarried());
			if (worker.getContacts() != null) {
				pstm.setString(9, worker.getContacts().getTelephone());
				pstm.setString(10, worker.getContacts().getEmail());
			}
			pstm.setInt(11, worker.getId());

			String query = pstm.toString();
			logger.info("updateWorker: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method updateWorker: {}", e);
		}
	}

	public static Worker selectWorker(Connection conn, int id) {
		String sql = "SELECT w.id, name, w.surname, w.gender, w.fiscalCode, w.birthDate, w.birthPlace, w.birthProvPlace, "
				+ "w.married, w.telephone, w.email, c.id, c.recruitmentDate, c.dismissalDate, c.type, c.qualification, "
				+ "s.id, s.salaryForHour, s.salaryForDay FROM gestione_cantieri.worker w "
				+ "INNER JOIN gestione_cantieri.contract c ON w.id = c.worker_id "
				+ "INNER JOIN gestione_cantieri.salary s ON w.id = s.worker_id WHERE w.id = ?";

		Worker newWorker = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("selectWorker: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					newWorker = new Worker();
					newWorker.setId(rs.getInt("w.id"));
					newWorker.setName(rs.getString("w.name"));
					newWorker.setSurname(rs.getString("w.surname"));
					newWorker.setGender(rs.getString("w.gender"));
					newWorker.setFiscalCode(rs.getString("w.fiscalCode"));
					newWorker.setBirthDate(rs.getDate("w.birthDate"));
					newWorker.setBirthPlace(rs.getString("w.birthPlace"));
					newWorker.setBirthProvPlace(rs.getString("w.birthProvPlace"));
					newWorker.setMarried(rs.getBoolean("w.married"));
					Contacts contacts = new Contacts();
					contacts.setTelephone(rs.getString("w.telephone"));
					contacts.setEmail(rs.getString("w.email"));
					newWorker.setContacts(contacts);
					ContractTerms contractTerms = new ContractTerms();
					contractTerms.setId(rs.getInt("c.id"));
					contractTerms.setContractType(ContractType.valueOf(rs.getString("c.type")));
					contractTerms.setRecruitmentDate(rs.getDate("c.recruitmentDate"));
					contractTerms.setDismissalDate(rs.getDate("c.dismissalDate"));
					contractTerms.setQualification(Qualification.valueOf(rs.getString("c.qualification")));
					newWorker.setContractTerms(contractTerms);
					Salary salary = new Salary();
					salary.setId(rs.getInt("s.id"));
					salary.setSalaryForHour(rs.getBigDecimal("s.salaryForHour"));
					salary.setSalaryForDay(rs.getBigDecimal("s.salaryForDay"));
					newWorker.setSalary(salary);
				}
			} catch (SQLException e) {
				logger.error("Error in method selectWorker: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method selectWorker: {}", e);
		}

		return newWorker;
	}

	public static void assignToBuilding(Connection conn, Job job, int id) {
		String sql = "INSERT INTO gestione_cantieri.building_worker (worker_id, building_id, dateOfWork, hoursOfWork) "
				+ "VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, job.getWorkerId());
			pstm.setInt(2, id);
			pstm.setDate(3, job.getDateOfWork());
			pstm.setInt(4, job.getHoursOfWork());

			String query = pstm.toString();
			logger.info("assignToBuilding: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method assignToBuilding: {}", e);
		}
	}

	public static void updateWorker(Connection conn, Job job) {
		String sql = "UPDATE gestione_cantieri.building_worker SET worker_id = ?, dateOfWork = ?, hoursOfWork = ? "
				+ "WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, job.getWorkerId());
			pstm.setDate(2, job.getDateOfWork());
			pstm.setInt(3, job.getHoursOfWork());
			pstm.setInt(4, job.getId());

			String query = pstm.toString();
			logger.info("updateWorker: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method updateWorker: {}", e);
		}
	}

	public static Map<String, Integer> getWorkerHours(Connection conn, int id) {
		String sql = "SELECT b.name, SUM(hoursOfWork) AS hoursOfWork FROM gestione_cantieri.building_worker bw "
				+ "INNER JOIN gestione_cantieri.building b ON bw.building_id = b.id WHERE bw.worker_id = ? "
				+ "GROUP BY bw.building_id";

		Map<String, Integer> buildingHours = new HashMap<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("getWorkerHours: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					String buildingName = rs.getString("b.name");
					int hoursOfWork = rs.getInt("hoursOfWork");
					buildingHours.put(buildingName, hoursOfWork);
				}
			} catch (SQLException e) {
				logger.error("Error in method getWorkerHours: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method getWorkerHours: {}", e);
		}
		return buildingHours;
	}
}
