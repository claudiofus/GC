package gc.model;

public class Product {
	private String name;
	private String providerName;
	private float medPrice;

	public Product() {

	}

	public Product(String name, String providerName) {
		this.name = name;
		this.providerName = providerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public float getMedPrice() {
		return medPrice;
	}

	public void setMedPrice(float medPrice) {
		this.medPrice = medPrice;
	}
}