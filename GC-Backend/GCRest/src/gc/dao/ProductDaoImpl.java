package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
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
		Connection conn = jdbcConnection.getConnnection();
		List<Product> productData = DBProduct.queryProduct(conn);
		jdbcConnection.closeConnection(conn);

		return productData;
	}

	public List<Product> getProductsPrices() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Product> productData = DBOrder.queryProductPrice(conn);
		jdbcConnection.closeConnection(conn);

		return productData;
	}

	public List<HashMap<String, Object>> getProductDetails(String prdName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<HashMap<String, Object>> productData = DBOrder.queryPricesHistory(conn, prdName);
		jdbcConnection.closeConnection(conn);

		return productData;
	}

	public Product insertProduct(String name, String providerName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Product productData = new Product();
		productData.setName(name);
		productData.setProviderName(providerName);

		try {
			DBProduct.insertProduct(conn, productData);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertProduct: {}", e);
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return productData;
	}
}