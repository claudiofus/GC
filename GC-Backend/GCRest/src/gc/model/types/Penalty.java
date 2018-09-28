package gc.model.types;

import javax.xml.bind.annotation.XmlElement;

public class Penalty extends AScadenza {
	@XmlElement
	private String description;
	@XmlElement
	private int points;
	@XmlElement
	private boolean paid;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}
}
