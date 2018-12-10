package gc.model.types;

public abstract class AScadenza {
	public java.sql.Date deadlineDate;
	public float amount;

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
}