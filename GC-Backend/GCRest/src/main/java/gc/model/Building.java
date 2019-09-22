package gc.model;

import gc.model.types.Address;
import gc.model.types.ProjectCost;

public class Building {
	private int id;
	private String name;
	private boolean open;
	private java.sql.Date startDate;
	private java.sql.Date endDate;
	private Address address;
	private Float reqAmount;
	private ProjectCost prjCost;

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

	public java.sql.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}

	public java.sql.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Date endDate) {
		this.endDate = endDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Float getReqAmount() {
		return reqAmount;
	}

	public void setReqAmount(Float reqAmount) {
		this.reqAmount = reqAmount;
	}

	public ProjectCost getPrjCost() {
		return prjCost;
	}

	public void setPrjCost(ProjectCost prjCost) {
		this.prjCost = prjCost;
	}
}