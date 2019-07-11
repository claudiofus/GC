package gc.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.DDT;
import gc.utils.Utils;

public class DBDdt {
	private static final Logger logger = LogManager.getLogger(DBDdt.class.getName());

	private DBDdt() {
		throw new IllegalStateException("DBDdt class");
	}

	public static List<DDT> queryDDT(Connection conn) {
		String sql = "SELECT id, number, provider_id, date FROM gestione_cantieri.ddt";

		List<DDT> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			String query = pstm.toString();
			logger.info("queryDDT: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Integer id = rs.getInt("id");
					String number = rs.getString("number");
					Integer providerId = rs.getInt("provider_id");
					// Date date = rs.getDate("date");

					DDT ddt = new DDT();
					ddt.setId(id);
					ddt.setNumeroDDT(number);
					ddt.setProviderId(providerId);
					// ddt.setDataDDT(date);
					list.add(ddt);
				}
			} catch (SQLException e) {
				logger.error("Error in method queryDDT: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method queryDDT: {}", e);
		}

		return list;
	}

	public static DDT getDDT(Connection conn, int ddtID) {
		String sql = "SELECT id, provider_id, number, date FROM gestione_cantieri.ddt WHERE id = ?";

		DDT ddt = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, ddtID);

			String query = pstm.toString();
			logger.info("getDDT: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Integer id = rs.getInt("id");
					Integer providerId = rs.getInt("provider_id");
					String number = rs.getString("number");
					java.sql.Date date = rs.getDate("date");
					ddt = new DDT();
					ddt.setId(id);
					ddt.setProviderId(providerId);
					ddt.setNumeroDDT(number);
					ddt.setDataDDT(Utils.asXMLGregorianCalendar(date));
				}
			} catch (SQLException e) {
				logger.error("Error in method getDDT: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method getDDT: {}", e);
		}

		return ddt;
	}

	public static int insertDDT(Connection conn, DDT ddtData) {
		String sql = "INSERT INTO gestione_cantieri.ddt (provider_id, number, date) VALUES (?, ?, ?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			Date ddtDate = Utils.fromXMLGrToSqlDate(ddtData.getDataDDT());
			pstm.setInt(1, ddtData.getProviderId());
			pstm.setString(2, ddtData.getNumeroDDT());
			pstm.setDate(3, ddtDate);

			String query = pstm.toString();
			logger.info("insertDDT: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting event failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				ddtData.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertDDT: {}", e);
		}
		return ddtData.getId();
	}
}
