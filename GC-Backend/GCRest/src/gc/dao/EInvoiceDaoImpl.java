package gc.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.conn.JDBCConnection;
import gc.db.DBInvoice;
import gc.einvoice.DatiDDTType;
import gc.einvoice.DettaglioLineeType;
import gc.einvoice.DettaglioPagamentoType;
import gc.model.DDT;
import gc.model.EInvoice;
import gc.model.Event;
import gc.model.Invoice;
import gc.model.Order;
import gc.model.Provider;
import gc.model.types.BaseOrder;
import gc.model.types.Scadenza;
import gc.service.EventService;
import gc.service.OrderService;
import gc.utils.Constants;
import gc.utils.Utils;

public class EInvoiceDaoImpl {
	private static final Logger logger = LogManager.getLogger(EInvoiceDaoImpl.class.getName());

	public EInvoice createInvoice(EInvoice einv, Provider provider) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			int id = DBInvoice.insertInvoice(conn, einv, provider.getId());
			einv.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertInvoice: {}", e);
			try {
				conn.rollback();
				throw new IllegalStateException("It's not allowed to reload the same invoice.");
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return einv;
	}

	public List<EInvoice> getInvoices(Provider provider) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<EInvoice> invList = new ArrayList<>();

		try {
			invList = DBInvoice.getInvoices(conn, provider.getId());
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getInvoices: {}", e);
			try {
				conn.rollback();
				throw new IllegalStateException("Error in method getInvoices.");
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return invList;
	}

	public EInvoice getInvoice(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		EInvoice einv = new EInvoice();

		try {
			einv = DBInvoice.getInvoice(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method getInvoice: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return einv;
	}

	public List<DDT> rebuildDDTs(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<DDT> ddts = new ArrayList<>();

		try {
			ddts = DBInvoice.rebuildDDTs(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method rebuildDDTs: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return ddts;
	}

	public List<Order> rebuildOrders(int id) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();
		List<Order> orders = new ArrayList<>();

		try {
			orders = DBInvoice.rebuildOrders(conn, id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method rebuildOrders: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}

		return orders;
	}

	private void addRelation(int invoiceId, int ddtId, int orderId) {
		JDBCConnection jdbcConnection = new JDBCConnection();
		Connection conn = jdbcConnection.getConnnection();

		try {
			DBInvoice.saveInvoice(conn, invoiceId, ddtId, orderId);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method addRelation: {}", e);
			try {
				conn.rollback();
				throw new IllegalStateException("Error in method addRelation.");
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		} finally {
			jdbcConnection.closeConnection(conn);
		}
	}

	public List<Invoice> getOrders(EInvoice einv, List<DDT> ddts) {
		List<Invoice> invList = new ArrayList<>();
		Invoice inv = new Invoice();
		inv.setDataDoc(new java.sql.Date(einv.getInvoiceDate().getTime()));
		inv.setDeadlines(einv.getDeadlines());
		inv.setId(einv.getEinvNumb());
		inv.setDocNum(einv.getEinvNumb());
		inv.setProvider(einv.getProviderName());
		if (!ddts.isEmpty()) {
			inv.setDdts(ddts);
		}
		invList.add(inv);

		ArrayList<Order> orders;

		// Verifica che ddt corrisponde a linea
		boolean hasRif = Utils.containsRifLinea(ddts);

		if (hasRif) {
			Map<String, ArrayList<Order>> map = new HashMap<>();

			for (DDT ddt : ddts) {
				String ddtTitle = Utils.getFancyTitle(ddt);

				List<DettaglioLineeType> detailsLines = einv.getOrders();
				orders = extractOrders(detailsLines, ddt, einv);

				map.put(ddtTitle, orders);
				inv.setDDTOrders(map);
			}
		} else {
			// non c'è associazione tra linea e DDT perciò creo singola fattura
			List<DettaglioLineeType> detailsLines = einv.getOrders();
			orders = extractOrders(detailsLines, null, einv);

			Map<String, ArrayList<Order>> map = new HashMap<>();
			String ddtTitle = "";
			if (!ddts.isEmpty()) {
				Date date = Utils.fromXMLGrToDate(ddts.get(0).getDataDDT());
				ddtTitle = MessageFormat.format("DDT {0} del {1,date,short}", ddts.get(0).getNumeroDDT(), date);
			}
			map.put(ddtTitle, orders);
			inv.setDDTOrders(map);
		}

		return invList;
	}

	public Provider checkProvider(EInvoice einv) {
		Provider provider = new Provider();
		String providerName = einv.getProviderName();
		if (!providerName.isEmpty()) {
			ProviderDaoImpl prImpl = new ProviderDaoImpl();
			if (!Utils.containsProvName(prImpl.getProviders(), providerName)) {
				Provider p = new Provider();
				p.setName(providerName);
				provider = prImpl.insertProvider(p);
			} else {
				provider = prImpl.getProviderDetails(providerName);
			}
		}
		return provider;
	}

	public List<DatiDDTType> getDDTs(EInvoice einv) {
		return einv.getDDTs();
	}

	public List<String> getAttachments(EInvoice einv) {
		return einv.getAttachments();
	}

	public List<Scadenza> getDeadlines(EInvoice einv) {
		String provider = einv.getProviderName();
		// Recupera scadenze e metodi di pagamento
		List<Scadenza> deadlines = einv.getDeadlines();

		// Crea reminder per le scadenze
		if (deadlines != null && !deadlines.isEmpty()) {
			EventService evService = new EventService();
			int counter = 1;
			for (Scadenza deadline : deadlines) {
				try {
					DettaglioPagamentoType paym = deadline.getPaymentDets();
					String type = counter == deadlines.size() ? "Saldo"
							: MessageFormat.format("Rata n. {0},", counter++);
					String title = MessageFormat.format("{0,number} € - {1} fattura n. {2} del {3,date,medium} - {4}",
							paym.getImportoPagamento(), type, deadline.getInvoiceNum(), einv.getInvoiceDate(),
							provider);
					Event tmp = new Event(title);
					tmp.setPaid(false);

					XMLGregorianCalendar deadDate = paym.getDataScadenzaPagamento();
					Date eventDate = deadDate != null ? Utils.fromXMLGrToDate(deadDate) : einv.getInvoiceDate();
					tmp.setStartDate(new java.sql.Date(eventDate.getTime()));
					evService.addEvent(tmp);
				} catch (IOException e) {
					logger.error("Error creating event: {}", e);
				}
			}
		}

		return deadlines;
	}

	private ArrayList<Order> extractOrders(List<DettaglioLineeType> detailsLines, DDT ddt, EInvoice einv) {
		ArrayList<Order> orders = new ArrayList<>();
		OrderService ordService = new OrderService();
		EInvoiceDaoImpl eInvoiceDaoImpl = new EInvoiceDaoImpl();

		Date dateOrd;
		int ddtId = 0;
		if (ddt == null) {
			dateOrd = einv.getInvoiceDate();
		} else {
			dateOrd = Utils.fromXMLGrToDate(ddt.getDataDDT());
			ddtId = ddt.getId();
		}

		long dateOrdMillis = dateOrd.getTime();

		for (DettaglioLineeType line : detailsLines) {
			try {
				BaseOrder ord = new BaseOrder();
				if (line.getPrezzoUnitario().compareTo(BigDecimal.ZERO) != 0
						&& line.getPrezzoTotale().compareTo(BigDecimal.ZERO) != 0 && line.getDescrizione() != null) {

					ord.setName(line.getDescrizione());
					ord.setUm(line.getUnitaMisura());
					ord.setPrice(line.getPrezzoUnitario());
					ord.setNoIvaPrice(line.getPrezzoTotale());
					ord.setQuantity(line.getQuantita());
					ord.setIva(line.getAliquotaIVA());
					ord.setIvaPrice(Utils.addIva(line.getPrezzoTotale(), line.getAliquotaIVA()));
					ord.setDateOrder(new java.sql.Date(dateOrdMillis));
					if (ddtId != 0) {
						ord.setDdtId(ddtId);
					}

					// Quantity can be null
					if (ord.getQuantity() == null) {
						ord.setQuantity(new BigDecimal(1));
					}

					BigDecimal discObjPrice = Utils.divide(ord.getNoIvaPrice(), ord.getQuantity());
					BigDecimal discount = Utils.calcDiscount(ord.getPrice(), discObjPrice);
					ord.setDiscount(discount);

					ord = (BaseOrder) ordService.addOrder(einv.getProviderName(), ord).getEntity();
					orders.add(ord);
					eInvoiceDaoImpl.addRelation(einv.getId(), ddtId, ord.getId());
				} else {
					// TODO capire come gestire queste righe
					logger.error("Riga non valida: {}", line);
				}
			} catch (Exception e) {
				logger.error("Exception during getOrders from EInvoice {}", e);
			}
		}

		return orders;
	}
}
