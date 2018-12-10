package gc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = { "color" })
public class Event {
	private int id;
	private String title;
	private boolean paid;
	private boolean allDay;
	private java.sql.Date start_date;

	public Event() {
		super();
	}

	public Event(String title) {
		super();
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("start")
	public java.sql.Date getStart_date() {
		return start_date;
	}

	public void setStart_date(java.sql.Date start_date) {
		this.start_date = start_date;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
}