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
	private static final Logger logger = LogManager.getLogger(ProviderDaoImpl.class.getName());

	public List<Provider> getProviders() {
		List<Provider> productData = new ArrayList<>();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();

		try {
			productData = DBProvider.queryProvider(connection);
		} catch (SQLException e) {
			logger.error("Error in method getProviders: ", e);
		}

		return productData;
	}

	public Provider getProviderDetails(String code) {
		Provider productData = new Provider();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();

		try {
			productData = DBProvider.findProvider(connection, code);
		} catch (SQLException e) {
			logger.error("Error in method getProviderDetails: ", e);
		}

		return productData;
	}
}