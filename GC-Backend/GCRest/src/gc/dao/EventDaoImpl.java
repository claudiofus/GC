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
			DBUtils.insertEvent(conn, ev);
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
}