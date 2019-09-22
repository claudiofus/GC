package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Worker;
import gc.model.types.job.Salary;
import gc.utils.Utils;

public class DBSalary {
	private static final Logger logger = LogManager.getLogger(DBSalary.class.getName());

	private DBSalary() {
		throw new IllegalStateException("DBSalary class");
	}
	
	public static int insertSalary(Connection conn, Worker workerData) {
		String sql = "INSERT INTO gestione_cantieri.salary (worker_id, salaryForHour, salaryForDay) "
				+ "VALUES (?, ?, ?)";

		Salary salary = workerData.getSalary();
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setInt(1, workerData.getId());
			pstm.setBigDecimal(2, salary.getSalaryForHour());
			pstm.setBigDecimal(3, salary.getSalaryForDay());

			String query = pstm.toString();
			logger.info("insertSalary: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting salary failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				salary.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertSalary: {}", e);
		}

		return salary.getId();
	}

	public static void updateSalary(Connection conn, Worker worker) {
		String sql = "UPDATE gestione_cantieri.salary SET salaryForHour = ?, salaryForDay = ? WHERE worker_id = ?";
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			Salary salary = worker.getSalary();
			pstm.setBigDecimal(1, salary.getSalaryForHour());
			pstm.setBigDecimal(2, salary.getSalaryForDay());
			pstm.setInt(3, worker.getId());

			String query = pstm.toString();
			logger.info("updateSalary: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method updateSalary: {}", e);
		}
	}
}
