package gc.model;

import java.awt.Rectangle;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gc.model.types.BaseOrder;

@JsonDeserialize(as = BaseOrder.class)
public abstract class Order {
	@JsonIgnore
	public abstract String getDDT();

	@JsonIgnore
	public abstract Rectangle getORDERS_AREA();

	@JsonIgnore
	public abstract String getDBCODE();

	@JsonIgnore
	public abstract String getDATEORDER();

	@JsonIgnore
	public abstract String getDATEFORMAT();

	@JsonIgnore
	public abstract LinkedMap<String, ArrayList<Order>> parseOrder(PDDocument document, Connection conn, int page,
			LinkedMap<String, ArrayList<Order>> map) throws InvalidPasswordException, IOException;

	private int id;
	private Integer building_id;
	private String code;
	private String name;
	private String um;
	private float quantity;
	private float price;
	private float discount;
	private float adj_price;
	private float iva;
	private java.sql.Date date_order;
	private boolean state;

	public Order() {
		super();
	}

	public Order(int id, String code, String name, String um, float quantity, float price, float discount,
			float adj_price, float iva, java.sql.Date date_order) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.um = um;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.adj_price = adj_price;
		this.iva = iva;
		this.date_order = new java.sql.Date(date_order.getTime());
	}

	public Order(String code, String name, String um, float quantity, float price, float discount, float adj_price,
			float iva, java.sql.Date date_order) {
		super();
		this.code = code;
		this.name = name;
		this.um = um;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.adj_price = adj_price;
		this.iva = iva;
		this.date_order = new java.sql.Date(date_order.getTime());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getAdj_price() {
		return adj_price;
	}

	public void setAdj_price(float adj_price) {
		this.adj_price = adj_price;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
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

	public Integer getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Integer building_id) {
		this.building_id = building_id;
	}
}