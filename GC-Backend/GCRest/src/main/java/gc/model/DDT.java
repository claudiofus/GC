package gc.model;

import java.util.List;

import gc.einvoice.DatiDDTType;

public class DDT extends DatiDDTType {
	private int id;
	private int invoiceId;
	private int providerId;

	public DDT() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setNumeroRiferimentoLinea(List<Integer> list) {
		this.riferimentoNumeroLinea = list;
	}
}
