package gc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gc.model.types.Deadline;

@JsonDeserialize(as = Invoice.class)
public class Invoice {
	int id;
	String num_doc;
	java.sql.Date data_doc;

	/**
	 * String = Descrizione - DDT ArrayList<Order> = Ordini del DDT
	 */
	Map<String, ArrayList<Order>> DDTOrders;
	List<Deadline> scadenze;

	public Invoice() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(String num_doc) {
		this.num_doc = num_doc;
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

	public List<Deadline> getScadenze() {
		return scadenze;
	}

	public void setScadenze(List<Deadline> scadenze) {
		this.scadenze = scadenze;
	}
}
