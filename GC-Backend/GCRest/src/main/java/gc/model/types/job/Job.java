package gc.model.types.job;

import java.sql.Date;

public class Job {
	private int id;
	private int workerId;
	private int hoursOfWork;
	private Date dateOfWork;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public int getHoursOfWork() {
		return hoursOfWork;
	}

	public void setHoursOfWork(int hoursOfWork) {
		this.hoursOfWork = hoursOfWork;
	}

	public Date getDateOfWork() {
		return dateOfWork;
	}

	public void setDateOfWork(Date dateOfWork) {
		this.dateOfWork = dateOfWork;
	}
}
