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

import gc.dao.VehicleDaoImpl;
import gc.model.Event;
import gc.model.Vehicle;

@Path("/vehicle")
public class VehicleService {
	EventService evService = new EventService();

	@POST
	@Path("/addVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addVehicle(Vehicle vehicle) throws IOException {

		if (vehicle.getPlate() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		List<Vehicle> vehicleList = null;
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		vehicleList = vehicleDaoImpl.getVehicles();

		if (vehicleList.stream()
				.filter(o -> o.getPlate().equals(vehicle.getPlate()))
				.findFirst().isPresent()) {
			System.out.println("Il veicolo esiste già nel database.");
		} else {
			vehicleDaoImpl.insertVehicle(vehicle);
			Event ev = new Event();
			String vehicleStr = "€ - " + vehicle.getPlate() + " - "
					+ vehicle.getBrand() + " " + vehicle.getModel();

			if (vehicle.getInsurance() != null) {
				ev.setTitle("Scadenza assicurazione "
						+ vehicle.getInsurance().getAmount() + vehicleStr);
				ev.setStart_date(vehicle.getInsurance().getDeadlineDate());
				ev.setPaid(false);
				evService.addEvent(ev);
			}
			if (vehicle.getCarTax() != null) {
				ev = new Event();
				ev.setTitle("Scadenza bollo " + vehicle.getCarTax().getAmount()
						+ vehicleStr);
				ev.setStart_date(vehicle.getCarTax().getDeadlineDate());
				ev.setPaid(false);
				evService.addEvent(ev);
			}
			if (vehicle.getRevision() != null) {
				ev = new Event();
				ev.setTitle("Scadenza revisione "
						+ vehicle.getRevision().getAmount() + vehicleStr);
				ev.setStart_date(vehicle.getRevision().getDeadlineDate());
				ev.setPaid(false);
				evService.addEvent(ev);
			}
		}

		return Response.ok(vehicle).build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts() {
		VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
		List<Vehicle> vehicleList = vehicleDaoImpl.getVehicles();

		return Response.ok(vehicleList).build();
	}

	@POST
	@Path("/updateVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateVehicle(Vehicle vehicle) throws IOException {
		if (vehicle.getPlate() == null) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		if (vehicle.getPenalty() != null) {
			Response res = getProducts();
			List<?> vehicles = (List<?>) res.getEntity();
			boolean found = false;
			Vehicle temp = null;

			for (int i = 0; i < vehicles.size() && !found; i++) {
				if (vehicles.get(i) instanceof Vehicle) {
					temp = (Vehicle) vehicles.get(i);
					if (temp.getPlate().equalsIgnoreCase(vehicle.getPlate())) {
						found = true;
					}
				}
			}

			if (temp == null) {
				return Response.status(Response.Status.PRECONDITION_FAILED)
						.build();
			}

			VehicleDaoImpl vehicleDaoImpl = new VehicleDaoImpl();
			if (temp.getPenalty() != null) {
				temp.getPenalty().add(vehicle.getPenalty().get(0));
			} else {
				temp.setPenalty(vehicle.getPenalty());
			}
			vehicleDaoImpl.updateVehicle(temp);

			String vehicleStr = "€ - " + vehicle.getPlate() + " - "
					+ temp.getBrand() + " " + temp.getModel();
			Event ev = new Event();
			ev.setTitle("Multa " + vehicle.getPenalty().get(0).getAmount()
					+ vehicleStr);
			ev.setStart_date(vehicle.getPenalty().get(0).getDeadlineDate());
			ev.setPaid(vehicle.getPenalty().get(0).isPaid());
			evService.addEvent(ev);
		}

		return Response.ok().build();
	}
}