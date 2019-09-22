package gc.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBEvent;
import gc.model.Event;
import gc.utils.Constants;

public class EventDaoImpl implements Dao<Event> {
	private static final Logger logger = LogManager.getLogger(EventDaoImpl.class.getName());

	@Override
	public Event get(int id) {
		Event eventData = DBEvent.getEvent(conn, id);
		return eventData;
	}

	@Override
	public List<Event> getAll() {
		List<Event> eventData = DBEvent.queryEvent(conn);
		return eventData;
	}

	@Override
	public void save(Event event) {
		try {
			int id = DBEvent.insertEvent(conn, event);
			event.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertEvent: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}

	@Override
	public void update(Event event, String[] params) {
		Event updEv = null;

		try {
			updEv = new Event();
			updEv.setId(event.getId());
			updEv.setTitle(event.getTitle());
			updEv.setStartDate(event.getStartDate());
			updEv.setPaymentDate(event.getPaymentDate());
			updEv.setPaid(event.isPaid());

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
		}
	}

	@Override
	public void delete(Event event) {
		try {
			DBEvent.deleteEvent(conn, event.getId());
			conn.commit();
		} catch (Exception e) {
			logger.error("Error during delete event with id: " + event.getId(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}
}