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

import gc.dao.EventDaoImpl;
import gc.model.Event;

@Path("/event")
public class EventService {

	/**
	 * Get all events.
	 * 
	 * @return List of all events.
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEvents() {
		List<Event> eventList = null;

		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		eventList = eventDaoImpl.getEvents();

		return Response.status(200).entity(eventList).build();
	}

	@POST
	@Path("/addEvent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addEvent(final Event event) throws IOException {
		if (event.getTitle() == null || event.getTitle().isEmpty()) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		List<Event> eventList = null;

		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		eventList = eventDaoImpl.getEvents();

		for (Event el : eventList) {
			if (el.getTitle().equalsIgnoreCase(event.getTitle())) {
				return Response.status(Response.Status.PRECONDITION_FAILED)
						.entity("{\"error\": \"Event already exists with name: "
								+ event.getTitle() + "\"}")
						.build();
			}
		}

		eventDaoImpl.insertEvent(event);
		return Response.ok(event).build();
	}
}