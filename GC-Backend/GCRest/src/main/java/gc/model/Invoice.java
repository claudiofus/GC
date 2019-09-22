package gc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gc.model.types.Scadenza;

@JsonDeserialize(as = Invoice.class)
public class Invoice {
	String id;
	String docNum;
	String provider;
	java.sql.Date dataDoc;

	Map<String, ArrayList<Order>> DDTOrders;
	List<DDT> ddts;
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

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public java.sql.Date getDataDoc() {
		return dataDoc;
	}

	public void setDataDoc(java.sql.Date dataDoc) {
		this.dataDoc = dataDoc;
	}

	public Map<String, ArrayList<Order>> getDDTOrders() {
		return DDTOrders;
	}

	public void setDDTOrders(Map<String, ArrayList<Order>> map) {
		DDTOrders = map;
	}

	public List<DDT> getDdts() {
		return ddts;
	}

	public void setDdts(List<DDT> ddts) {
		this.ddts = ddts;
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
