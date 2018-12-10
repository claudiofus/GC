package gc.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCConnection {
	private static final Logger logger = LogManager.getLogger(JDBCConnection.class.getName());

	public Connection getConnnection() {
		String hostName = "localhost";
		String dbName = "gestione_cantieri";
		String userName = "root";
		String password = "12345";
		Connection connection = null;

		try {
			String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, userName, password);
			connection.setAutoCommit(false);
			logger.debug("Connection with driver successful");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			logger.error("Error during connection to DB, check connection.", e);
		}

		return connection;
	}

}