package gc.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gc.model.types.BaseOrder;

@JsonDeserialize(as = BaseOrder.class)
public abstract class Order {
	private int id;
	private Integer building_id;
	private String name;
	private String um;
	private BigDecimal quantity;
	private BigDecimal price;
	private BigDecimal discount;
	private BigDecimal noIvaPrice;
	private BigDecimal iva;
	private BigDecimal ivaPrice;
	private java.sql.Date date_order;
	private boolean state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Integer building_id) {
		this.building_id = building_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUm() {
		return um;
	}
	
	public void setUm(String um) {
		this.um = um;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getDiscount() {
		return discount;
	}
	
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	public BigDecimal getNoIvaPrice() {
		return noIvaPrice;
	}

	public void setNoIvaPrice(BigDecimal noIvaPrice) {
		this.noIvaPrice = noIvaPrice;
	}

	public BigDecimal getIva() {
		return iva;
	}
	
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	
	public BigDecimal getIvaPrice() {
		return ivaPrice;
	}
	
	public void setIvaPrice(BigDecimal ivaPrice) {
		this.ivaPrice = ivaPrice;
	}
	
	public java.sql.Date getDate_order() {
		return date_order;
	}
	
	public void setDate_order(java.sql.Date date_order) {
		this.date_order = date_order;
	}
	
	public boolean isState() {
		return state;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
}