package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.model.Product;

public class DBProduct {

	public static void insertProduct(Connection conn, Product product)
			throws SQLException {
		String sql = "INSERT INTO product(id, name, provider_code) VALUES (?,?,?)";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, product.getCode());
		pstm.setString(2, product.getName());
		pstm.setString(3, product.getProviderCode());
		System.out.println("insertProduct: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static List<Product> queryProduct(Connection conn)
			throws SQLException {
		String sql = "SELECT id, name, provider_code FROM gestione_cantieri.product";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryProduct: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			String code = rs.getString("id");
			String name = rs.getString("name");
			String providerCode = rs.getString("provider_code");
			Product product = new Product(code, name, providerCode);
			list.add(product);
		}

		pstm.close();
		return list;
	}

	public static Product findProduct(Connection conn, Product prd)
			throws SQLException {
		String sql = "SELECT id, name, provider_code FROM gestione_cantieri.product WHERE id = ? AND name = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, prd.getCode());
		pstm.setString(2, prd.getName());
		System.out.println("findProduct: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Product product = null;
		while (rs.next()) {
			String name = rs.getString("Name");
			String providerCode = rs.getString("provider_code");
			product = new Product(prd.getCode(), name, providerCode);
		}

		pstm.close();
		return product;
	}
}
