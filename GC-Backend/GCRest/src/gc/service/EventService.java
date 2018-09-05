package gc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

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
			if (el.getId() == event.getId()) {
				eventDaoImpl.updateEvent(event);
				return Response.ok(event).build();
			}
		}

		eventDaoImpl.insertEvent(event);
		return Response.ok(event).build();
	}

	@POST
	@Path("/updateEvent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateEvent(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData, Charset.forName("UTF-8")));
			String line = null;
			while ((line = in.readLine()) != null) {
				strBuilder.append(line);
			}
			System.out.println("Data Received: " + strBuilder.toString());
			Gson gson = new Gson();
			Event ev = gson.fromJson(strBuilder.toString(), Event.class);

			EventDaoImpl eventDaoImpl = new EventDaoImpl();
			eventDaoImpl.updateEvent(ev);
			return Response.ok(gson.toJson(ev)).build();
		} catch (IOException e) {
			System.err.println("Error Parsing: - " + incomingData);
			return Response.status(500)
					.entity("Error Parsing: - " + incomingData).build();
		}
	}

	@POST
	@Path("/deleteEvent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteEvent(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData, Charset.forName("UTF-8")));
			String line = null;
			while ((line = in.readLine()) != null) {
				strBuilder.append(line);
			}
			System.out.println("Data Received: " + strBuilder.toString());
			Gson gson = new Gson();
			Event ev = gson.fromJson(strBuilder.toString(), Event.class);

			EventDaoImpl eventDaoImpl = new EventDaoImpl();
			eventDaoImpl.deleteEvent(ev);
			return Response.ok(gson.toJson(ev)).build();
		} catch (IOException e) {
			System.err.println("Error Parsing: - " + incomingData);
			return Response.status(500)
					.entity("Error Parsing: - " + incomingData).build();
		}
	}
}