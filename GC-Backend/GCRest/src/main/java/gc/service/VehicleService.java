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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.dao.VehicleDaoImpl;
import gc.model.Event;
import gc.model.Vehicle;
import gc.model.types.BaseDeadline;
import gc.utils.Utils;

@Path("/vehicle")
public class VehicleService {
	private static final Logger logger = LogManager.getLogger(VehicleService.class.getName());
	EventService evService = new EventService();

	/**
	 * Add a vehicle
	 * 
	 * @param vehicle to add
	 * @return added vehicle
	 * @throws IOException
	 */
	@POST
	@Path("/addVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addVehicle(final Vehicle vehicle) throws IOException {

		if (vehicle.getPlate() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		List<Vehicle> vehicleList = null;
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		vehicleList = vehicleDaoImpl.getAll();

		if (Utils.containsPlate(vehicleList, vehicle.getPlate())) {
			logger.warn("Il veicolo esiste già nel database.");
		} else {
			Event ev = new Event();
			String vehicleStr = "€ - " + vehicle.getPlate() + " - " + vehicle.getBrand() + " " + vehicle.getModel();

			if (vehicle.getInsurance() != null) {
				ev.setTitle("Scadenza assicurazione - " + vehicle.getInsurance().getAmount() + vehicleStr);
				ev.setStartDate(vehicle.getInsurance().getDeadlineDate());
				ev.setPaid(false);
				evService.addEvent(ev);
				vehicle.getInsurance().setEventID(ev.getId());
			}
			if (vehicle.getCarTax() != null) {
				ev = new Event();
				ev.setTitle("Scadenza bollo - " + vehicle.getCarTax().getAmount() + vehicleStr);
				ev.setStartDate(vehicle.getCarTax().getDeadlineDate());
				ev.setPaid(false);
				evService.addEvent(ev);
				vehicle.getCarTax().setEventID(ev.getId());
			}
			if (vehicle.getRevision() != null) {
				ev = new Event();
				ev.setTitle("Scadenza revisione - " + vehicle.getRevision().getAmount() + vehicleStr);
				ev.setStartDate(vehicle.getRevision().getDeadlineDate());
				ev.setPaid(false);
				evService.addEvent(ev);
				vehicle.getRevision().setEventID(ev.getId());
			}
			vehicleDaoImpl.save(vehicle);
		}

		return Response.ok(vehicle).build();
	}

	/**
	 * Get all vehicles
	 * 
	 * @return list of vehicles
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicles() {
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		List<Vehicle> vehicleList = vehicleDaoImpl.getAll();

		return Response.ok(vehicleList).build();
	}

	/**
	 * Get the insurance of all vehicles
	 * 
	 * @return list of insurances
	 */
	@GET
	@Path("/insurance/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInsurances() {
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		List<Vehicle> vehicleList = vehicleDaoImpl.getInsurances();

		return Response.ok(vehicleList).build();
	}

	/**
	 * Get the car tax of all vehicles
	 * 
	 * @return list of car taxes
	 */
	@GET
	@Path("/cartax/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCarTaxes() {
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		List<Vehicle> vehicleList = vehicleDaoImpl.getCarTaxes();

		return Response.ok(vehicleList).build();
	}

	/**
	 * Get the revision of all vehicles
	 * 
	 * @return list of revisions
	 */
	@GET
	@Path("/revision/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRevisions() {
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		List<Vehicle> vehicleList = vehicleDaoImpl.getRevisions();

		return Response.ok(vehicleList).build();
	}

	/**
	 * Get the penalties of all vehicles
	 * 
	 * @return list of penalties
	 */
	@GET
	@Path("/penalty/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPenalties() {
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		List<Vehicle> vehicleList = vehicleDaoImpl.getPenalties();

		return Response.ok(vehicleList).build();
	}

	/**
	 * Add a penalty
	 * 
	 * @param vehicle of the penalty
	 * @return vehicle obj updated with penalty
	 * @throws IOException
	 */
	@POST
	@Path("/addPenalty")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPenalty(final Vehicle vehicle) throws IOException {
		if (vehicle.getPlate() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		if (vehicle.getPenalty() != null) {
			Response res = getVehicles();
			List<?> vehicles = (List<?>) res.getEntity();
			boolean found = false;
			Vehicle exVehicle = null;

			for (int i = 0; i < vehicles.size() && !found; i++) {
				if (vehicles.get(i) instanceof Vehicle) {
					exVehicle = (Vehicle) vehicles.get(i);
					if (exVehicle.getPlate().equalsIgnoreCase(vehicle.getPlate())) {
						found = true;
					}
				}
			}

			if (!found) {
				return Response.status(Response.Status.PRECONDITION_FAILED).build();
			}

			VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
			// TODO usare String format con currency 
			String vehicleStr = "€ - " + vehicle.getPlate() + " - " + exVehicle.getBrand() + " " + exVehicle.getModel();
			java.sql.Date penDate = vehicle.getPenalty().get(0).getDeadlineDate();
			Event ev = new Event();
			ev.setTitle("Multa del " + Utils.sqlDateToDate(penDate, "dd/MM/yyyy") + " - "
					+ vehicle.getPenalty().get(0).getAmount() + vehicleStr);
			ev.setStartDate(penDate);
			ev.setPaid(vehicle.getPenalty().get(0).isPaid());
			evService.addEvent(ev);
			exVehicle.setPenalty(vehicle.getPenalty());
			exVehicle.getPenalty().get(0).setEventID(ev.getId());

			vehicleDaoImpl.insertPenalty(exVehicle);
		}

		return Response.ok(vehicle).build();
	}

	/**
	 * Update insurance, car tax or revision of a vehicle
	 * 
	 * @param vehicle to update
	 * @return updated vehicle
	 * @throws IOException
	 */
	@POST
	@Path("/updateVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateVehicle(final Vehicle vehicle) {
		if (vehicle.getPlate() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		BaseDeadline baseDeadline = null;
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		if (vehicle.getInsurance() != null) {
			baseDeadline = vehicle.getInsurance();
			vehicleDaoImpl.updateInsurance(vehicle);
		} else if (vehicle.getCarTax() != null) {
			baseDeadline = vehicle.getCarTax();
			vehicleDaoImpl.updateCarTax(vehicle);
		} else if (vehicle.getRevision() != null) {
			baseDeadline = vehicle.getRevision();
			vehicleDaoImpl.updateRevision(vehicle);
		}

		if (baseDeadline != null) {
			Response res = evService.getEventByID(baseDeadline.getEventID());
			Event ev = (Event) res.getEntity();
			ev.setStartDate(baseDeadline.getDeadlineDate());
			// TODO usare String format con currency
			String[] splitted = ev.getTitle().split("-");
			splitted[1] = " " + baseDeadline.getAmount() + "€ ";
			ev.setTitle(String.join("-", splitted));
			evService.updateEvent(ev);
		}

		return Response.ok().build();
	}
}