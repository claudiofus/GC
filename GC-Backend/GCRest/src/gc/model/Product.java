package gc.model;

public class Product {
	private String code;
	private String name;
	private String providerCode;
	private float medPrice;

	public Product() {

	}

	public Product(String code, String name, String providerCode) {
		this.code = code;
		this.name = name;
		this.providerCode = providerCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public float getMedPrice() {
		return medPrice;
	}

	public void setMedPrice(float medPrice) {
		this.medPrice = medPrice;
	}
}