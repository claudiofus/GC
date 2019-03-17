package gc.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.einvoice.AllegatiType;
import gc.einvoice.AnagraficaType;
import gc.einvoice.CedentePrestatoreType;
import gc.einvoice.DatiDDTType;
import gc.einvoice.DatiGeneraliType;
import gc.einvoice.DatiPagamentoType;
import gc.einvoice.DettaglioLineeType;
import gc.einvoice.DettaglioPagamentoType;
import gc.einvoice.FatturaElettronicaBodyType;
import gc.einvoice.FatturaElettronicaType;
import gc.model.types.Scadenza;
import gc.utils.Base64Coder;
import gc.utils.Utils;

public class EInvoice extends FatturaElettronicaType {
	private static final Logger logger = LogManager
			.getLogger(EInvoice.class.getName());
	private static final String ATTACHMENTS_LOCATION = "D:\\a.pdf";
	
	public String getEinvNumb() {
		if (this.getFatturaElettronicaBody().size() == 1) {
			return this.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
		} else {
			//TODO stabilire il comportamento
			logger.error("Stabilire il comportamento in questo caso");
		}
		return null;
	}
	
	public String getProvider() {
		CedentePrestatoreType fornitore = this.getFatturaElettronicaHeader().getCedentePrestatore();
		AnagraficaType anagProvider = fornitore.getDatiAnagrafici().getAnagrafica();
		if (anagProvider.getDenominazione() == null || anagProvider.getDenominazione().isEmpty()) {
			return anagProvider.getCognome() + " " + anagProvider.getNome();
		} else {
			return anagProvider.getDenominazione();
		}
	}
	
	public List<DatiDDTType> getDDTs() {
		List<FatturaElettronicaBodyType> fatture = this.getFatturaElettronicaBody();
		List<DatiDDTType> ddts = new ArrayList<>();
		DatiGeneraliType dg;
		for (FatturaElettronicaBodyType fatt : fatture) {
			dg = fatt.getDatiGenerali();
			if (dg.getDatiDDT().isEmpty()) {
				logger.info("Data fattura unica: "
						+ dg.getDatiGeneraliDocumento().getData());
			} else {
				ddts = dg.getDatiDDT();
				logger.info("Data ddts: " + ddts);
			}
		}
		return ddts;
	}
	
	public List<DettaglioLineeType> getOrders() {
		List<FatturaElettronicaBodyType> fatture = this.getFatturaElettronicaBody();
		List<DettaglioLineeType> linee = new ArrayList<>();
		fatture.forEach(el -> {
			linee.addAll(el.getDatiBeniServizi().getDettaglioLinee());
		});

		return linee;
	}
	
	public Date getInvoiceDate() {
		List<FatturaElettronicaBodyType> fatture = this.getFatturaElettronicaBody();
		if (fatture.size() > 1) {
			logger.error("Capire come fare in questo caso, più fatture.");
		} else {
			DatiGeneraliType dg = fatture.get(0).getDatiGenerali();
			XMLGregorianCalendar invDate = dg.getDatiGeneraliDocumento().getData();
			return Utils.fromXMLGrToDate(invDate);
		}

		return null;
	}

	public List<String> getAttachments() {
		List<FatturaElettronicaBodyType> fatture = this.getFatturaElettronicaBody();
		List<AllegatiType> attachments = new ArrayList<>();
		for (FatturaElettronicaBodyType fattura : fatture) {
			attachments.addAll(fattura.getAllegati());
		}

		List<String> attachs64List = new ArrayList<>();
		String base64;
		byte[] att, pdfBytes;

		for (AllegatiType attachm : attachments) {
			att = attachm.getAttachment();
			base64 = new String(Base64Coder.encode(att));
			pdfBytes = DatatypeConverter.parseBase64Binary(base64);
			writePDF(pdfBytes);
			attachs64List.add(base64);
		}

		return attachs64List;
	}

	public boolean writePDF(byte[] bytes) {
		try {
			Files.write(Paths.get(ATTACHMENTS_LOCATION), bytes);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Scadenza> getDeadlines() {
		List<FatturaElettronicaBodyType> fatture = this.getFatturaElettronicaBody();
		Map<String, List<DettaglioPagamentoType>> map = new HashMap<>();
		List<Scadenza> deadlines = new ArrayList<>();
		for (FatturaElettronicaBodyType fattura : fatture) {
			for (DatiPagamentoType dpt : fattura.getDatiPagamento()) {
				map.put(fattura.getDatiGenerali().getDatiGeneraliDocumento()
						.getNumero(), dpt.getDettaglioPagamento());
			}
		}

		map.forEach((invoiceNum, paymentDets) -> {
			paymentDets.forEach(dpt -> {
				Scadenza deadln = new Scadenza();
				deadln.setInvoiceNum(invoiceNum);
				deadln.setPaymentDets(dpt);
				deadlines.add(deadln);
			});
		});

		return deadlines;
	}
}
