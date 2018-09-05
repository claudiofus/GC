package gc.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import gc.dao.InvoiceDaoImpl;
import gc.model.Invoice;
import gc.utils.Utils;

@Path("/invoice")
public class InvoiceService {
	private static final String UPLOAD_FOLDER = "D:\\GC\\GCRest\\WebContent\\WEB-INF\\UPLOADED\\";

	@POST
	@Path("/addInvoice")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addInvoice(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("provider") String provider) {

		try {
			// check if all form parameters are provided
			if (uploadedInputStream == null || fileDetail == null
					|| provider == null || provider.isEmpty()) {
				return Response.status(400).entity("Invalid form data").build();
			}

			// create our destination folder, if it not exists
			Utils.createFolderIfNotExists(UPLOAD_FOLDER);

			String uploadedFileLocation = UPLOAD_FOLDER
					+ fileDetail.getFileName();
			Utils.saveToFile(uploadedInputStream, uploadedFileLocation);

			File file = new File(uploadedFileLocation);
			InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
			Invoice inv = invoiceDaoImpl.addInvoice(provider, file);

			return Response.status(200).entity(inv).build();
		} catch (IOException e) {
			return Response.status(500).entity("Cannot save file!").build();
		} catch (SecurityException e) {
			return Response.status(500)
					.entity("Cannot create destination folder on server!")
					.build();
		}
	}
}
