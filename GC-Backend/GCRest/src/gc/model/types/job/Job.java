package gc.model.types.job;

import java.sql.Date;

public class Job {
	private int id;
	private int worker_id;
	private int hoursOfWork;
	private Date dateOfWork;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(int worker_id) {
		this.worker_id = worker_id;
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
