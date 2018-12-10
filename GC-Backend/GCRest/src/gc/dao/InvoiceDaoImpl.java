package gc.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import gc.conn.JDBCConnection;
import gc.factory.OrderFactory;
import gc.model.Event;
import gc.model.Invoice;
import gc.model.Order;
import gc.model.types.BaseOrder;
import gc.model.types.Deadline;
import gc.service.EventService;
import gc.utils.Utils;

public class InvoiceDaoImpl {
	private static final Logger logger = LogManager.getLogger(InvoiceDaoImpl.class.getName());

	public List<Invoice> addInvoice(String provider, File file) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Invoice> invList = new ArrayList<Invoice>();
		PDDocument document = null;
		try {
			OrderFactory ordFact = new OrderFactory();
			BaseOrder ord = ordFact.getBaseOrder(provider);
			LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
			String inv_number = null;
			java.sql.Date inv_date = null;
			List<Deadline> deadlines = null;
			Invoice inv = new Invoice();

			document = PDDocument.load(file);
			for (int page = 0; page < document.getNumberOfPages(); page++) {
				inv_number = ord.getNumber(document, page).replaceAll("\\s+", "");

				if (inv_number != null && !inv_number.equalsIgnoreCase(inv.getNum_doc())) {
					inv = new Invoice();
					inv.setNum_doc(inv_number);
					map = new LinkedMap<>();
					invList.add(inv);
				}

				map = ord.parseOrder(document, conn, page, map);
				inv_date = ord.getDate(document, page);
				deadlines = ord.getDeadlines(document, page);
				inv.setData_doc(inv_date);
				inv.setScadenze(deadlines);
				inv.setDDTOrders(map);

				if (deadlines != null && deadlines.size() > 0) {
					EventService evService = new EventService();
					for (int i = 0; i < deadlines.size(); i++) {
						String title = deadlines.get(i).getAmount() + "€ - rata n." + (i + 1) + ", fattura n."
								+ inv.getNum_doc() + " del " + Utils.sqlDateToDate(inv.getData_doc(), "dd/MM/yyyy")
								+ " - " + provider;
						Event tmp = new Event(title);
						tmp.setPaid(false);
						tmp.setStart_date(deadlines.get(i).getDeadlineDate());
						evService.addEvent(tmp);
					}
				}
			}

			return invList;
		} catch (IOException e) {
			logger.error("Error in method addInvoice: ", e);
		} finally {
			try {
				document.close();
				conn.close();
			} catch (SQLException | IOException e) {
				logger.error("Error document or connection close: ", e);
			}
		}
		return null;
	}
}