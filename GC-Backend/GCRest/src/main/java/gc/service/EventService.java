package gc.service;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		List<Event> eventList = eventDaoImpl.getAll();

		return Response.status(200).entity(eventList).build();
	}

	/**
	 * Get event from id
	 * 
	 * @param eventID of the event
	 * @return event found
	 */
	@GET
	@Path("/{eventid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventByID(@PathParam("userid") int eventID) {
		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		Event event = eventDaoImpl.get(eventID);

		return Response.status(200).entity(event).build();
	}

	/**
	 * Add an event
	 * 
	 * @param event to add
	 * @return event added
	 * @throws IOException if a same title is found
	 */
	@POST
	@Path("/addEvent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addEvent(final Event event) throws IOException {
		if (event.getTitle() == null || event.getTitle().isEmpty()) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		List<Event> eventList = eventDaoImpl.getAll();

		for (Event el : eventList) {
			if (el.getId() == event.getId()) {
				eventDaoImpl.update(event, null);
				return Response.ok(event).build();
			}
		}

		eventDaoImpl.save(event);
		return Response.ok(event).build();
	}

	/**
	 * Update an event
	 * 
	 * @param ev to update
	 * @return event updated
	 */
	@POST
	@Path("/updateEvent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateEvent(final Event ev) {
		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		eventDaoImpl.update(ev, null);
		return Response.ok(ev).build();
	}

	/**
	 * Delete an event
	 * 
	 * @param ev to delete
	 * @return true if delete succeded
	 */
	@POST
	@Path("/deleteEvent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteEvent(final Event ev) {
		EventDaoImpl eventDaoImpl = new EventDaoImpl();
		eventDaoImpl.delete(ev);
		return Response.ok().build();
	}
}