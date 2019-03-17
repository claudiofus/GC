package gc.service;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gc.dao.WorkerDaoImpl;
import gc.model.Worker;

@Path("/worker")
public class WorkerService {

	/**
	 * Get all workers.
	 * 
	 * @return List of all workers.
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkers() {
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		List<Worker> workers = workerDaoImpl.getWorkers();
		return Response.ok(workers).build();
	}
	
	/**
	 * Add a worker
	 * 
	 * @param worker to add
	 * @return worker added
	 * @throws IOException if a same title is found
	 */
	@POST
	@Path("/addWorker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addWorker(final Worker worker) throws IOException {
		if (worker.getName() == null || worker.getSurname() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		List<Worker> workerList = workerDaoImpl.getWorkers();

		for (Worker el : workerList) {
			if (el.getFiscalCode().equalsIgnoreCase(worker.getFiscalCode())) {
				workerDaoImpl.updateWorker(worker);
				return Response.ok(worker).build();
			}
		}

		workerDaoImpl.insertWorker(worker);
		return Response.ok(worker).build();
	}

	/**
	 * Update an worker
	 * 
	 * @param ev to update
	 * @return worker updated
	 */
	@POST
	@Path("/updateWorker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateWorker(final Worker ev) {
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		workerDaoImpl.updateWorker(ev);
		return Response.ok(ev).build();
	}
}