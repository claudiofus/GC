package gc.model.types;

import java.io.Serializable;

public class Insurance extends AScadenza implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1152056749058861113L;
	private String company;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
