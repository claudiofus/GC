package gc.conn;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCConnection {
	private static final Logger logger = LogManager.getLogger(JDBCConnection.class.getName());
	private static JDBCConnection single_instance = null;
	private Connection connection;

	private JDBCConnection() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/gestione_cantieri");

			connection = ds.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException | NamingException e) {
			logger.error("Error during connection to DB, check connection: {}", e);
		}
	}

	public static JDBCConnection getInstance() {
		if (single_instance == null)
			single_instance = new JDBCConnection();

		return single_instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}