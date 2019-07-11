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

	private DBProduct() {
		throw new IllegalStateException("DBProduct class");
	}

	public static void insertProduct(Connection conn, Product product) {
		String sql = "INSERT INTO product(name, provider_name) VALUES (?,?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, product.getName());
			pstm.setString(2, product.getProviderName());

			String query = pstm.toString();
			logger.info("insertProduct: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method insertProduct: {}", e);
		}
	}

	public static List<Product> queryProduct(Connection conn) {
		String sql = "SELECT name, provider_name FROM gestione_cantieri.product";

		List<Product> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {

			String query = pstm.toString();
			logger.info("queryProduct: {}", query);

			list = getProductList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method queryProduct: {}", e);
		}
		return list;
	}

	public static Product findProduct(Connection conn, Product prd) {
		String sql = "SELECT name, provider_name FROM gestione_cantieri.product WHERE name = ?";

		Product product = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, prd.getName());

			String query = pstm.toString();
			logger.info("findProduct: {}", query);

			List<Product> prdList = getProductList(pstm);
			if (!prdList.isEmpty()) {
				product = prdList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method findProduct: {}", e);
		}
		return product;
	}

	private static List<Product> getProductList(PreparedStatement pstm) {
		List<Product> productList = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			Product prd = null;
			while (rs.next()) {
				String name = rs.getString("name");
				String providerName = rs.getString("provider_name");
				prd = new Product(name, providerName);
				productList.add(prd);
			}
		} catch (SQLException e) {
			logger.error("Error in method getProductList: {}", e);
		}

		return productList;
	}
}
