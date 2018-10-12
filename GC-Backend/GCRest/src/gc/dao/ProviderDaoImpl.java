package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.db.DBProvider;
import gc.model.Provider;

public class ProviderDaoImpl {

	public List<Provider> getProviders() {
		List<Provider> productData = new ArrayList<>();
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();

		try {
			productData = DBProvider.queryProvider(connection);
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}

		return productData;
	}
}