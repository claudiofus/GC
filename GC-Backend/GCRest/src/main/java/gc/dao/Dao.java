package gc.dao;

import java.sql.Connection;
import java.util.List;

import gc.conn.JDBCConnection;

public interface Dao<T> {
	Connection conn = JDBCConnection.getInstance().getConnection();
	
	T get(int id);

	List<T> getAll();

	void save(T t);

	void update(T t, String[] params);

	void delete(T t);
}