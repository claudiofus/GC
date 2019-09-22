package gc.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBProvider;
import gc.model.Provider;
import gc.utils.Constants;

public class ProviderDaoImpl implements Dao<Provider> {
	private static final Logger logger = LogManager.getLogger(ProviderDaoImpl.class.getName());

	@Override
	public Provider get(int id) {
		Provider productData = DBProvider.findProviderById(conn, id);
		return productData;
	}

	@Override
	public List<Provider> getAll() {
		List<Provider> productData = DBProvider.queryProvider(conn);
		return productData;
	}

	@Override
	public void save(Provider provider) {
		try {
			int id = DBProvider.insertProvider(conn, provider);
			provider.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertProvider: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}

	@Override
	public void update(Provider t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Provider t) {
		// TODO Auto-generated method stub
		
	}
	
	public Provider getProviderDetails(String name) {
		Provider productData = DBProvider.findProvider(conn, name);
		return productData;
	}
}