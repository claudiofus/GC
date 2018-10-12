package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.model.Provider;

public class DBProvider {

	public static List<Provider> queryProvider(Connection conn)
			throws SQLException {
		String sql = "SELECT id, name, code FROM gestione_cantieri.provider ORDER BY name";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryProvider: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Provider> list = new ArrayList<Provider>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String code = rs.getString("code");
			Provider provider = new Provider(id, name, code);
			list.add(provider);
		}
		pstm.close();
		return list;
	}

	public static Provider findProvider(Connection connection,
			String codeToFind) throws SQLException {
		String sql = "SELECT id, name, code FROM gestione_cantieri.provider WHERE code = ?";

		PreparedStatement pstm = connection.prepareStatement(sql);
		pstm.setString(1, codeToFind);
		System.out.println("findProvider: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Provider provider = null;
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String code = rs.getString("code");
			provider = new Provider(id, name, code);
		}
		pstm.close();
		return provider;
	}
}
