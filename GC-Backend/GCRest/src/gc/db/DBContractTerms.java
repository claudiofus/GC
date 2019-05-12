package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Worker;
import gc.model.types.job.ContractTerms;
import gc.model.types.job.ContractType;
import gc.model.types.job.Qualification;

public class DBContractTerms {
	private static final Logger logger = LogManager.getLogger(DBContractTerms.class.getName());

	public static int insertContractTerms(Connection conn, Worker workerData) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.contract (worker_id, recruitmentDate, dismissalDate, type, qualification) "
				+ "VALUES (?, ?, ?, ?, ?)";

		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ContractTerms ct = workerData.getContractTerms();
		pstm.setInt(1, workerData.getId());
		pstm.setDate(2, ct.getRecruitmentDate());
		pstm.setDate(3, ct.getDismissalDate());
		pstm.setString(4, ContractType.getEnumByString(ct.getContractType().getType()));
		pstm.setString(5, Qualification.getEnumByString(ct.getQualification().getQualification()));

		logger.info("insertContract: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting contract failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				ct.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Inserting contract failed, no ID obtained.");
			}
		}
		pstm.close();
		return ct.getId();
	}

	public static ContractTerms selectContract(Connection conn, int id) throws Exception {
		String sql = "SELECT id, recruitmentDate, dismissalDate, type, qualification FROM gestione_cantieri.contract "
				+ "WHERE worker_id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);
		logger.info("selectContract: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		ContractTerms ct = null;
		while (rs.next()) {
			ct = new ContractTerms();
			ct.setId(id);
			ct.setRecruitmentDate(rs.getDate("recruitmentDate"));
			ct.setDismissalDate(rs.getDate("dismissalDate"));
			ct.setContractType(ContractType.valueOf(rs.getString("type")));
			ct.setQualification(Qualification.valueOf(rs.getString("qualification")));
		}
		pstm.close();
		return ct;
	}

	public static void updateContractTerms(Connection conn, Worker worker) throws SQLException {
		String sql = "UPDATE gestione_cantieri.contract "
				+ "SET recruitmentDate = ?, dismissalDate = ?, type = ?, qualification = ? " + "WHERE worker_id = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		ContractTerms ct = worker.getContractTerms();
		pstm.setDate(1, ct.getRecruitmentDate());
		pstm.setDate(2, ct.getDismissalDate());
		pstm.setString(3, ct.getContractType().name());
		pstm.setString(4, ct.getQualification().name());
		pstm.setInt(5, worker.getId());

		logger.info("updateContractTerms: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}
}
