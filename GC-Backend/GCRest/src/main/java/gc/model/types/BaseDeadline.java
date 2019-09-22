package gc.model.types;

import java.math.BigDecimal;

public abstract class BaseDeadline {
	private java.sql.Date deadlineDate;
	private BigDecimal amount;
	private int eventID;
	private boolean paid;

	public java.sql.Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(java.sql.Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}
}