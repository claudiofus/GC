package gc.model.types.job;

import java.util.Date;

public class JobType {
	private Qualification qualification;
	private Date profSeniority;
	
	public Qualification getQualification() {
		return qualification;
	}
	
	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}
	
	public Date getProfSeniority() {
		return profSeniority;
	}
	
	public void setProfSeniority(Date profSeniority) {
		this.profSeniority = profSeniority;
	}
}