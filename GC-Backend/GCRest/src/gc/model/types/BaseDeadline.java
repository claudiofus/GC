package gc.model.types;

public abstract class BaseDeadline {
	private java.sql.Date deadlineDate;
	private float amount;
	private int eventID;
	private boolean paid;

	public java.sql.Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(java.sql.Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
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