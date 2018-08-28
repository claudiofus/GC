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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import gc.dao.BuildingDaoImpl;
import gc.dao.OrderDaoImpl;
import gc.model.Building;
import gc.model.Order;
import gc.model.types.Address;

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
			if (type != null) el.getAddress().setAddressType(Address.getDescFromCode(type));
		}

		return Response.status(200).entity(buildingList).build();
	}

	// https://maps.googleapis.com/maps/api/directions/json?origin=Noci&destination=PiazzaMassariBari
	@GET
	@Path("/directions/{dest : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirections(@PathParam("dest") String dest) {

		Client client = new Client();
		WebResource webResource = client
				.resource(
						"https://maps.googleapis.com/maps/api/directions/json")
				.queryParam("origin", "Via V. Guerra, 5, 70015 Noci BA")
				.queryParam("destination", dest).queryParam("language", "it");
		String result = webResource.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class).getEntity(String.class);

		return Response.status(200).entity(result).build();
	}

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
						.entity("{\"error\": \"Building already exists with name: "
								+ building.getName() + "\"}")
						.build();
			}
		}

		long todayMillis = new Date().getTime();
		if (building.getEnd_date() == null || building.getEnd_date()
				.after(new java.sql.Date(todayMillis))) {
			building.setOpen(true);
		}

		buildingDaoImpl.insertBuilding(building);
		return Response.ok(building).build();
	}

	@POST
	@Path("/assignBuilding/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignBuilding(@PathParam("name") String name,
			final Order[] orderList) throws IOException {

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

	@GET
	@Path("/details/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildingDetails(@PathParam("name") String name)
			throws IOException {
		List<Order> orderList = null;
		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		orderList = orderDaoImpl.getOrders(name);

		return Response.ok(orderList).build();
	}
}