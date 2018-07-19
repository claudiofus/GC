package gc.service;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

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
	public String getProducts() {
		String productDetails = null;
		List<Product> productList = null;

		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		productList = productDaoImpl.getProducts();

		Gson gson = new Gson();
		productDetails = gson.toJson(productList);
		return productDetails;
	}

	/**
	 * Get the product with code passe in input.
	 * 
	 * @param name
	 *            of the product to find.
	 * @return Product with name specified.
	 */
	@GET
	@Path("/price/{name : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProduct(@PathParam("name") String name) {
		String productDetails = null;
		List<HashMap<String, Object>> product = null;

		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		product = productDaoImpl.getProductDetails(name);

		Gson gson = new Gson();
		productDetails = gson.toJson(product);
		return productDetails;
	}

	/**
	 * Get all products of all providers with medium price per unit.
	 * 
	 * @return List of all products.
	 */
	@GET
	@Path("/products/prices")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProductsPrices() {
		String productDetails = null;
		List<Product> productList = null;

		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
		productList = productDaoImpl.getProductsPrices();

		Gson gson = new Gson();
		productDetails = gson.toJson(productList);
		return productDetails;
	}

//	@POST
//	@Path("/insertProduct")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response insertProduct(InputStream incomingData) {
//		StringBuilder strBuilder = new StringBuilder();
//		try {
//			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				strBuilder.append(line);
//			}
//		} catch (Exception e) {
//			System.out.println("Error Parsing: - ");
//		}
//		System.out.println("Data Received: " + strBuilder.toString());
//		Gson gson = new Gson();
//		Product product = gson.fromJson(strBuilder.toString(), Product.class);
//
//		List<Product> productList = null;
//
//		ProductDaoImpl productDaoImpl = new ProductDaoImpl();
//		productList = productDaoImpl.getProducts();
//
//		for (Product prd : productList) {
//			if (prd.getCode().equalsIgnoreCase(product.getCode())) {
//				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//						.entity("{\"error\": \"Product already exists for code: " + product.getCode() + "\"}").build();
//			}
//		}
//
//		productDaoImpl.insertProduct(product.getCode(), product.getName());
//
//		return Response.ok(gson.toJson(product)).build();
//	}
}