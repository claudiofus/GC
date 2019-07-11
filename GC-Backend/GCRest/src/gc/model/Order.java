package gc.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gc.model.types.BaseOrder;

@JsonDeserialize(as = BaseOrder.class)
public abstract class Order {
	private int id;
	private Integer invoiceId;
	private Integer ddtId;
	private Integer buildingId;
	private String name;
	private String um;
	private BigDecimal quantity;
	private BigDecimal price;
	private BigDecimal discount;
	private BigDecimal noIvaPrice;
	private BigDecimal iva;
	private BigDecimal ivaPrice;
	private java.sql.Date dateOrder;
	private boolean state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getDdtId() {
		return ddtId;
	}

	public void setDdtId(Integer ddtId) {
		this.ddtId = ddtId;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
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

	public java.sql.Date getDateOrder() {
		return dateOrder;
	}

	public void setDateOrder(java.sql.Date dateOrder) {
		this.dateOrder = dateOrder;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
}