package gc.service;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gc.dao.ProductDaoImpl;
import gc.model.Product;

@Path("/product")
public class ProductService {

	/**
	 * Get all products of all providers.
	 * 
	 * @return List of all products.
	 */
	@GET
	@Path("/products")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts() {
		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		List<Product> productList = productDaoImpl.getAll();

		return Response.ok(productList).build();
	}

	/**
	 * Get the product with code passe in input.
	 * 
	 * @param name of the product to find.
	 * @return Product with name specified.
	 */
	@GET
	@Path("/price/{name : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("name") String name) {

		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		List<HashMap<String, Object>> product = productDaoImpl.getProductDetails(name);

		return Response.ok(product).build();
	}

	/**
	 * Get all products of all providers with medium price per unit.
	 * 
	 * @return List of all products.
	 */
	@GET
	@Path("/products/prices")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductsPrices() {
		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		List<Product> productList = productDaoImpl.getAllPrices();

		return Response.ok(productList).build();
	}
}