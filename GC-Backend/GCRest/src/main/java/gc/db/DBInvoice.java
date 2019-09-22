package gc.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.model.DDT;
import gc.model.EInvoice;
import gc.model.Order;
import gc.model.Provider;
import gc.model.types.BaseOrder;
import gc.utils.Utils;

public class DBInvoice {
	private static final Logger logger = LogManager.getLogger(DBInvoice.class.getName());

	private DBInvoice() {
		throw new IllegalStateException("DBInvoice class");
	}

	public static int insertInvoice(Connection conn, EInvoice einv, int providerId) {
		String sql = "INSERT INTO gestione_cantieri.invoice (number, provider_id, date) VALUES (?, ?, ?)";
		Date einvDate = new java.sql.Date(einv.getInvoiceDate().getTime());

		int dbInvIdx = 0;
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setString(1, einv.getEinvNumb());
			pstm.setInt(2, providerId);
			pstm.setDate(3, einvDate);

			String query = pstm.toString();
			logger.info("insertInvoice: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserting invoice failed, no rows affected.");
			}

			int tempId = Utils.getIdFromQuery(pstm);
			if (tempId != 0) {
				dbInvIdx = tempId;
			}
		} catch (SQLException e) {
			logger.error("Error in method insertInvoice: {}", e);
			throw new IllegalStateException("It's not allowed to reload the same invoice.");
		}

		return dbInvIdx;
	}

	public static List<EInvoice> getInvoices(Connection conn, int providerId) {
		String sql = "SELECT id, number, provider_id, date FROM gestione_cantieri.invoice WHERE provider_id = ?";

		List<EInvoice> list = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setInt(1, providerId);

			String query = pstm.toString();
			logger.info("getInvoices: {}", query);

			list = getEinvoiceList(pstm);
		} catch (SQLException e) {
			logger.error("Error in method getInvoices: {}", e);
		}

		return list;
	}

	public static EInvoice getInvoice(Connection conn, int id) {
		String sql = "SELECT id, number, provider_id, date FROM gestione_cantieri.invoice WHERE id = ?";

		EInvoice einv = new EInvoice();
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("getInvoice: {}", query);

			List<EInvoice> einvoiceList = getEinvoiceList(pstm);
			if (!einvoiceList.isEmpty()) {
				einv = einvoiceList.get(0);
			}
		} catch (SQLException e) {
			logger.error("Error in method getInvoice: {}", e);
		}

		return einv;
	}

	public static List<DDT> rebuildDDTs(Connection conn, int id) {
		String sql = "SELECT ddt.id, provider_id, number, date FROM gestione_cantieri.inv_ddt_order inv INNER JOIN gestione_cantieri.ddt ddt "
				+ "ON inv.ddt_id = ddt.id WHERE inv.invoice_id = ? GROUP BY ddt.id";

		List<DDT> ddts = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("rebuildDDTs: {}", query);

			ddts = getDDTOrder(pstm);
		} catch (SQLException e) {
			logger.error("Error in method rebuildDDTs: {}", e);
		}

		return ddts;
	}

	public static List<Order> rebuildOrders(Connection conn, int id) {
		String sql = "SELECT order_id FROM gestione_cantieri.inv_ddt_order WHERE invoice_id = ?";

		List<Order> orders = new ArrayList<>();
		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setInt(1, id);

			String query = pstm.toString();
			logger.info("rebuildOrders: {}", query);

			orders = getOrderId(pstm);
		} catch (SQLException e) {
			logger.error("Error in method rebuildOrders: {}", e);
		}

		return orders;
	}

	public static void saveInvoice(Connection conn, int invoiceId, int ddtId, int orderId) {
		String sql = "INSERT INTO gestione_cantieri.inv_ddt_order (invoice_id, order_id, ddt_id) VALUES (?, ?, ?)";

		try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstm.setInt(1, invoiceId);
			pstm.setInt(2, orderId);
			if (ddtId != 0) {
				pstm.setInt(3, ddtId);
			} else {
				pstm.setNull(3, Types.INTEGER);
			}

			String query = pstm.toString();
			logger.info("saveInvoice: {}", query);

			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Saving invoice relations failed, no rows affected.");
			}
		} catch (SQLException e) {
			logger.error("Error in method saveInvoice: {}", e);
		}
	}

	private static List<EInvoice> getEinvoiceList(PreparedStatement pstm) {
		List<EInvoice> einvoiceList = new ArrayList<>();
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				int einvId = rs.getInt("id");
				String einvNumb = rs.getString("number");
				int providerId = rs.getInt("provider_id");
				Date einvDate = rs.getDate("date");
				EInvoice einv = new EInvoice();
				einv.setNumber(einvNumb);
				einv.setDate(einvDate);
				einv.setId(einvId);
				Provider p = new Provider();
				p.setId(providerId);
				einv.setProvider(p);
				einvoiceList.add(einv);
			}
		} catch (SQLException e) {
			logger.error("Error in method getInvoices: {}", e);
		}
		return einvoiceList;
	}

	private static List<DDT> getDDTOrder(PreparedStatement pstm) {
		List<DDT> ddts = new ArrayList<>();
		DDT ddt;
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				ddt = new DDT();
				ddt.setId(rs.getInt("ddt.id"));
				ddt.setProviderId(rs.getInt("provider_id"));
				ddt.setNumeroDDT(rs.getString("number"));
				ddt.setDataDDT(Utils.asXMLGregorianCalendar(rs.getDate("date")));
				ddts.add(ddt);
			}
		} catch (SQLException e) {
			logger.error("Error in method rebuildDDTs: {}", e);
		}

		return ddts;
	}

	private static List<Order> getOrderId(PreparedStatement pstm) {
		List<Order> list = new ArrayList<>();
		Order ord;
		try (ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				ord = new BaseOrder();
				ord.setId(rs.getInt("order_id"));
				list.add(ord);
			}
		} catch (SQLException e) {
			logger.error("Error in method rebuildOrders: {}", e);
		}

		return list;
	}
}
