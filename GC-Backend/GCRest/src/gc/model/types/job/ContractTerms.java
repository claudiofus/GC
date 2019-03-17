package gc.model.types.job;

import java.util.Date;

public class ContractTerms {
	private Date recruitmentDate;
	private Date dismissalDate;
	private ContractType contractType;

	public Date getRecruitmentDate() {
		return recruitmentDate;
	}
	
	public void setRecruitmentDate(Date recruitmentDate) {
		this.recruitmentDate = recruitmentDate;
	}
	
	public Date getDismissalDate() {
		return dismissalDate;
	}
	
	public void setDismissalDate(Date dismissalDate) {
		this.dismissalDate = dismissalDate;
	}
	
	public ContractType getContractType() {
		return contractType;
	}
	
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
}
