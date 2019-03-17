package gc.model;

import gc.einvoice.ContattiType;
import gc.model.types.job.ContractTerms;
import gc.model.types.job.JobType;
import gc.model.types.job.Salary;

public class Worker {
	private int id;
	private String name;
	private String surname;
	private String fiscalCode;
	private java.sql.Date birthDate;
	private String birthPlace;
	private String birthProvPlace;
	private String gender;
	private boolean married;
	private ContattiType contacts;
	private ContractTerms contractTerms;
	private JobType jobType;
	private Salary salary;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public java.sql.Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(java.sql.Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBirthProvPlace() {
		return birthProvPlace;
	}

	public void setBirthProvPlace(String birthProvPlace) {
		this.birthProvPlace = birthProvPlace;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public ContattiType getContacts() {
		return contacts;
	}

	public void setContacts(ContattiType contacts) {
		this.contacts = contacts;
	}

	public ContractTerms getContractTerms() {
		return contractTerms;
	}

	public void setContractTerms(ContractTerms contractTerms) {
		this.contractTerms = contractTerms;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}
}
