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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		if (ord.getName() == null || ord.getName().isEmpty() || providerCode == null || providerCode.isEmpty()) {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}

		List<Product> productList = null;

		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		productList = productDaoImpl.getProducts();

		if (!Utils.containsName(productList, ord.getName())) {
			productDaoImpl.insertProduct(ord.getName(), providerCode);
		} else {
			logger.warn("Il prodotto esiste gi� nel database.");
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