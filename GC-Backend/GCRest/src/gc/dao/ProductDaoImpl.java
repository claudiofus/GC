package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBOrder;
import gc.db.DBProduct;
import gc.model.Product;

public class ProductDaoImpl {
	private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class.getName());

	public List<Product> getProducts() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Product> productData = new ArrayList<>();

		try {
			productData = DBProduct.queryProduct(connection);
		} catch (SQLException e) {
			logger.error("Error in method getProducts: ", e);
		}

		return productData;
	}

	public List<Product> getProductsPrices() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<Product> productData = new ArrayList<>();

		try {
			productData = DBOrder.queryProductPrice(connection);
		} catch (SQLException e) {
			logger.error("Error in method getProductsPrices: ", e);
		}

		return productData;
	}

	public List<HashMap<String, Object>> getProductDetails(String prdName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		List<HashMap<String, Object>> productData = null;

		try {
			productData = DBOrder.queryPricesHistory(connection, prdName);
		} catch (SQLException e) {
			logger.error("Error in method getProductDetails: ", e);
		}

		return productData;
	}

	public Product insertProduct(String code, String name, String providerCode) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection connection = jdbcConnection.getConnnection();
		Product productData = new Product(code, name, providerCode);

		try {
			DBProduct.insertProduct(connection, productData);
			connection.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertProduct: ", e);
		}

		return productData;
	}
}