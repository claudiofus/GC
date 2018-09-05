package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.model.Event;
import gc.utils.DBUtils;

public class EventDaoImpl {

	public List<Event> getEvents() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Event> eventData = new ArrayList<>();

		try {
			eventData = DBUtils.queryEvent(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return eventData;
	}

	public Event insertEvent(Event ev) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			int id = DBUtils.insertEvent(conn, ev);
			ev.setId(id);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return ev;
	}

	public Event updateEvent(Event ev) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Event updEv = null;

		try {
			updEv = ev.getClass().getConstructor().newInstance();
			updEv.setId(ev.getId());
			updEv.setTitle(ev.getTitle());
			updEv.setStart_date(ev.getStart_date());
			updEv.setPaid(ev.isPaid());

			DBUtils.updateEvent(conn, updEv);
			conn.commit();
			updEv = DBUtils.selectEvent(conn, updEv);
			conn.commit();
		} catch (Exception e) {
			System.err.println("Error during update event");
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}

		return updEv;
	}

	public Event deleteEvent(Event ev) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			if (ev != null) {
				DBUtils.deleteEvent(conn, ev);
				conn.commit();
			}
		} catch (Exception e) {
			System.err.println("Error during delete event");
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}

		return ev;
	}
}