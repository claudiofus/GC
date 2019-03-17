package gc.model.types.job;

import java.math.BigDecimal;
import java.util.Date;

public class Salary {
	private int daysOfWork;
	private int hoursOfWork;
	private BigDecimal salaryForHour;
	private BigDecimal salaryForDay;
	private Date dateOfWork;

	public int getDaysOfWork() {
		return daysOfWork;
	}

	public void setDaysOfWork(int daysOfWork) {
		this.daysOfWork = daysOfWork;
	}

	public int getHoursOfWork() {
		return hoursOfWork;
	}

	public void setHoursOfWork(int hoursOfWork) {
		this.hoursOfWork = hoursOfWork;
	}

	public BigDecimal getSalaryForHour() {
		return salaryForHour;
	}

	public void setSalaryForHour(BigDecimal salaryForHour) {
		this.salaryForHour = salaryForHour;
	}

	public BigDecimal getSalaryForDay() {
		return salaryForDay;
	}

	public void setSalaryForDay(BigDecimal salaryForDay) {
		this.salaryForDay = salaryForDay;
	}

	public Date getDateOfWork() {
		return dateOfWork;
	}

	public void setDateOfWork(Date dateOfWork) {
		this.dateOfWork = dateOfWork;
	}
}
