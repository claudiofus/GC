package gc.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gc.dao.ProviderDaoImpl;
import gc.model.Provider;

@Path("/provider")
public class ProviderService {

	/**
	 * Get all providers
	 * 
	 * @return list of providers
	 */
	@GET
	@Path("/providers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProviders() {
		ProviderDaoImpl providerDaoImpl = new ProviderDaoImpl();
		List<Provider> providerList = providerDaoImpl.getProviders();

		return Response.ok(providerList).build();
	}

	/**
	 * Get the details of a provider
	 * 
	 * @param code of the provider
	 * @return the provider found
	 */
	@GET
	@Path("/providers/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProvider(@PathParam("code") String name) {
		ProviderDaoImpl providerDaoImpl = new ProviderDaoImpl();
		Provider provider = providerDaoImpl.getProviderDetails(name);

		return Response.ok(provider).build();
	}
}