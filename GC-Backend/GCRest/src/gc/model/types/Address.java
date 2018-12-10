package gc.model.types;

public class Address {
	private String addressType;
	private String addressName;
	private String addressNumber;
	private String cap;
	private String city;
	private String province;
	private String state;

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static String getDescFromCode(String type) {
		String desc = null;
		switch (type) {
		case "contr":
			desc = "Contrada";
			break;
		case "cso":
			desc = "Corso";
			break;
		case "lgo":
			desc = "Largo";
			break;
		case "pzza":
			desc = "Piazza";
			break;
		case "ple":
			desc = "Piazzale";
			break;
		case "ptta":
			desc = "Piazzetta";
			break;
		case "via":
			desc = "Via";
			break;
		case "vle":
			desc = "Viale";
			break;
		case "vico":
			desc = "Vico";
			break;
		case "vicl":
			desc = "Vicolo";
			break;
		case "zona":
			desc = "Zona";
			break;
		case "zi":
			desc = "Zona Industriale";
			break;
		default:
			break;
		}
		return desc;
	}
}
