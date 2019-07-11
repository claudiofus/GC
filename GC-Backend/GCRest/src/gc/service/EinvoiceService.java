package gc.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import gc.dao.EInvoiceDaoImpl;
import gc.model.DDT;
import gc.model.EInvoice;
import gc.model.Invoice;
import gc.model.Order;
import gc.model.Provider;
import gc.utils.JaxbUtil;
import gc.utils.Utils;

@Path("/einvoice")
public class EinvoiceService {
	private static final Logger logger = LogManager.getLogger(EinvoiceService.class.getName());
	private static final String UPLOAD_FOLDER = "D:\\tomcat\\apache-tomcat-9.0.8\\webapps\\GCRest\\WEB-INF\\UPLOADED\\";
	DDTService ddtService = new DDTService();
	OrderService ordService = new OrderService();

	/**
	 * Add e-invoice
	 * 
	 * @param uploadedInputStream
	 *            invoice file
	 * @param fileDetail
	 *            file name
	 * @return parsed invoice
	 */
	@POST
	@Path("/addEinvoice")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addEinvoice(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		try {
			// check if all form parameters are provided
			if (uploadedInputStream == null || fileDetail == null) {
				return Response.status(400).entity("Invalid file").build();
			}

			// create destination folder, if it not exists
			Utils.createFolderIfNotExists(UPLOAD_FOLDER);

			String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
			Utils.saveToFile(uploadedInputStream, uploadedFileLocation);

			String xml = new String(Utils.removeP7MCodes(uploadedFileLocation));
			EInvoice einv = JaxbUtil.jaxbUnMarshal(xml, EInvoice.class);

			EInvoiceDaoImpl einvoiceDaoImpl = new EInvoiceDaoImpl();

			Provider provider = einvoiceDaoImpl.checkProvider(einv);
			try {
				einv = einvoiceDaoImpl.createInvoice(einv, provider);

				// Retrieving all DDTs
				List<DDT> ddts = (List<DDT>) ddtService.addDDTbyEinv(einv).getEntity();

				// Creating orders
				List<Invoice> inv = einvoiceDaoImpl.getOrders(einv, ddts);

				// Creating deadlines
				einvoiceDaoImpl.getDeadlines(einv);

				// Creating attachments
				List<String> attachmentList = einvoiceDaoImpl.getAttachments(einv);
				if (!inv.isEmpty() && !attachmentList.isEmpty()) {
					inv.get(0).setAttachments(attachmentList);
				}

				return Response.status(200).entity(inv).build();
			} catch (IllegalStateException e) {
				return Response.status(500).entity("Non è possibile inserire due volte la stessa fattura.").build();
			}
		} catch (IOException e) {
			logger.error("Error adding invoice or copying file: {}", e);
			return Response.status(500).entity("Cannot save file!").build();
		} catch (SecurityException e) {
			logger.error("Error adding invoice or copying file: {}", e);
			return Response.status(500).entity("Cannot create destination folder on server!").build();
		}
	}

	/**
	 * Get the details of a provider
	 * 
	 * @param code
	 *            of the provider
	 * @return the provider found
	 */
	@GET
	@Path("/providers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoices(@PathParam("id") int id) {
		Provider provider = new Provider();
		provider.setId(id);
		EInvoiceDaoImpl einvoiceDaoImpl = new EInvoiceDaoImpl();
		List<EInvoice> invList = einvoiceDaoImpl.getInvoices(provider);

		return Response.ok(invList).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoice(@PathParam("id") int id) {
		EInvoiceDaoImpl einvoiceDaoImpl = new EInvoiceDaoImpl();
		EInvoice einv = einvoiceDaoImpl.getInvoice(id);

		return Response.ok(einv).build();
	}

	@GET
	@Path("/rebuild/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response rebuildInvoice(@PathParam("id") int id) {
		EInvoiceDaoImpl einvoiceDaoImpl = new EInvoiceDaoImpl();
		Invoice inv = new Invoice();

		List<DDT> ddts = einvoiceDaoImpl.rebuildDDTs(id);
		inv.setDdts(ddts);

		List<Order> ordersId = einvoiceDaoImpl.rebuildOrders(id);
		// List<Order> orders = new ArrayList<>();
		Map<String, ArrayList<Order>> map = new HashMap<>();
		for (Order ord : ordersId) {
			Order temp = (Order) ordService.getOrder(ord.getId()).getEntity();
			// orders.add(temp);

			if (temp.getDdtId() == 0) {
				addToMap(map, temp, "");
			} else {
				DDT ddt = (DDT) ddtService.getDDTByID(temp.getDdtId()).getEntity();
				if (Utils.containsDDT(ddts, ddt.getNumeroDDT(), ddt.getProviderId())) {
					String ddtTitle = Utils.getFancyTitle(ddt);
					addToMap(map, temp, ddtTitle);
				}
			}
		}

		inv.setDDTOrders(map);

		return Response.ok(inv).build();
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
}