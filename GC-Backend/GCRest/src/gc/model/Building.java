package gc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import gc.model.types.Address;
import gc.model.types.ProjectCost;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Building {
	@XmlElement
	private int id;
	@XmlElement
	private String name;
	@XmlElement
	private boolean open;
	private java.sql.Date start_date;
	private java.sql.Date end_date;
	@XmlElement
	private Address address;
	@XmlElement
	private Float req_amount;
	private ProjectCost prj_cost;

	public Building() {
		super();
	}

	public Building(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public java.sql.Date getStart_date() {
		return new java.sql.Date(start_date.getTime());
	}

	public void setStart_date(java.sql.Date start_date) {
		this.start_date = new java.sql.Date(start_date.getTime());
	}

	public java.sql.Date getEnd_date() {
		return new java.sql.Date(end_date.getTime());
	}

	public void setEnd_date(java.sql.Date end_date) {
		this.end_date = new java.sql.Date(end_date.getTime());
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Float getReq_amount() {
		return req_amount;
	}

	public void setReq_amount(Float req_amount) {
		this.req_amount = req_amount;
	}

	public ProjectCost getPrj_cost() {
		return prj_cost;
	}

	public void setPrj_cost(ProjectCost prj_cost) {
		this.prj_cost = prj_cost;
	}
}