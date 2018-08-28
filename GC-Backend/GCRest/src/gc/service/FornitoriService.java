package gc.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import gc.dao.ProviderDaoImpl;
import gc.model.Provider;

@Path("/provider")
public class FornitoriService {
	@GET
	@Path("/providers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProviders() {
		String providerDetails = null;
		List<Provider> providerList = null;

		ProviderDaoImpl providerDaoImpl = new ProviderDaoImpl();
		providerList = providerDaoImpl.getProviders();
		
		Gson gson = new Gson();
		providerDetails = gson.toJson(providerList);
		return providerDetails;
	}
	
	@GET
	@Path("/providers/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProvider(@PathParam("code") String code) {
		String providerDetails = null;
		Provider provider= null;

		ProviderDaoImpl providerDaoImpl = new ProviderDaoImpl();
		provider = providerDaoImpl.getProviderDetails(code);

		Gson gson = new Gson();
		providerDetails = gson.toJson(provider);
		return providerDetails;
	}
}
