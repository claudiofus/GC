package gc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBStats;
import gc.dto.BuildingStatDTO;
import gc.dto.WorkerStatDTO;
import gc.model.Building;
import gc.model.Worker;

public class StatsDaoImpl {
	private Connection conn = JDBCConnection.getInstance().getConnection();
	private static final Logger logger = LogManager.getLogger(StatsDaoImpl.class.getName());

	public List<BuildingStatDTO> getAll() {
		List<BuildingStatDTO> stats = new ArrayList<>();

		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		List<Building> buildings = buildingDaoImpl.getAll();
		BuildingStatDTO buildingDTO;
		Map<Integer, BigDecimal> totOrders = DBStats.getTotOrders(conn);
		Map<Integer, BigDecimal> totWorkers = DBStats.getTotWorkers(conn);
		for (Building b : buildings) {
			buildingDTO = new BuildingStatDTO();
			buildingDTO.setBuilding(b);
			buildingDTO.setOrdersTotal(totOrders.get(b.getId()));
			buildingDTO.setWorkersTotal(totWorkers.get(b.getId()));
			stats.add(buildingDTO);
		}
		return stats;
	}

	public List<WorkerStatDTO> getLast30WorkLoad() {
		List<WorkerStatDTO> stats = new ArrayList<>();
		
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		List<Worker> workers = workerDaoImpl.getAll();
		
		Map<Integer, Integer> idWorkingHours = DBStats.getLast30WorkingHours(conn);
		
		WorkerStatDTO workerDTO;
		for (Worker w : workers) {
			int workingHours = 0;
			workerDTO = new WorkerStatDTO();
			workerDTO.setWorker(w);
			if (idWorkingHours.containsKey(w.getId())) {
				workingHours = idWorkingHours.get(w.getId());
			}
			workerDTO.setWorkingHours(workingHours);
			stats.add(workerDTO);
		}
		
		return stats;
	}
}
