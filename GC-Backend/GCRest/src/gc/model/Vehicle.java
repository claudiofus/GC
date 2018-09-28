package gc.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import gc.model.types.CarTax;
import gc.model.types.Insurance;
import gc.model.types.Penalty;
import gc.model.types.Revision;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonDeserialize(as = Vehicle.class)
public class Vehicle {
	private int id;
	@XmlElement
	private String brand;
	@XmlElement
	private String model;
	@XmlElement
	private String color;
	@XmlElement(name = "registration_date")
	private java.sql.Date firstRegistration;
	@XmlElement
	private String plate;
	@XmlElement
	private Insurance insurance;
	@XmlElement(name = "car_tax")
	private CarTax carTax;
	@XmlElement
	private Revision revision;
	private List<Penalty> penalty;

	public Vehicle() {
	}

	public Vehicle(String brand, String model, String color,
			java.sql.Date firstRegistration, String plate) {
		super();
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.firstRegistration = firstRegistration;
		this.plate = plate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
