package gc.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gc.dao.BuildingDaoImpl;
import gc.dao.OrderDaoImpl;
import gc.model.Building;
import gc.model.Order;
import gc.model.types.Address;
import gc.model.types.GMap;

@Path("/building")
public class BuildingService {

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

		for (Building el : buildingList) {
			String type = el.getAddress().getAddressType();
			if (type != null)
				el.getAddress().setAddressType(Address.getDescFromCode(type));
		}

		return Response.status(200).entity(buildingList).build();
	}

	/**
	 * Get the distance from Google Maps API
	 * 
	 * @param dest address of building
	 * @return distance and time to reach
	 */
	// https://maps.googleapis.com/maps/api/directions/json?origin=Noci&destination=PiazzaMassariBari
	@GET
	@Path("/directions/{dest : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirections(@PathParam("dest") String dest) {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://maps.googleapis.com/maps/api/directions/json")
				.queryParam("origin", "Via V. Guerra, 5, 70015 Noci BA").queryParam("destination", dest)
				.queryParam("language", "it");
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		GMap route = response.readEntity(GMap.class);

		return Response.status(200).entity(route).build();
	}

	/**
	 * Add a building
	 * 
	 * @param building to add
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

		for (Building el : buildingList) {
			if (el.getName().equalsIgnoreCase(building.getName())) {
				return Response.status(Response.Status.PRECONDITION_FAILED)
						.entity("{\"error\": \"Building already exists with name: " + building.getName() + "\"}")
						.build();
			}
		}

		long todayMillis = new Date().getTime();
		if (building.getEnd_date() == null || building.getEnd_date().after(new java.sql.Date(todayMillis))) {
			building.setOpen(true);
		}

		buildingDaoImpl.insertBuilding(building);
		return Response.ok(building).build();
	}

	/**
	 * Assign a list of orders to a building
	 * 
	 * @param name      of the building
	 * @param orderList to assign
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
		for (Order el : orderList) {
			if (el.isState()) {
				el.setBuilding_id(building.getId());
				orderDaoImpl.updateOrder(el);
			}
		}

		return Response.ok().build();
	}

	/**
	 * Get a summary of utils for the building
	 * 
	 * @param name of the building
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
}