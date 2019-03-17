package gc.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.einvoice.DatiDDTType;
import gc.einvoice.DettaglioLineeType;
import gc.einvoice.DettaglioPagamentoType;
import gc.model.EInvoice;
import gc.model.Event;
import gc.model.Invoice;
import gc.model.Order;
import gc.model.Provider;
import gc.model.types.BaseOrder;
import gc.model.types.Scadenza;
import gc.service.EventService;
import gc.service.OrderService;
import gc.utils.Utils;

public class EInvoiceDaoImpl {
	private static final Logger logger = LogManager
			.getLogger(EInvoiceDaoImpl.class.getName());

	public List<Invoice> getOrders(EInvoice einv) {
		List<Invoice> invList = new ArrayList<Invoice>();
		Invoice inv = new Invoice();
		inv.setData_doc(new java.sql.Date(einv.getInvoiceDate().getTime()));
		inv.setDeadlines(einv.getDeadlines());
		inv.setId(einv.getEinvNumb());
		inv.setDoc_num(einv.getEinvNumb());
		inv.setProvider(einv.getProvider());
		invList.add(inv);

		checkProvider(einv);

		ArrayList<Order> orders = new ArrayList<>();
		List<DatiDDTType> ddts = getDDTs(einv);

		// Verifica che ddt corrisponde a linea
		boolean hasRif = Utils.containsRifLinea(ddts);

		if (hasRif) {
			Map<String, ArrayList<Order>> map = new HashMap<>();

			for (DatiDDTType ddt : ddts) {
				Date date = Utils.fromXMLGrToDate(ddt.getDataDDT());
				String ddtTitle = "DDT " + ddt.getNumeroDDT() + " del "
						+ Utils.getStringFromDate(date, false);
				map.put(ddtTitle, orders);

				List<DettaglioLineeType> detailsLines = einv.getOrders();
				detailsLines.forEach(line -> {
					try {
						BaseOrder ord = new BaseOrder();
						if (line.getPrezzoUnitario()
								.compareTo(BigDecimal.ZERO) != 0
								&& line.getPrezzoTotale()
										.compareTo(BigDecimal.ZERO) != 0
								&& line.getDescrizione() != null) {
							long dateOrdMillis = date.getTime();

							ord.setName(line.getDescrizione());
							ord.setUm(line.getUnitaMisura());
							ord.setPrice(line.getPrezzoUnitario());
							ord.setNoIvaPrice(line.getPrezzoTotale());
							ord.setQuantity(line.getQuantita());
							ord.setIva(line.getAliquotaIVA());
							ord.setIvaPrice(Utils.addIva(line.getPrezzoTotale(),
									line.getAliquotaIVA()));
							ord.setDate_order(new java.sql.Date(dateOrdMillis));

							// Quantity can be null
							if (ord.getQuantity() == null) {
								ord.setQuantity(new BigDecimal(1));
							}

							BigDecimal discObjPrice = Utils.divide(ord.getNoIvaPrice(), ord.getQuantity());
							BigDecimal discount = Utils.calcDiscount(ord.getPrice(), discObjPrice);
							ord.setDiscount(discount);

							orders.add(ord);
							OrderService ordService = new OrderService();
							ordService.addOrder(einv.getProvider(), ord);
						} else {
							// TODO capire come gestire queste righe
							logger.error("Riga non valida: " + line);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

				inv.setDDTOrders(map);
			}
		} else {
			Date invoiceDate = einv.getInvoiceDate();
			// non c'è associazione tra linea e DDT perciò creo singola fattura
			List<DettaglioLineeType> detailsLines = einv.getOrders();
			detailsLines.forEach(line -> {
				try {
					BaseOrder ord = new BaseOrder();
					if (line.getPrezzoUnitario().compareTo(BigDecimal.ZERO) != 0
							&& line.getPrezzoTotale()
									.compareTo(BigDecimal.ZERO) != 0
							&& line.getDescrizione() != null) {
						long dateOrdMillis = invoiceDate.getTime();

						ord.setName(line.getDescrizione());
						ord.setUm(line.getUnitaMisura());
						ord.setPrice(line.getPrezzoUnitario());
						ord.setNoIvaPrice(line.getPrezzoTotale());
						ord.setQuantity(line.getQuantita());
						ord.setIva(line.getAliquotaIVA());
						ord.setIvaPrice(Utils.addIva(line.getPrezzoTotale(),
								line.getAliquotaIVA()));
						ord.setDate_order(new java.sql.Date(dateOrdMillis));

						// Quantity can be null
						if (ord.getQuantity() == null) {
							ord.setQuantity(new BigDecimal(1));
						}

						BigDecimal discObjPrice = Utils.divide(ord.getNoIvaPrice(), ord.getQuantity());
						BigDecimal discount = Utils.calcDiscount(ord.getPrice(), discObjPrice);
						ord.setDiscount(discount);
						
						orders.add(ord);
						OrderService ordService = new OrderService();
						ordService.addOrder(einv.getProvider(), ord);
					} else {
						// TODO capire come gestire queste righe
						logger.error("Riga non valida");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			Map<String, ArrayList<Order>> map = new HashMap<>();
			String ddtTitle = "";
			if (!ddts.isEmpty()) {
				Date date = Utils.fromXMLGrToDate(ddts.get(0).getDataDDT());
				ddtTitle = "DDT " + ddts.get(0).getNumeroDDT() + " del "
						+ Utils.getStringFromDate(date, false);
			}
			map.put(ddtTitle, orders);
			inv.setDDTOrders(map);
		}

		return invList;
	}

	public void checkProvider(EInvoice einv) {
		String provider = einv.getProvider();
		if (!provider.isEmpty()) {
			ProviderDaoImpl prImpl = new ProviderDaoImpl();
			if (!Utils.containsProvName(prImpl.getProviders(), provider)) {
				Provider p = new Provider();
				p.setName(provider);
				prImpl.insertProvider(p);
			}
		}
	}

	public List<DatiDDTType> getDDTs(EInvoice einv) {
		return einv.getDDTs();
	}

	public List<String> getAttachments(EInvoice einv) {
		return einv.getAttachments();
	}

	public List<Scadenza> getDeadlines(EInvoice einv) {
		String provider = einv.getProvider();
		// Recupera scadenze e metodi di pagamento
		List<Scadenza> deadlines = einv.getDeadlines();

		// Crea reminder per le scadenze
		if (deadlines != null && deadlines.size() > 0) {
			EventService evService = new EventService();
			int counter = 1;
			for (Scadenza deadline : deadlines) {
				try {
					DettaglioPagamentoType paym = deadline.getPaymentDets();
					String type = counter == deadlines.size()
							? "Saldo"
							: MessageFormat.format("Rata n. {0},", counter++);
					String title = MessageFormat.format(
							"{0,number} € - {1} fattura n. {2} del {3,date,medium} - {4}",
							paym.getImportoPagamento(), type,
							deadline.getInvoiceNum(), einv.getInvoiceDate(),
							provider);
					Event tmp = new Event(title);
					tmp.setPaid(false);

					XMLGregorianCalendar deadDate = paym
							.getDataScadenzaPagamento();
					Date eventDate = deadDate != null
							? Utils.fromXMLGrToDate(deadDate)
							: einv.getInvoiceDate();
					tmp.setStart_date(new java.sql.Date(eventDate.getTime()));
					evService.addEvent(tmp);
				} catch (IOException e) {
					logger.error("Error creating event: ", e);
				}
			}
		}

		return deadlines;
	}
}
