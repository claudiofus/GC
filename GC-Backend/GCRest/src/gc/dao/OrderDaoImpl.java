package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBOrder;
import gc.model.Order;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.utils.Constants;
import gc.utils.Utils;

public class OrderDaoImpl {
	private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class.getName());

	public Order insertOrder(Order ord) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			int id = DBOrder.insertOrdine(conn, ord, ord.getBuildingId() != null);
			ord.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertOrder: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return ord;
	}

	public Order getOrder(int orderId) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Order ord = new BaseOrder();
		ord.setId(orderId);
		ord = DBOrder.selectOrdine(conn, ord);
		jdbcConnection.closeConnection(conn);

		return ord;
	}

	public List<Order> getOrders(String buildingName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Order> orderData = DBOrder.findOrder(conn, buildingName);
		jdbcConnection.closeConnection(conn);

		return orderData;
	}

	public Order updateOrder(Order ord) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Order updOrd = null;

		try {
			updOrd = new BaseOrder();
			updOrd.setId(ord.getId());
			updOrd.setDdtId(ord.getDdtId());
			updOrd.setBuildingId(ord.getBuildingId());
			updOrd.setName(ord.getName());
			updOrd.setUm(ord.getUm());
			updOrd.setQuantity(ord.getQuantity());
			updOrd.setPrice(ord.getPrice());
			updOrd.setDiscount(ord.getDiscount());
			updOrd.setNoIvaPrice(ord.getNoIvaPrice());
			updOrd.setIva(ord.getIva());
			updOrd.setIvaPrice(Utils.addIva(ord.getNoIvaPrice(), ord.getIva()));
			updOrd.setDateOrder(ord.getDateOrder());

			DBOrder.updateOrdine(conn, updOrd);
			conn.commit();
			updOrd = DBOrder.selectOrdine(conn, updOrd);
			conn.commit();
		} catch (Exception e) {
			logger.error("Error during update order", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return updOrd;
	}

	public List<UM> getUMs() {
		return Arrays.asList(UM.values());
	}
}