package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBEvent;
import gc.model.Event;
import gc.utils.Constants;

public class EventDaoImpl {
	private static final Logger logger = LogManager.getLogger(EventDaoImpl.class.getName());

	public List<Event> getEvents() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Event> eventData = DBEvent.queryEvent(conn);
		jdbcConnection.closeConnection(conn);

		return eventData;
	}

	public Event getEventByID(int eventID) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Event eventData = DBEvent.getEvent(conn, eventID);
		jdbcConnection.closeConnection(conn);

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
			logger.error("Error in method insertEvent: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return ev;
	}

	public Event updateEvent(Event ev) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Event updEv = null;

		try {
			updEv = new Event();
			updEv.setId(ev.getId());
			updEv.setTitle(ev.getTitle());
			updEv.setStartDate(ev.getStartDate());
			updEv.setPaymentDate(ev.getPaymentDate());
			updEv.setPaid(ev.isPaid());

			DBEvent.updateEvent(conn, updEv);
			conn.commit();
			updEv = DBEvent.getEvent(conn, updEv.getId());
			conn.commit();
		} catch (Exception e) {
			logger.error("Error during update event", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
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
			logger.error("Error during delete event with id: " + id, e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return true;
	}
}