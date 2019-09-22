package gc.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.dao.BuildingDaoImpl;
import gc.dao.DDTDaoImpl;
import gc.dao.OrderDaoImpl;
import gc.dto.OrderDTO;
import gc.model.Building;
import gc.model.DDT;
import gc.model.EInvoice;
import gc.model.Order;
import gc.model.Provider;
import gc.model.types.job.Job;
import gc.utils.Utils;

@Path("/building")
public class BuildingService {
	private static final Logger logger = LogManager.getLogger(BuildingService.class.getName());
	private static final Date todayMidnight = Utils.getTodayMidnight();
	DDTService ddtService = new DDTService();
	EinvoiceService eInvoiceService = new EinvoiceService();
	ProviderService prService = new ProviderService();

	/**
	 * Get all buildings.
	 * 
	 * @return List of all buildings.
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildings() {
		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		List<Building> buildingList = buildingDaoImpl.getAll();

		for (Building b : buildingList) {
			boolean valid = checkBuildingValid(b);
			if (b.isOpen() != valid) {
				b.setOpen(valid);
				buildingDaoImpl.update(b, null);
			}
		}

		return Response.status(200).entity(buildingList).build();
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
	public Response addBuilding(final Building building) {
		if (building.getName() == null || building.getName().isEmpty() || building.getAddress() == null
				|| building.getReqAmount() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		List<Building> buildingList = buildingDaoImpl.getAll();

		boolean buildingValid = checkBuildingValid(building);
		building.setOpen(buildingValid);

		for (Building el : buildingList) {
			if (el.getName().equals(building.getName())) {
				logger.warn("Updating building : {}", building.getName());
				buildingDaoImpl.update(building, null);
				return Response.ok(building).build();
			}
		}

		buildingDaoImpl.save(building);
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
	@Path("/assignBuilding/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignBuilding(@PathParam("id") int id, OrderDTO body) {
		final Order[] orderList = body.getOrder();
		final int ddtId = body.getDdtId();
		DDT ddtData = null;

		if (ddtId != 0) {
			DDTDaoImpl ddtDaoImpl = new DDTDaoImpl();
			List<DDT> ddtList = ddtDaoImpl.getAll();
			for (DDT ddt : ddtList) {
				if (ddt.getId() == ddtId) {
					ddtData = ddt;
					break;
				}
			}
		}

		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		Building building = buildingDaoImpl.get(id);

		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		List<Integer> updOrderList = new ArrayList<>();
		for (Order el : orderList) {
			if (el.isState()) {
				el.setBuildingId(building.getId());
				el.setDdtId(ddtData != null ? ddtData.getId() : null);
				orderDaoImpl.update(el, null);
				updOrderList.add(el.getId());
			}
		}

		return Response.ok(updOrderList).build();
	}

	/**
	 * Get a summary of utils for the building
	 * 
	 * @param name of the building
	 * @return list of expenses for the building
	 * @throws IOException
	 */
	@GET
	@Path("/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildingDetails(@PathParam("id") int id) {
		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		List<Order> orderList = orderDaoImpl.getAll(id);

		Map<String, ArrayList<Order>> map = new HashMap<>();
		for (Order ord : orderList) {
			if (ord.getDdtId() == 0) {
				EInvoice einv = (EInvoice) eInvoiceService.getInvoice(ord.getInvoiceId()).getEntity();
				Provider provider = (Provider) prService.getProviderById(einv.getProvider().getId()).getEntity();
				String einvTitle = MessageFormat.format("{0} - FATTURA n. {1} DEL {2,date,short}", provider.getName(),
						einv.getNumber(), einv.getDate());
				addToMap(map, ord, einvTitle);
			} else {
				DDT ddt = (DDT) ddtService.getDDTByID(ord.getDdtId()).getEntity();
				if (ddt != null) {
					Provider provider = (Provider) prService.getProviderById(ddt.getProviderId()).getEntity();

					// Creo il titolo e la lista di ordini per il ddt
					String ddtTitle = MessageFormat.format("{0} - {1}", provider.getName(), Utils.getFancyTitle(ddt));
					addToMap(map, ord, ddtTitle);
				}
			}
		}

		return Response.ok(map).build();
	}

	@GET
	@Path("/jobs/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBuildingJobs(@PathParam("id") int id) {
		BuildingDaoImpl buildingDaoImpl = new BuildingDaoImpl();
		Building building = buildingDaoImpl.get(id);

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

	private void addToMap(Map<String, ArrayList<Order>> map, Order ord, String title) {
		if (map.containsKey(title)) {
			map.get(title).add(ord);
		} else {
			ArrayList<Order> ordList = new ArrayList<>();
			ordList.add(ord);
			map.put(title, ordList);
		}
	}

	private boolean checkBuildingValid(Building b) {
		return b.getEndDate() == null || b.getEndDate().after(todayMidnight);
	}
}