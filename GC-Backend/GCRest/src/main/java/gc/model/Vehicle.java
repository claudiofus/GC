package gc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import gc.model.types.CarTax;
import gc.model.types.Insurance;
import gc.model.types.Penalty;
import gc.model.types.Revision;

public class Vehicle {
	private String brand;
	private String model;
	private String color;
	@JsonProperty("registration_date")
	private java.sql.Date firstRegistration;
	private String plate;
	private Insurance insurance;
	@JsonProperty("car_tax")
	private CarTax carTax;
	private Revision revision;
	private List<Penalty> penalty;

	public Vehicle() {
	}

	public Vehicle(String brand, String model, String color, java.sql.Date firstRegistration, String plate) {
		super();
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.firstRegistration = firstRegistration;
		this.plate = plate;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public java.sql.Date getFirstRegistration() {
		return firstRegistration;
	}

	public void setFirstRegistration(java.sql.Date firstRegistration) {
		this.firstRegistration = firstRegistration;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	public CarTax getCarTax() {
		return carTax;
	}

	public void setCarTax(CarTax carTax) {
		this.carTax = carTax;
	}

	public Revision getRevision() {
		return revision;
	}

	public void setRevision(Revision revision) {
		this.revision = revision;
	}

	public List<Penalty> getPenalty() {
		return penalty;
	}

	public void setPenalty(List<Penalty> penalty) {
		this.penalty = penalty;
	}
}
