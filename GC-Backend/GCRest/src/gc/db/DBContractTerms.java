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
import gc.utils.Utils;

public class DBContractTerms {
	private static final Logger logger = LogManager.getLogger(DBContractTerms.class.getName());

	private DBContractTerms() {
		throw new IllegalStateException("DBContractTerms class");
	}

	public static int insertContractTerms(Connection conn, Worker workerData) {
		String sql = "INSERT INTO gestione_cantieri.contract (worker_id, recruitmentDate, dismissalDate, type, qualification) "
				+ "VALUES (?, ?, ?, ?, ?)";

		ContractTerms ct = new ContractTerms();
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ct = workerData.getContractTerms();
			pstm.setInt(1, workerData.getId());
			pstm.setDate(2, ct.getRecruitmentDate());
			pstm.setDate(3, ct.getDismissalDate());
			pstm.setString(4, ContractType.getEnumByString(ct.getContractType().getType()));
			pstm.setString(5, Qualification.getEnumByString(ct.getQualification().getQualification()));

			String query = pstm.toString();
			logger.info("insertContract: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting contract failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				ct.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertContractTerms: {}", e);
		}
		return ct.getId();
	}

	public static ContractTerms selectContract(Connection conn, int id) {
		String sql = "SELECT id, recruitmentDate, dismissalDate, type, qualification FROM gestione_cantieri.contract "
				+ "WHERE worker_id = ?";

		ContractTerms ct = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("selectContract: {}", query);

			ct = getContractTerms(pstm, id);
		} catch (SQLException e) {
			logger.error("Error in method selectContract: {}", e);
		}
		return ct;
	}

	public static void updateContractTerms(Connection conn, Worker worker) {
		String sql = "UPDATE gestione_cantieri.contract "
				+ "SET recruitmentDate = ?, dismissalDate = ?, type = ?, qualification = ? " + "WHERE worker_id = ?";
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			ContractTerms ct = worker.getContractTerms();
			pstm.setDate(1, ct.getRecruitmentDate());
			pstm.setDate(2, ct.getDismissalDate());
			pstm.setString(3, ct.getContractType().name());
			pstm.setString(4, ct.getQualification().name());
			pstm.setInt(5, worker.getId());

			String query = pstm.toString();
			logger.info("updateContractTerms: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method updateContractTerms: {}", e);
		}
	}

	private static ContractTerms getContractTerms(PreparedStatement pstm, int id) {
		ContractTerms ct = null;
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				ct = new ContractTerms();
				ct.setId(id);
				ct.setRecruitmentDate(rs.getDate("recruitmentDate"));
				ct.setDismissalDate(rs.getDate("dismissalDate"));
				ct.setContractType(ContractType.valueOf(rs.getString("type")));
				ct.setQualification(Qualification.valueOf(rs.getString("qualification")));
			}
		} catch (SQLException e) {
			logger.error("Error in method selectContract: {}", e);
		}

		return ct;
	}
}
