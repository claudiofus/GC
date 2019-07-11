package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBDdt;
import gc.model.DDT;
import gc.utils.Constants;

public class DDTDaoImpl {
	private static final Logger logger = LogManager.getLogger(DDTDaoImpl.class.getName());

	public List<DDT> getDDTs() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<DDT> ddtData = DBDdt.queryDDT(conn);
		jdbcConnection.closeConnection(conn);

		return ddtData;
	}

	public DDT getDDTByID(int ddtID) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		DDT ddtData = DBDdt.getDDT(conn, ddtID);
		jdbcConnection.closeConnection(conn);

		return ddtData;
	}

	public DDT insertDDT(DDT ddt) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			int id = DBDdt.insertDDT(conn, ddt);
			ddt.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertEvent: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return ddt;
	}
}