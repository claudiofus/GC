package gc.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBOrder;
import gc.db.DBProduct;
import gc.model.Product;

public class ProductDaoImpl implements Dao<Product> {
	private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class.getName());

	@Override
	public Product get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getAll() {
		List<Product> productData = DBProduct.queryProduct(conn);
		return productData;
	}

	@Override
	public void save(Product prd) {
		try {
			DBProduct.insertProduct(conn, prd);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertProduct: {}", e);
		}
	}

	@Override
	public void update(Product t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Product t) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Product> getAllPrices() {
		List<Product> productData = DBOrder.queryProductPrice(conn);
		return productData;
	}
	
	public List<HashMap<String, Object>> getProductDetails(String prdName) {
		List<HashMap<String, Object>> productData = DBOrder.queryPricesHistory(conn, prdName);
		return productData;
	}
}