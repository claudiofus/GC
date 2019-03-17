package gc.model.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gc.model.Order;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseOrder extends Order {

	public BaseOrder() {
		super();
	}
}
