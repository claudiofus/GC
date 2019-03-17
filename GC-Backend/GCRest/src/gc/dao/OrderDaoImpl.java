package gc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBOrder;
import gc.model.Order;
import gc.model.UM;
import gc.model.types.BaseOrder;
import gc.utils.Utils;

public class OrderDaoImpl {
	private static final Logger logger = LogManager
			.getLogger(OrderDaoImpl.class.getName());

	public Order insertOrder(Order ord) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBOrder.insertOrdine(conn, ord, ord.getBuilding_id() != null);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertOrder: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return ord;
	}

	public List<Order> getOrders(String buildingName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Order> orderData = new ArrayList<>();

		try {
			orderData = DBOrder.findOrder(conn, buildingName);
		} catch (SQLException e) {
			logger.error("Error in method getOrders: ", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}

		return orderData;
	}

	public Order updateOrder(Order ord) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Order updOrd = null;

		try {
			updOrd = new BaseOrder();
			updOrd.setId(ord.getId());
			updOrd.setBuilding_id(ord.getBuilding_id());
			updOrd.setName(ord.getName());
			updOrd.setUm(ord.getUm());
			updOrd.setQuantity(ord.getQuantity());
			updOrd.setPrice(ord.getPrice());
			updOrd.setDiscount(ord.getDiscount());
			updOrd.setNoIvaPrice(ord.getNoIvaPrice());
			updOrd.setIva(ord.getIva());
			updOrd.setIvaPrice(Utils.addIva(ord.getNoIvaPrice(), ord.getIva()));
			updOrd.setDate_order(ord.getDate_order());

			DBOrder.updateOrdine(conn, updOrd);
			conn.commit();
			updOrd = DBOrder.selectOrdine(conn, updOrd);
			conn.commit();
		} catch (Exception e) {
			logger.error("Error during update order", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error in closing connection: ", e);
				}
			}
		}
		
		return updOrd;
	}

	public List<UM> getUMs() {
		List<UM> UMList = Arrays.asList(UM.values());
		return UMList;
	}
}