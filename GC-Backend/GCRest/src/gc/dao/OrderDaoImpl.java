package gc.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import gc.conn.JDBCConnection;
import gc.db.DBOrder;
import gc.fornitori.Akifix;
import gc.fornitori.AutofficinaLippolis;
import gc.fornitori.FinishVillage;
import gc.fornitori.Intermobil;
import gc.fornitori.Mag;
import gc.fornitori.Montone;
import gc.fornitori.ResinaColor;
import gc.model.Order;
import gc.model.UM;

public class OrderDaoImpl {
	private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class.getName());

	public Map<String, ArrayList<Order>> addOrder(String provider, File file) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
			PDDocument document = PDDocument.load(file);
			for (int page = 0; page < document.getNumberOfPages(); page++) {
				switch (provider) {
				case "montone":
					map = new Montone().parseOrder(document, conn, page, map);
					break;
				case "mag":
					map = new Mag().parseOrder(document, conn, page, map);
					break;
				case "resinaColor":
					map = new ResinaColor().parseOrder(document, conn, page, map);
					break;
				case "intermobil":
					map = new Intermobil().parseOrder(document, conn, page, map);
					break;
				case "finishVillage":
					map = new FinishVillage().parseOrder(document, conn, page, map);
					break;
				case "autoffLippolis":
					map = new AutofficinaLippolis().parseOrder(document, conn, page, map);
					break;
				case "akifix":
					map = new Akifix().parseOrder(document, conn, page, map);
					break;
				}
			}
			return map;
		} catch (IOException e) {
			logger.error("Error in method addOrder: ", e);
			return null;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	public Order insertOrder(Order ord) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBOrder.insertOrdine(conn, ord, true);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertOrder: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Error in connection rollback: ", e1);
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
		}

		return orderData;
	}

	public Order updateOrder(Order ord) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		Order updOrd = null;

		try {
			updOrd = ord.getClass().getConstructor().newInstance();
			updOrd.setId(ord.getId());
			updOrd.setBuilding_id(ord.getBuilding_id());
			updOrd.setCode(ord.getCode());
			updOrd.setName(ord.getName());
			updOrd.setUm(ord.getUm());
			updOrd.setQuantity(ord.getQuantity());
			updOrd.setPrice(ord.getPrice());
			updOrd.setDiscount(ord.getDiscount());
			updOrd.setAdj_price(ord.getAdj_price());
			updOrd.setIva(ord.getIva());
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
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Error in connection close: ", e);
			}
		}

		return updOrd;
	}

	public List<UM> getUMs() {
		List<UM> UMList = Arrays.asList(UM.values());
		return UMList;
	}
}