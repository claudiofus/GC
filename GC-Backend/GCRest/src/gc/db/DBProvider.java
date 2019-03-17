package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.Provider;

public class DBProvider {
	private static final Logger logger = LogManager.getLogger(DBProvider.class.getName());

	public static List<Provider> queryProvider(Connection conn) throws SQLException {
		String sql = "SELECT id, name FROM gestione_cantieri.provider ORDER BY name";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryProvider: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Provider> list = new ArrayList<Provider>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			Provider provider = new Provider(id, name);
			list.add(provider);
		}
		pstm.close();
		return list;
	}

	public static Provider findProvider(Connection connection, String nameToFind) throws SQLException {
		String sql = "SELECT id, name FROM gestione_cantieri.provider WHERE name = ?";

		PreparedStatement pstm = connection.prepareStatement(sql);
		pstm.setString(1, nameToFind);
		logger.info("findProvider: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Provider provider = null;
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			provider = new Provider(id, name);
		}
		pstm.close();
		return provider;
	}

	public static int insertProvider(Connection conn, Provider provider) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.provider (name) VALUES (?)";

		PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, provider.getName());

		logger.info("insertProvider: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting provider failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				provider.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Inserting provider failed, no ID obtained.");
			}
		}
		pstm.close();
		return provider.getId();
	}
}
