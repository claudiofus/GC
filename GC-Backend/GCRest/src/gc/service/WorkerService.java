package gc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gc.dao.BuildingDaoImpl;
import gc.dao.WorkerDaoImpl;
import gc.model.Building;
import gc.model.Worker;
import gc.model.types.job.Job;
import gc.utils.Utils;

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
		List<Worker> workers = workerDaoImpl.getAll();
		return Response.ok(workers).build();
	}

	/**
	 * Add a worker
	 * 
	 * @param worker
	 *            to add
	 * @return worker added
	 * @throws IOException
	 *             if a same title is found
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
		List<Worker> workerList = workerDaoImpl.getAll();

		for (Worker el : workerList) {
			if (el.getFiscalCode().equalsIgnoreCase(worker.getFiscalCode())) {
				Worker updWorker = workerDaoImpl.updateWorker(worker, el.getId());
				return Response.ok(updWorker).build();
			}
		}

		workerDaoImpl.insertWorker(worker);
		return Response.ok(worker).build();
	}

	/**
	 * Get a summary of worker
	 * 
	 * @param id
	 *            of the worker
	 * @return list of details for the worker
	 * @throws IOException
	 */
	@GET
	@Path("/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildingDetails(@PathParam("id") int id) {
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		Worker worker = workerDaoImpl.getWorkersById(id);
		return Response.ok(worker).build();
	}

	/**
	 * Get hours of a worker
	 * 
	 * @param id
	 *            of the worker
	 * @return list of hours for the worker
	 * @throws IOException
	 */
	@GET
	@Path("/hours/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkerHours(@PathParam("id") int id) {
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		Map<String, Integer> workerHours = workerDaoImpl.getWorkerHours(id);
		return Response.ok(workerHours).build();
	}

	@POST
	@Path("/assignBuilding/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignBuilding(@PathParam("name") String name, final Job job) {
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		Building building = buildingDaoImpl.getBuildingDetails(name);
		List<Job> jobs = buildingDaoImpl.getJobs(building.getId());
		if (jobs.stream().anyMatch(o -> o.getId() == job.getId())) {
			workerDaoImpl.updateWorker(job);
		} else {
			workerDaoImpl.assignWorker(job, building.getId());
		}

		return Response.ok().build();
	}

	@POST
	@Path("/cost")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response calcCost(final Job job) {
		WorkerDaoImpl workerDaoImpl = new WorkerDaoImpl();
		Worker worker = workerDaoImpl.getWorkersById(job.getWorkerId());
		if (worker.getSalary() != null) {
			return Response.ok(Utils.multiply(worker.getSalary().getSalaryForHour(), job.getHoursOfWork())).build();
		}
		return Response.serverError().build();
	}
}