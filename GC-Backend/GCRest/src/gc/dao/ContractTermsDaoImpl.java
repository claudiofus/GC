package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBContractTerms;
import gc.model.Worker;
import gc.model.types.job.ContractTerms;

public class ContractTermsDaoImpl {
	private static final Logger logger = LogManager.getLogger(ContractTermsDaoImpl.class.getName());

	public ContractTerms insertContractTerms(Worker worker) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBContractTerms.insertContractTerms(conn, worker);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertWorker: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return worker.getContractTerms();
	}
}
