package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBProvider;
import gc.model.Provider;

public class ProviderDaoImpl {
	private static final Logger logger = LogManager
			.getLogger(ProviderDaoImpl.class.getName());

	public Provider insertProvider(Provider provider) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBProvider.insertProvider(conn, provider);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertProvider: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e1) {
						logger.error("Error in closing connection: ", e1);
					}
				}
			}
		}

		return provider;
	}

	public List<Provider> getProviders() {
		List<Provider> productData = new ArrayList<>();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			productData = DBProvider.queryProvider(conn);
		} catch (SQLException e) {
			logger.error("Error in method getProviders: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return productData;
	}

	public Provider getProviderDetails(String name) {
		Provider productData = new Provider();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			productData = DBProvider.findProvider(conn, name);
		} catch (SQLException e) {
			logger.error("Error in method getProviderDetails: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return productData;
	}
}