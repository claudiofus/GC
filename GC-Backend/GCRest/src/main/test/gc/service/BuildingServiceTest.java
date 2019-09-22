package gc.service;

import static gc.test.util.ServiceTest.APP_REST_PATH;
import static gc.test.util.ServiceTest.BASE_URI;
import static gc.test.util.ServiceTest.PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import gc.model.Building;
import gc.model.types.Address;

class BuildingServiceTest {

	@Test
	void testGetBuildings() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(BASE_URI + ":" + PORT + APP_REST_PATH + "/building/all");
		Response response = target.request(MediaType.APPLICATION_JSON).get(Response.class);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
	}

	@Test
	void testAddBuilding() {
		Building newBuilding = new Building();
		Address address = new Address();
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(BASE_URI + ":" + PORT + APP_REST_PATH + "/building/addBuilding");
		Entity<Building> buildingEntity = Entity.entity(newBuilding, MediaType.APPLICATION_JSON);
		Builder requestBuilder = target.request(MediaType.APPLICATION_JSON);
		Response response = requestBuilder.post(buildingEntity);
		assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
		
		newBuilding.setName("Test");
		response = requestBuilder.post(buildingEntity);
		assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
		
		newBuilding.setAddress(address);
		response = requestBuilder.post(buildingEntity);
		assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
		
		newBuilding.setReqAmount(100f);
		response = requestBuilder.post(buildingEntity);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
		
		Building addedBuilding = response.readEntity(gc.model.Building.class);
		assertEquals("Test", addedBuilding.getName());
		assertNotNull(addedBuilding.getAddress());
		assertEquals(100f, addedBuilding.getReqAmount());
	}

//	@Test
//	void testAssignBuilding() {
//		fail("Not yet implemented");
//	}

	@Test
	void testGetBuildingDetails() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(BASE_URI + ":" + PORT + APP_REST_PATH + "/building/details/5");
		Response response = target.request(MediaType.APPLICATION_JSON).get(Response.class);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
	}
//
//	@Test
//	void testGetBuildingJobs() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteJob() {
//		fail("Not yet implemented");
//	}

}
