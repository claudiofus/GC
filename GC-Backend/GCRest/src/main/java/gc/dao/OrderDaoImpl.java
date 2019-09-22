package gc.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBOrder;
import gc.model.Order;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.utils.Constants;
import gc.utils.Utils;

public class OrderDaoImpl implements Dao<Order> {
	private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class.getName());

	@Override
	public Order get(int id) {
		Order ord = new BaseOrder();
		ord.setId(id);
		ord = DBOrder.selectOrdine(conn, ord);

		return ord;
	}

	@Override
	public List<Order> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Order order) {

		try {
			int id = DBOrder.insertOrdine(conn, order, order.getBuildingId() != null);
			order.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertOrder: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}		
	}

	@Override
	public void update(Order order, String[] params) {
		Order updOrd = null;

		try {
			updOrd = new BaseOrder();
			updOrd.setId(order.getId());
			updOrd.setDdtId(order.getDdtId());
			updOrd.setBuildingId(order.getBuildingId());
			updOrd.setName(order.getName());
			updOrd.setUm(order.getUm());
			updOrd.setQuantity(order.getQuantity());
			updOrd.setPrice(order.getPrice());
			updOrd.setDiscount(order.getDiscount());
			updOrd.setNoIvaPrice(order.getNoIvaPrice());
			updOrd.setIva(order.getIva());
			updOrd.setIvaPrice(Utils.addIva(order.getNoIvaPrice(), order.getIva()));
			updOrd.setDateOrder(order.getDateOrder());

			DBOrder.updateOrdine(conn, updOrd);
			conn.commit();
		} catch (Exception e) {
			logger.error("Error during update order", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}		
	}

	@Override
	public void delete(Order t) {
		// TODO Auto-generated method stub
		
	}

	public List<Order> getAll(int buildingId) {
		List<Order> orderData = DBOrder.findOrder(conn, buildingId);
		return orderData;
	}
	
	public List<UM> getUMs() {
		return Arrays.asList(UM.values());
	}
}