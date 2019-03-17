package gc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gc.model.types.Scadenza;

@JsonDeserialize(as = Invoice.class)
public class Invoice {
	String id;
	String doc_num;
	String provider;
	java.sql.Date data_doc;

	/**
	 * String = Descrizione - DDT ArrayList<Order> = Ordini del DDT
	 */
	Map<String, ArrayList<Order>> DDTOrders;
	List<Scadenza> deadlines;
	List<String> attachments;

	public Invoice() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getDoc_num() {
		return doc_num;
	}

	public void setDoc_num(String doc_num) {
		this.doc_num = doc_num;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public java.sql.Date getData_doc() {
		return data_doc;
	}

	public void setData_doc(java.sql.Date data_doc) {
		this.data_doc = data_doc;
	}

	public Map<String, ArrayList<Order>> getDDTOrders() {
		return DDTOrders;
	}

	public void setDDTOrders(Map<String, ArrayList<Order>> dDTOrders) {
		DDTOrders = dDTOrders;
	}

	public List<Scadenza> getDeadlines() {
		return deadlines;
	}

	public void setDeadlines(List<Scadenza> deadlines) {
		this.deadlines = deadlines;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}
}
