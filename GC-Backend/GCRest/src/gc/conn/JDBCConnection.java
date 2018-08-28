package gc.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	public Connection getConnnection() {
		String hostName = "localhost";
		String dbName = "gestione_cantieri";
		String userName = "root";
		String password = "12345";
		Connection connection = null;

		try {
			String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?serverTimezone=UTC";
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, userName, password);
			connection.setAutoCommit(false);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			System.err.println("Error during connection to DB, check connection.");
			e.printStackTrace();
		}

		return connection;
	}

}