package gc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gc.dao.DDTDaoImpl;
import gc.dao.EInvoiceDaoImpl;
import gc.einvoice.DatiDDTType;
import gc.model.DDT;
import gc.model.EInvoice;
import gc.model.Provider;
import gc.utils.Utils;

@Path("/ddt")
public class DDTService {
	/**
	 * Get ddt from id
	 * 
	 * @param id
	 *            of the ddt
	 * @return ddt found
	 */
	@GET
	@Path("/{ddtid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDDTByID(@PathParam("ddtid") int ddtid) {
		DDTDaoImpl ddtDaoImpl = new DDTDaoImpl();
		DDT ddt = ddtDaoImpl.getDDTByID(ddtid);

		return Response.status(200).entity(ddt).build();
	}

	/**
	 * Add a DDT
	 * 
	 * @param ddt
	 *            to add
	 * @return ddt added
	 * @throws IOException
	 *             if there's no number
	 */
	@POST
	@Path("/addDDT")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDDTbyEinv(EInvoice einv) {
		DDTDaoImpl ddtDaoImpl = new DDTDaoImpl();
		EInvoiceDaoImpl einvoiceDaoImpl = new EInvoiceDaoImpl();

		List<DatiDDTType> ddts = einv.getDDTs();
		List<DDT> ddtsDef = ddtDaoImpl.getDDTs();
		List<DDT> ddtsEinv = new ArrayList<>();
		Provider provider = einvoiceDaoImpl.checkProvider(einv);

		for (DatiDDTType ddt : ddts) {
			boolean yetDefined = Utils.containsDDT(ddtsDef, ddt.getNumeroDDT(), provider.getId());
			if (!yetDefined) {
				DDT temp = new DDT();
				temp.setNumeroDDT(ddt.getNumeroDDT());
				temp.setDataDDT(ddt.getDataDDT());
				temp.setProviderId(provider.getId());
				temp.setNumeroRiferimentoLinea(ddt.getRiferimentoNumeroLinea());
				ddtDaoImpl.insertDDT(temp);
				ddtsEinv.add(temp);
			}
		}

		return Response.status(200).entity(ddtsEinv).build();
	}
}
