package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gc.model.Event;

public class DBEvent {

	public static List<Event> queryEvent(Connection conn) throws SQLException {
		String sql = "SELECT id, title, start_date, paid FROM gestione_cantieri.event ORDER BY start_date ASC";

		PreparedStatement pstm = conn.prepareStatement(sql);
		System.out.println("queryEvent: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Event> list = new ArrayList<Event>();
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String title = rs.getString("title");
			java.sql.Date start_date = rs.getDate("start_date");
			boolean paid = rs.getBoolean("paid");

			Event ev = new Event(title);
			ev.setId(id);
			ev.setStart_date(start_date);
			ev.setPaid(paid);
			list.add(ev);
		}

		pstm.close();
		return list;
	}

	public static Event getEvent(Connection conn, int evID)
			throws SQLException {
		String sql = "SELECT id, title, start_date, paid FROM gestione_cantieri.event WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, evID);
		System.out.println("getEvent: " + pstm.toString());

		Event ev = null;
		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String title = rs.getString("title");
			java.sql.Date start_date = rs.getDate("start_date");
			boolean paid = rs.getBoolean("paid");
			ev = new Event(title);
			ev.setId(id);
			ev.setStart_date(start_date);
			ev.setPaid(paid);
		}

		pstm.close();
		return ev;
	}

	public static int insertEvent(Connection conn, Event eventData)
			throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.event (title, start_date, paid) VALUES (?, ?, ?)";
		PreparedStatement pstm = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, eventData.getTitle());
		pstm.setDate(2, eventData.getStart_date());
		pstm.setBoolean(3, eventData.isPaid());

		System.out.println("insertEvent: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException("Inserting event failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				eventData.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Inserting event failed, no ID obtained.");
			}
		}
		pstm.close();
		return eventData.getId();
	}

	public static void updateEvent(Connection conn, Event ev)
			throws SQLException {
		String sql = "UPDATE gestione_cantieri.event SET title = ?, start_date = ?, paid = ? WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, ev.getTitle());
		pstm.setDate(2, ev.getStart_date());
		pstm.setBoolean(3, ev.isPaid());
		pstm.setInt(4, ev.getId());

		System.out.println("updateEvent: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static boolean deleteEvent(Connection conn, int id)
			throws SQLException {
		String sql = "DELETE FROM gestione_cantieri.event WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);
		System.out.println("deleteEvent: " + pstm.toString());

		int result = pstm.executeUpdate();
		pstm.close();
		if (result != 0) {
			System.out.println("Event with id = " + id + " deleted");
			return true;
		} else {
			System.out.println("No event was deleted with id = " + id);
			return false;
		}
	}

	public static Event selectEvent(Connection conn, int id) throws Exception {
		String sql = "SELECT id, title, start_date, paid FROM gestione_cantieri.event WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, id);
		System.out.println("selectEvent: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Event event = null;
		while (rs.next()) {
			event = new Event(rs.getString("title"));
			event.setId(rs.getInt("id"));
			event.setStart_date(rs.getDate("start_date"));
			event.setPaid(rs.getBoolean("paid"));
		}
		pstm.close();
		return event;
	}
}
