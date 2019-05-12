package gc.model.types.job;

import java.math.BigDecimal;

public class Salary {
	private int id;
	private BigDecimal salaryForHour;
	private BigDecimal salaryForDay;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
