package gc.model.types;

import gc.einvoice.DettaglioPagamentoType;

public class Scadenza {
	public String invoiceNum;
	public DettaglioPagamentoType paymentDets;

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public DettaglioPagamentoType getPaymentDets() {
		return paymentDets;
	}

	public void setPaymentDets(DettaglioPagamentoType paymentDets) {
		this.paymentDets = paymentDets;
	}
}