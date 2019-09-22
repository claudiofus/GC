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
import gc.utils.Utils;

public class DBProvider {
	private static final Logger logger = LogManager.getLogger(DBProvider.class.getName());

	private DBProvider() {
		throw new IllegalStateException("DBProvider class");
	}

	public static List<Provider> queryProvider(Connection conn) {
		String sql = "SELECT id, name FROM gestione_cantieri.provider ORDER BY name";

		List<Provider> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {

			String query = pstm.toString();
			logger.info("queryProvider: {}", query);

			list = getProviderList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method queryProvider: {}", e);
		}

		return list;
	}

	public static Provider findProvider(Connection conn, String nameToFind) {
		String sql = "SELECT id, name FROM gestione_cantieri.provider WHERE name = ?";

		Provider provider = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, nameToFind);

			String query = pstm.toString();
			logger.info("findProvider: {}", query);

			List<Provider> providerList = getProviderList(pstm);
			if (!providerList.isEmpty()) {
				provider = providerList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method findProvider: {}", e);
		}
		return provider;
	}

	public static Provider findProviderById(Connection conn, int idToFind) {
		String sql = "SELECT id, name FROM gestione_cantieri.provider WHERE id = ?";

		Provider provider = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, idToFind);

			String query = pstm.toString();
			logger.info("findProviderById: {}", query);

			List<Provider> providerList = getProviderList(pstm);
			if (!providerList.isEmpty()) {
				provider = providerList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method findProviderById: {}", e);
		}
		return provider;
	}

	public static int insertProvider(Connection conn, Provider provider) throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.provider (name) VALUES (?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setString(1, provider.getName());

			String query = pstm.toString();
			logger.info("insertProvider: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting provider failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				provider.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertProvider: {}", e);
		}

		return provider.getId();
	}

	private static List<Provider> getProviderList(PreparedStatement pstm) {
		List<Provider> providerList = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				Provider provider = new Provider(id, name);
				providerList.add(provider);
			}
		} catch (SQLException e) {
			logger.error("Error in method getProviderList: {}", e);
		}

		return providerList;
	}
}
