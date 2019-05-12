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

public class DBWorker {
	private static final Logger logger = LogManager.getLogger(DBWorker.class.getName());

	public static int insertWorker(Connection conn, Worker workerData) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.worker (name, surname, gender, fiscalCode, birthDate, "
				+ "birthPlace, birthProvPlace, married, telephone, email) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

		logger.info("insertWorker: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting worker failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				workerData.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Inserting worker failed, no ID obtained.");
			}
		}
		pstm.close();
		return workerData.getId();
	}

	public static List<Worker> queryWorker(Connection conn) throws SQLException {
		String sql = "SELECT id, name, surname, gender, fiscalCode, birthDate, birthPlace, birthProvPlace, "
				+ "married, telephone, email FROM gestione_cantieri.worker";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryWorker: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Worker> list = new ArrayList<Worker>();
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

		pstm.close();
		return list;
	}

	public static void updateWorker(Connection conn, Worker worker) throws SQLException {
		String sql = "UPDATE gestione_cantieri.worker "
				+ "SET name = ?, surname = ?, gender = ?, fiscalCode = ?, birthDate = ?, "
				+ "birthPlace = ?, birthProvPlace = ?, married = ?, telephone = ?, email = ? " + "WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
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

		logger.info("updateWorker: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static Worker selectWorker(Connection conn, int id) throws SQLException {
		String sql = "SELECT w.id, name, w.surname, w.gender, w.fiscalCode, w.birthDate, w.birthPlace, w.birthProvPlace, "
				+ "w.married, w.telephone, w.email, c.id, c.recruitmentDate, c.dismissalDate, c.type, c.qualification, "
				+ "s.id, s.salaryForHour, s.salaryForDay FROM gestione_cantieri.worker w "
				+ "INNER JOIN gestione_cantieri.contract c ON w.id = c.worker_id "
				+ "INNER JOIN gestione_cantieri.salary s ON w.id = s.worker_id WHERE w.id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);
		logger.info("selectWorker: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Worker newWorker = null;
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
		pstm.close();
		return newWorker;
	}

	public static void assignToBuilding(Connection conn, Job job, int id) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.building_worker (worker_id, building_id, dateOfWork, hoursOfWork) "
				+ "VALUES (?, ?, ?, ?)";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, job.getWorker_id());
		pstm.setInt(2, id);
		pstm.setDate(3, job.getDateOfWork());
		pstm.setInt(4, job.getHoursOfWork());

		logger.info("assignToBuilding: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static void updateWorker(Connection conn, Job job) throws SQLException {
		String sql = "UPDATE gestione_cantieri.building_worker SET worker_id = ?, dateOfWork = ?, hoursOfWork = ? "
				+ "WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, job.getWorker_id());
		pstm.setDate(2, job.getDateOfWork());
		pstm.setInt(3, job.getHoursOfWork());
		pstm.setInt(4, job.getId());

		logger.info("updateWorker: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static Map<?, ?> getWorkerHours(Connection conn, int id) throws SQLException {
		String sql = "SELECT b.name, SUM(hoursOfWork) AS hoursOfWork FROM gestione_cantieri.building_worker bw "
				+ "INNER JOIN gestione_cantieri.building b ON bw.building_id = b.id WHERE bw.worker_id = ? "
				+ "GROUP BY bw.building_id";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);
		logger.info("getWorkerHours: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Map<String, Integer> building_hours = new HashMap<>();
		while (rs.next()) {
			String building_name = rs.getString("b.name");
			int hoursOfWork = rs.getInt("hoursOfWork");
			building_hours.put(building_name, hoursOfWork);
		}

		pstm.close();
		return building_hours;
	}
}
