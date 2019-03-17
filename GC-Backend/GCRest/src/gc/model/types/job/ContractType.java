package gc.model.types.job;

public enum ContractType {
	INDETERMINATO("Contratto a tempo indeterminato"),
	DETERMINATO("Contratto a tempo determinato/termine"),
	SOMMINISTRAZIONE("Contratto di somministrazione"),
	CHIAMATA("Contratto a chiamata"),
	VOUCHER("Voucher"),
	APPRENDISTATO("Apprendistato"),
	PART_TIME("Part-time"),
	PROGETTO("Contratto a progetto "),
	TIROCINIO("Tirocinio formativo");

	private String type;

	private ContractType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
