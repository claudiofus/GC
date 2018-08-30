package gc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
	@XmlElement
	private int id;
	@XmlElement
	private String title;
	@XmlElement
	private boolean paid;
	private java.sql.Date start_date;
	@XmlElement
	private boolean executed;

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

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
}