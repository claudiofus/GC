package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gc.conn.JDBCConnection;
import gc.db.DBEvent;
import gc.model.Event;

public class EventDaoImpl {

	public List<Event> getEvents() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Event> eventData = new ArrayList<>();

		try {
			eventData = DBEvent.queryEvent(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return eventData;
	}

	public Event getEventByID(int eventID) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Event eventData = null;

		try {
			eventData = DBEvent.getEvent(conn, eventID);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return eventData;
	}

	public Event insertEvent(Event ev) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			int id = DBEvent.insertEvent(conn, ev);
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

			DBEvent.updateEvent(conn, updEv);
			conn.commit();
			updEv = DBEvent.selectEvent(conn, updEv.getId());
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

	public boolean deleteEvent(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBEvent.deleteEvent(conn, id);
			conn.commit();
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

		return true;
	}
}