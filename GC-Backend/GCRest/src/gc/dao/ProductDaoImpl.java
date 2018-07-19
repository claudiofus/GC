package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.model.Product;
import gc.utils.DBUtils;

public class ProductDaoImpl {

	public List<Product> getProducts() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Product> productData = new ArrayList<>();

		try {
			productData = DBUtils.queryProduct(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productData;
	}

	public List<Product> getProductsPrices() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Product> productData = new ArrayList<>();

		try {
			productData = DBUtils.queryProductPrice(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productData;
	}

	public List<HashMap<String, Object>> getProductDetails(String prdName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<HashMap<String, Object>> productData = null;

		try {
			productData = DBUtils.queryPricesHistory(connection, prdName);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productData;
	}

	public Product insertProduct(String code, String name, String providerCode) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		Product productData = new Product(code, name, providerCode);

		try {
			DBUtils.insertProduct(connection, productData);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productData;
	}
}