package gc.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.dao.BuildingDaoImpl;
import gc.dao.StatsDaoImpl;
import gc.dto.BuildingStatDTO;
import gc.dto.WorkerStatDTO;
import gc.model.Building;
import gc.model.types.job.Job;

@Path("/stats")
public class StatsService {
	private static final Logger logger = LogManager.getLogger(StatsService.class.getName());

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfits() {
		StatsDaoImpl statsDaoImpl = new StatsDaoImpl();
		List<BuildingStatDTO> statsList = statsDaoImpl.getAll();

		return Response.status(200).entity(statsList).build();
	}
	
	@GET
	@Path("/workload")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLast30WorkLoad() {
		StatsDaoImpl statsDaoImpl = new StatsDaoImpl();
		List<WorkerStatDTO> statsList = statsDaoImpl.getLast30WorkLoad();

		return Response.status(200).entity(statsList).build();
	}
	
	@GET
	@Path("/workload/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkingHoursByBuilding(@PathParam("id") int id) {
		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		Building building = buildingDaoImpl.get(id);

		List<Job> jobs = buildingDaoImpl.getJobs(building.getId());
		int workingHoursSum = jobs.stream().mapToInt(Job::getHoursOfWork).sum();

		return Response.status(200).entity(workingHoursSum).build();
	}
}
