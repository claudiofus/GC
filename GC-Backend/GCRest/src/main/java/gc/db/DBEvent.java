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

import gc.model.Event;
import gc.utils.Utils;

public class DBEvent {
	private static final Logger logger = LogManager.getLogger(DBEvent.class.getName());

	private DBEvent() {
		throw new IllegalStateException("DBEvent class");
	}
	
	public static List<Event> queryEvent(Connection conn) {
		String sql = "SELECT id, title, start_date, paid, payment_date FROM gestione_cantieri.event ORDER BY start_date ASC";

		List<Event> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			
			String query = pstm.toString();
			logger.info("queryEvent: {}", query);

			list = getEventList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method queryEvent: {}", e);
		}

		return list;
	}

	public static Event getEvent(Connection conn, int evID) {
		String sql = "SELECT id, title, start_date, paid, payment_date FROM gestione_cantieri.event WHERE id = ?";

		Event ev = null;
		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, evID);
			
			String query = pstm.toString();
			logger.info("getEvent: {}", query);

			List<Event> eventList = getEventList(pstm);
			if (!eventList.isEmpty()) {
				ev = eventList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method getEvent: {}", e);
		}

		return ev;
	}

	public static int insertEvent(Connection conn, Event eventData) {
		String sql = "INSERT INTO gestione_cantieri.event (title, start_date, paid, payment_date) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setString(1, eventData.getTitle());
			pstm.setDate(2, eventData.getStartDate());
			pstm.setBoolean(3, eventData.isPaid());
			pstm.setDate(4, eventData.getPaymentDate());

			String query = pstm.toString();
			logger.info("insertEvent: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting event failed, no rows affected.");
			}

			int id = Utils.getIdFromQuery(pstm);
			if (id != 0) {
				eventData.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Error in method insertEvent: {}", e);
		}
		return eventData.getId();
	}

	public static void updateEvent(Connection conn, Event ev) {
		String sql = "UPDATE gestione_cantieri.event SET title = ?, start_date = ?, paid = ?, payment_date = ? WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, ev.getTitle());
			pstm.setDate(2, ev.getStartDate());
			pstm.setBoolean(3, ev.isPaid());
			pstm.setDate(4, ev.getPaymentDate());
			pstm.setInt(5, ev.getId());

			String query = pstm.toString();
			logger.info("updateEvent: {}", query);

			pstm.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error in method updateEvent: {}", e);
		}
	}

	public static boolean deleteEvent(Connection conn, int id) {
		String sql = "DELETE FROM gestione_cantieri.event WHERE id = ?";

		try (PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setInt(1, id);
			
			String query = pstm.toString();
			logger.info("deleteEvent: {}", query);

			int result = pstm.executeUpdate();
			if (result != 0) {
				logger.info("Event with id = {} deleted", id);
				return true;
			} else {
				logger.info("No event was deleted with id = {}", id);
				return false;
			}
		} catch (SQLException e) {
			logger.error("Error in method deleteEvent: {}", e);
			return false;
		}
	}
	
	private static List<Event> getEventList(PreparedStatement pstm) {
		List<Event> eventList = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			Event ev = null;
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String title = rs.getString("title");
				java.sql.Date startDate = rs.getDate("start_date");
				boolean paid = rs.getBoolean("paid");
				java.sql.Date paymentDate = rs.getDate("payment_date");
				ev = new Event(title);
				ev.setId(id);
				ev.setStartDate(startDate);
				ev.setPaid(paid);
				ev.setPaymentDate(paymentDate);
				eventList.add(ev);
			}
		} catch (SQLException e) {
			logger.error("Error in method getEventList: {}", e);
		}

		return eventList;
	}
}
