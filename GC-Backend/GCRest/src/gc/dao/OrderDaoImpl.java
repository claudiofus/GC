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
import org.apache.pdfbox.pdmodel.PDDocument;

import gc.conn.JDBCConnection;
import gc.fornitori.Akifix;
import gc.fornitori.AutofficinaLippolis;
import gc.fornitori.FinishVillage;
import gc.fornitori.Intermobil;
import gc.fornitori.Mag;
import gc.fornitori.Montone;
import gc.fornitori.ResinaColor;
import gc.model.Order;
import gc.model.UM;
import gc.utils.DBUtils;

public class OrderDaoImpl {

	public Map<String, ArrayList<Order>> addOrder(String provider, File file) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
			PDDocument document = PDDocument.load(file);
			for (int page = 0; page < document.getNumberOfPages(); page++) {
				switch (provider) {
					case "montone" :
						map = new Montone().parseOrder(document, conn, page,
								map);
						break;
					case "mag" :
						map = new Mag().parseOrder(document, conn, page,
								map);
						break;
					case "resinaColor" :
						map = new ResinaColor().parseOrder(document, conn,
								page,
								map);
						break;
					case "intermobil" :
						map = new Intermobil().parseOrder(document, conn, page,
								map);
						break;
					case "finishVillage" :
						map = new FinishVillage().parseOrder(document, conn,
								page,
								map);
						break;
					case "autoffLippolis" :
						map = new AutofficinaLippolis().parseOrder(document,
								conn, page,
								map);
						break;
					case "akifix" :
						map = new Akifix().parseOrder(document, conn, page,
								map);
						break;
				}
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
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
			DBUtils.insertOrdine(conn, ord, true);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return ord;
	}

	public List<Order> getOrders(String buildingName) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Order> orderData = new ArrayList<>();

		try {
			orderData = DBUtils.findOrder(conn, buildingName);
		} catch (SQLException e) {
			e.printStackTrace();
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

			DBUtils.updateOrdine(conn, updOrd);
			conn.commit();
			updOrd = DBUtils.selectOrdine(conn, updOrd);
			conn.commit();
		} catch (Exception e) {
			System.err.println("Error during update order");
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}

		return updOrd;
	}

	public List<UM> getUMs() {
		List<UM> UMList = Arrays.asList(UM.values());
		return UMList;
	}
}