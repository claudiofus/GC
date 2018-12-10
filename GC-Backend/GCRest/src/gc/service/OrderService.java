package gc.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import gc.dao.OrderDaoImpl;
import gc.dao.ProductDaoImpl;
import gc.model.Order;
import gc.model.Product;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.utils.Utils;

@Path("/order")
public class OrderService {
	private static final Logger logger = LogManager.getLogger(OrderService.class.getName());
	private static final String UPLOAD_FOLDER = "D:\\GC\\GCRest\\WebContent\\WEB-INF\\UPLOADED\\";

	/**
	 * Returns text response to caller containing uploaded file location
	 * 
	 * @param uploadedInputStream invoice file
	 * @param fileDetail          file name
	 * @param provider            of the invoice
	 * @return error response in case of missing parameters an internal exception or
	 *         success response if file has been stored successfully
	 */
	@POST
	@Path("/insertOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("provider") String provider) {

		try {
			// check if all form parameters are provided
			if (uploadedInputStream == null || fileDetail == null || provider == null || provider.isEmpty()) {
				return Response.status(400).entity("Invalid form data").build();
			}

			// create our destination folder, if it not exists
			Utils.createFolderIfNotExists(UPLOAD_FOLDER);

			String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
			Utils.saveToFile(uploadedInputStream, uploadedFileLocation);

			File file = new File(uploadedFileLocation);
			OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
			Map<String, ArrayList<Order>> map = orderDaoImpl.addOrder(provider, file);
			return map != null ? Response.status(200).entity(map).build()
					: Response.status(500).entity("Order map is empty!").build();
		} catch (IOException e) {
			return Response.status(500).entity("Cannot save file!").build();
		} catch (SecurityException e) {
			return Response.status(500).entity("Cannot create destination folder on server!").build();
		}
	}

	/**
	 * Add a order from the invoice
	 * 
	 * @param providerCode of the provider
	 * @param ord          to add
	 * @return added order
	 * @throws IOException if ord is invalid
	 */
	@POST
	@Path("/addOrder/{provider : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addOrder(@PathParam("provider") String providerCode, BaseOrder ord) throws IOException {
		if (ord.getCode() == null || ord.getName() == null || ord.getCode().isEmpty() || ord.getName().isEmpty()
				|| providerCode == null || providerCode.isEmpty()) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		List<Product> productList = null;

		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		productList = productDaoImpl.getProducts();

		if (!Utils.containsCode(productList, ord.getCode()) && !Utils.containsName(productList, ord.getName())) {
			productDaoImpl.insertProduct(ord.getCode(), ord.getName(), providerCode);
		} else {
			logger.warn("Il prodotto esiste già nel database.");
		}

		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		orderDaoImpl.insertOrder(ord);
		return Response.ok(ord).build();
	}

	/**
	 * Update an order
	 * 
	 * @param ord to update
	 * @return updated order
	 */
	@POST
	@Path("/updateOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrder(Order ord) {
		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		orderDaoImpl.updateOrder(ord);
		return Response.ok(ord).build();
	}

	/**
	 * Get all unit of measurement
	 * 
	 * @return list of unit of measurement
	 */
	@GET
	@Path("/um/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUM() {
		List<UM> UMsList = null;

		OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
		UMsList = orderDaoImpl.getUMs();

		return Response.status(200).entity(UMsList).build();
	}
}