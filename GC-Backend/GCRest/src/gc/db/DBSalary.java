package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Worker;
import gc.model.types.job.Salary;

public class DBSalary {
	private static final Logger logger = LogManager.getLogger(DBSalary.class.getName());

	public static int insertSalary(Connection conn, Worker workerData) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.salary (worker_id, salaryForHour, salaryForDay) "
				+ "VALUES (?, ?, ?)";

		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		Salary salary = workerData.getSalary();
		pstm.setInt(1, workerData.getId());
		pstm.setBigDecimal(2, salary.getSalaryForHour());
		pstm.setBigDecimal(3, salary.getSalaryForDay());

		logger.info("insertSalary: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting salary failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				salary.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Inserting salary failed, no ID obtained.");
			}
		}
		pstm.close();
		return salary.getId();
	}

	public static void updateSalary(Connection conn, Worker worker) throws SQLException {
		String sql = "UPDATE gestione_cantieri.salary SET salaryForHour = ?, salaryForDay = ? WHERE worker_id = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		Salary salary = worker.getSalary();
		pstm.setBigDecimal(1, salary.getSalaryForHour());
		pstm.setBigDecimal(2, salary.getSalaryForDay());
		pstm.setInt(3, worker.getId());

		logger.info("updateSalary: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();

	}
}
