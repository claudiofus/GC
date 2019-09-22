package gc.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBContractTerms;
import gc.db.DBSalary;
import gc.db.DBWorker;
import gc.model.Worker;
import gc.model.types.Contacts;
import gc.model.types.job.Job;
import gc.utils.Constants;

public class WorkerDaoImpl implements Dao<Worker> {
	private static final Logger logger = LogManager.getLogger(WorkerDaoImpl.class.getName());

	@Override
	public Worker get(int id) {
		Worker worker = DBWorker.selectWorker(conn, id);
		return worker;
	}

	@Override
	public List<Worker> getAll() {
		List<Worker> workerData = DBWorker.queryWorker(conn);
		return workerData;
	}

	@Override
	public void save(Worker worker) {
		try {
			DBWorker.insertWorker(conn, worker);
			conn.commit();
			DBContractTerms.insertContractTerms(conn, worker);
			DBSalary.insertSalary(conn, worker);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertWorker: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}

	@Override
	public void update(Worker worker, String[] params) {
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
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}

	@Override
	public void delete(Worker t) {
		// Use this for each DAO
	}
	
	public Map<String, Integer> getWorkerHours(int id) {
		Map<String, Integer> workerHours = DBWorker.getWorkerHours(conn, id);
		return workerHours;
	}

	public void assignWorker(Job job, int buildingId) {
		try {
			DBWorker.assignToBuilding(conn, job, buildingId);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getProducts: {}", e);
		}
	}

	public void updateWorker(Job job) {
		try {
			DBWorker.updateWorker(conn, job);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getProducts: {}", e);
		}
	}
}