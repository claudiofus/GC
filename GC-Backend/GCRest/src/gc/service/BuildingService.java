package gc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.dao.BuildingDaoImpl;
import gc.dao.OrderDaoImpl;
import gc.model.Building;
import gc.model.Order;
import gc.model.types.job.Job;

@Path("/building")
public class BuildingService {
	private static final Logger logger = LogManager.getLogger(BuildingService.class.getName());

	/**
	 * Get all buildings.
	 * 
	 * @return List of all buildings.
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildings() {
		List<Building> buildingList = null;

		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		buildingList = buildingDaoImpl.getBuildings();

		return Response.status(200).entity(buildingList).build();
	}

	/**
	 * Add a building
	 * 
	 * @param building
	 *            to add
	 * @return building added
	 * @throws IOException
	 */
	@POST
	@Path("/addBuilding")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addBuilding(final Building building) throws IOException {
		if (building.getName() == null || building.getName().isEmpty()) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		List<Building> buildingList = null;

		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		buildingList = buildingDaoImpl.getBuildings();

		long todayMillis = new Date().getTime();
		if (building.getEnd_date() == null || building.getEnd_date().after(new java.sql.Date(todayMillis))) {
			building.setOpen(true);
		}
		
		for (Building el : buildingList) {
			if (el.getName().equalsIgnoreCase(building.getName())) {
				logger.warn("Updating building : " + building.getName());
				buildingDaoImpl.updateBuilding(building);
				return Response.ok(building).build();
			}
		}

		buildingDaoImpl.insertBuilding(building);
		return Response.ok(building).build();
	}

	/**
	 * Assign a list of orders to a building
	 * 
	 * @param name
	 *            of the building
	 * @param orderList
	 *            to assign
	 * @return valid response
	 * @throws IOException
	 */
	@POST
	@Path("/assignBuilding/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignBuilding(@PathParam("name") String name, final Order[] orderList) throws IOException {

		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		Building building = buildingDaoImpl.getBuildingDetails(name);

		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		List<Integer> updOrderList = new ArrayList<>();
		for (Order el : orderList) {
			if (el.isState()) {
				el.setBuilding_id(building.getId());
				orderDaoImpl.updateOrder(el);
				updOrderList.add(el.getId());
			}
		}

		return Response.ok(updOrderList).build();
	}

	/**
	 * Get a summary of utils for the building
	 * 
	 * @param name
	 *            of the building
	 * @return list of expenses for the building
	 * @throws IOException
	 */
	@GET
	@Path("/details/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildingDetails(@PathParam("name") String name) throws IOException {
		List<Order> orderList = null;
		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		orderList = orderDaoImpl.getOrders(name);

		return Response.ok(orderList).build();
	}

	@GET
	@Path("/jobs/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildingJobs(@PathParam("name") String name) throws IOException {
		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		Building building = buildingDaoImpl.getBuildingDetails(name);

		List<Job> jobs = buildingDaoImpl.getJobs(building.getId());
		return Response.ok(jobs).build();
	}

	@POST
	@Path("/jobsDel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteJob(final int job_id) {
		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		boolean result = buildingDaoImpl.deleteJob(job_id);
		return Response.ok(result).build();
	}
}