package gc.service;

import java.io.IOException;
import java.io.InputStream;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import gc.dao.EInvoiceDaoImpl;
import gc.model.EInvoice;
import gc.model.Invoice;
import gc.utils.JaxbUtil;
import gc.utils.Utils;

@Path("/einvoice")
public class EinvoiceService {
	private static final Logger logger = LogManager
			.getLogger(EinvoiceService.class.getName());
	private static final String UPLOAD_FOLDER = "D:\\tomcat\\apache-tomcat-9.0.8\\webapps\\GCRest\\WEB-INF\\UPLOADED\\";

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
	public Response addEinvoice(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		try {
			// check if all form parameters are provided
			if (uploadedInputStream == null || fileDetail == null) {
				return Response.status(400).entity("Invalid file").build();
			}

			// create destination folder, if it not exists
			Utils.createFolderIfNotExists(UPLOAD_FOLDER);

			String uploadedFileLocation = UPLOAD_FOLDER
					+ fileDetail.getFileName();
			Utils.saveToFile(uploadedInputStream, uploadedFileLocation);

			String xml = new String(Utils.removeP7MCodes(uploadedFileLocation));
			EInvoice einv = JaxbUtil.jaxbUnMarshal(xml, EInvoice.class);

			EInvoiceDaoImpl einvoiceDaoImpl = new EInvoiceDaoImpl();

			// Creating orders
			List<Invoice> inv = einvoiceDaoImpl.getOrders(einv);

			// Creating deadlines
			einvoiceDaoImpl.getDeadlines(einv);

			// Creating attachments
			List<String> attachmentList = einvoiceDaoImpl.getAttachments(einv);
			if (inv.size() > 0 && !attachmentList.isEmpty()) {
				inv.get(0).setAttachments(attachmentList);
			}

			return Response.status(200).entity(inv).build();
		} catch (IOException e) {
			logger.error("Error adding invoice or copying file", e);
			return Response.status(500).entity("Cannot save file!").build();
		} catch (SecurityException e) {
			logger.error("Error adding invoice or copying file", e);
			return Response.status(500)
					.entity("Cannot create destination folder on server!")
					.build();
		}
	}
}