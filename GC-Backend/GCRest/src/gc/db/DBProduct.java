package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Product;

public class DBProduct {
	private static final Logger logger = LogManager.getLogger(DBProduct.class.getName());

	public static void insertProduct(Connection conn, Product product) throws SQLException {
		String sql = "INSERT INTO product(name, provider_name) VALUES (?,?)";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, product.getName());
		pstm.setString(2, product.getProviderName());
		logger.info("insertProduct: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static List<Product> queryProduct(Connection conn) throws SQLException {
		String sql = "SELECT name, provider_name FROM gestione_cantieri.product";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryProduct: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			String name = rs.getString("name");
			String providerName = rs.getString("provider_name");
			Product product = new Product(name, providerName);
			list.add(product);
		}

		pstm.close();
		return list;
	}

	public static Product findProduct(Connection conn, Product prd) throws SQLException {
		String sql = "SELECT name, provider_name FROM gestione_cantieri.product WHERE name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, prd.getName());
		logger.info("findProduct: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Product product = null;
		while (rs.next()) {
			String name = rs.getString("Name");
			String providerName = rs.getString("provider_name");
			product = new Product(name, providerName);
		}

		pstm.close();
		return product;
	}
}
