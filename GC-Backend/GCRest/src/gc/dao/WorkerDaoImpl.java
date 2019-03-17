package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBWorker;
import gc.einvoice.ContattiType;
import gc.model.Worker;

public class WorkerDaoImpl {
	private static final Logger logger = LogManager
			.getLogger(WorkerDaoImpl.class.getName());

	public List<Worker> getWorkers() {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Worker> workerData = new ArrayList<>();

		try {
			workerData = DBWorker.queryWorker(conn);
		} catch (SQLException e) {
			logger.error("Error in method getProducts: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return workerData;
	}

	public Worker insertWorker(Worker worker) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBWorker.insertWorker(conn, worker);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertWorker: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return worker;
	}

	public Worker updateWorker(Worker worker) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Worker updWorker = null;

		try {
			updWorker = new Worker();
			updWorker.setId(worker.getId());
			updWorker.setName(worker.getName());
			updWorker.setSurname(worker.getSurname());
			updWorker.setGender(worker.getGender());
			updWorker.setFiscalCode(worker.getFiscalCode());
			updWorker.setBirthDate(worker.getBirthDate());
			updWorker.setBirthPlace(worker.getBirthPlace());
			updWorker.setBirthProvPlace(worker.getBirthProvPlace());
			updWorker.setMarried(worker.isMarried());
			if (worker.getContacts() != null) {
				ContattiType contacts = new ContattiType();
				contacts.setTelefono(worker.getContacts().getTelefono());
				contacts.setEmail(worker.getContacts().getEmail());
				updWorker.setContacts(contacts);
			}

			DBWorker.updateWorker(conn, updWorker);
			conn.commit();
			updWorker = DBWorker.selectWorker(conn, updWorker);
			conn.commit();
		} catch (Exception e) {
			logger.error("Error during update order", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return updWorker;
	}
}