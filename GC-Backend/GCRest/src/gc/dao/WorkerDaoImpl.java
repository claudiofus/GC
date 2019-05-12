package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBContractTerms;
import gc.db.DBSalary;
import gc.db.DBWorker;
import gc.model.Worker;
import gc.model.types.Contacts;
import gc.model.types.job.Job;

public class WorkerDaoImpl implements Dao<Worker> {
	private static final Logger logger = LogManager.getLogger(WorkerDaoImpl.class.getName());

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
			DBContractTerms.insertContractTerms(conn, worker);
			DBSalary.insertSalary(conn, worker);
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

	public Worker updateWorker(Worker worker, int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Worker updWorker = null;

		try {
			updWorker = new Worker();
			updWorker.setId(id);
			updWorker.setName(worker.getName());
			updWorker.setSurname(worker.getSurname());
			updWorker.setGender(worker.getGender());
			updWorker.setFiscalCode(worker.getFiscalCode());
			updWorker.setBirthDate(worker.getBirthDate());
			updWorker.setBirthPlace(worker.getBirthPlace());
			updWorker.setBirthProvPlace(worker.getBirthProvPlace());
			updWorker.setMarried(worker.isMarried());
			if (worker.getContacts() != null) {
				Contacts contacts = new Contacts();
				contacts.setTelephone(worker.getContacts().getTelephone());
				contacts.setEmail(worker.getContacts().getEmail());
				updWorker.setContacts(contacts);
			}

			DBWorker.updateWorker(conn, updWorker);
			conn.commit();
			DBContractTerms.updateContractTerms(conn, worker);
			DBSalary.updateSalary(conn, worker);
			updWorker = DBWorker.selectWorker(conn, updWorker.getId());
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

	public Worker getWorkersById(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Worker worker = new Worker();

		try {
			worker = DBWorker.selectWorker(conn, id);
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

		return worker;
	}
	
	public Map<?,?> getWorkerHours(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Map<?,?> workerHours = null;

		try {
			workerHours = DBWorker.getWorkerHours(conn, id);
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

		return workerHours;
	}

	public void assignWorker(Job job, int building_id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBWorker.assignToBuilding(conn, job, building_id);
			conn.commit();
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
	}

	public void updateWorker(Job job) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBWorker.updateWorker(conn, job);
			conn.commit();
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
	}

	@Override
	public Optional<Worker> get(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Worker worker = new Worker();

		try {
			worker = DBWorker.selectWorker(conn, id);
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

		return Optional.ofNullable(worker);
	}

	@Override
	public List<Worker> getAll() {
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

	@Override
	public void save(Worker t) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Worker t, String[] params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(Worker t) {
		// TODO Auto-generated method stub
	}
}