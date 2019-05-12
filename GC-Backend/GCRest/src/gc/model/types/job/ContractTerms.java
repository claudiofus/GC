package gc.model.types.job;

import java.sql.Date;

public class ContractTerms {
	private int id;
	private Date recruitmentDate;
	private Date dismissalDate;
	private ContractType contractType;
	private Qualification qualification;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}
}
