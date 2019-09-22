package gc.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBStats {
	private static final Logger logger = LogManager.getLogger(DBStats.class.getName());

	public static Map<Integer, BigDecimal> getTotOrders(Connection conn) {
		String sql = "SELECT bui.id, SUM(ord.iva_price) AS TOT FROM gestione_cantieri.order ord "
				+ "INNER JOIN gestione_cantieri.building bui ON ord.building_id = bui.id "
				+ "GROUP BY ord.building_id;";

		Map<Integer, BigDecimal> idTotOrders = new HashMap<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			String query = pstm.toString();
			logger.info("getTotOrders: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Integer id = rs.getInt("id");
					BigDecimal totOrders = rs.getBigDecimal("tot");
					idTotOrders.put(id, totOrders);
				}
			} catch (SQLException e) {
				logger.error("Error in method getTotOrders: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method getTotOrders: {}", e);
		}

		return idTotOrders;
	}
	
	public static Map<Integer, BigDecimal> getTotWorkers(Connection conn) {
		String sql = "SELECT building_id AS id, SUM(worker_tot) AS worker_tot FROM "
				+ "(SELECT bw.building_id, s.salaryForHour * SUM(bw.hoursOfWork) AS worker_tot "
				+ "FROM gestione_cantieri.building_worker bw INNER JOIN gestione_cantieri.salary s "
				+ "ON s.worker_id = bw.worker_id GROUP BY bw.building_id , bw.worker_id) temp "
				+ "GROUP BY building_id;";

		Map<Integer, BigDecimal> idTotWorkers = new HashMap<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			String query = pstm.toString();
			logger.info("getTotWorkers: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Integer id = rs.getInt("id");
					BigDecimal totOrders = rs.getBigDecimal("worker_tot");
					idTotWorkers.put(id, totOrders);
				}
			} catch (SQLException e) {
				logger.error("Error in method getTotWorkers: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method getTotWorkers: {}", e);
		}

		return idTotWorkers;
	}

	public static Map<Integer, Integer> getLast30WorkingHours(Connection conn) {
		String sql = "SELECT worker_id AS id, SUM(hoursOfWork) AS working_hours "
				+ "FROM gestione_cantieri.building_worker bw "
				+ "WHERE bw.dateOfWork BETWEEN NOW() - INTERVAL 30 DAY AND NOW() GROUP BY bw.worker_id;";
		Map<Integer, Integer> idWorkingHours = new HashMap<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			String query = pstm.toString();
			logger.info("getWorkingHours: {}", query);

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					Integer id = rs.getInt("id");
					Integer workingHours = rs.getInt("working_hours");
					idWorkingHours.put(id, workingHours);
				}
			} catch (SQLException e) {
				logger.error("Error in method getWorkingHours: {}", e);
			}
		} catch (SQLException e) {
			logger.error("Error in method getWorkingHours: {}", e);
		}

		return idWorkingHours;
	}
}
