package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBProvider;
import gc.model.Provider;
import gc.utils.Constants;

public class ProviderDaoImpl {
	private static final Logger logger = LogManager.getLogger(ProviderDaoImpl.class.getName());

	public Provider insertProvider(Provider provider) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			int id = DBProvider.insertProvider(conn, provider);
			provider.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertProvider: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			} finally {
				jdbcConnection.closeConnection(conn);
			}
		}

		return provider;
	}

	public List<Provider> getProviders() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Provider> productData = DBProvider.queryProvider(conn);
		jdbcConnection.closeConnection(conn);

		return productData;
	}

	public Provider getProviderDetails(String name) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Provider productData = DBProvider.findProvider(conn, name);
		jdbcConnection.closeConnection(conn);

		return productData;
	}

	public Provider getProviderDetails(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Provider productData = DBProvider.findProviderById(conn, id);
		jdbcConnection.closeConnection(conn);

		return productData;
	}
}