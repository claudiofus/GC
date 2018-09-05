package gc.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gc.conn.JDBCConnection;
import gc.factory.OrderFactory;
import gc.model.Event;
import gc.model.Invoice;
import gc.model.Order;
import gc.model.types.BaseOrder;
import gc.model.types.Scadenza;
import gc.service.EventService;
import gc.utils.Utils;

public class InvoiceDaoImpl {

	public Invoice addInvoice(String provider, File file) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			OrderFactory ordFact = new OrderFactory();
			BaseOrder ord = ordFact.getBaseOrder(provider);
			Map<String, ArrayList<Order>> map = ord.parseOrder(file, conn);
			String inv_number = ord.getNumber(file).replaceAll("\\s+", "");
			java.sql.Date inv_date = ord.getDate(file);
			List<Scadenza> deadlines = ord.getDeadlines(file);

			Invoice inv = new Invoice();
			if (map != null && !map.isEmpty()) {
				inv.setDDTOrders(map);
			}
			inv.setData_doc(inv_date);
			inv.setNum_doc(inv_number);
			inv.setScadenze(deadlines);

			if (deadlines != null && deadlines.size() > 0) {
				EventService evService = new EventService();
				for (int i = 0; i < deadlines.size(); i++) {
					String title = deadlines.get(i).getAmount() + "€ - rata n."
							+ (i + 1) + ", fattura n." + inv.getNum_doc()
							+ " del " + Utils.sqlDateToDate(inv.getData_doc(), "dd/MM/yyyy") + " - " + provider;
					Event tmp = new Event(title);
					tmp.setPaid(false);
					tmp.setStart_date(deadlines.get(i).getDeadlineDate());
					evService.addEvent(tmp);
				}
			}

			return inv;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}