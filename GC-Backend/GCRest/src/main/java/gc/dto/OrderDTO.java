package gc.dto;

import gc.model.Order;

public class OrderDTO {
	private Order[] order;
	private int ddtId;

	public Order[] getOrder() {
		return order;
	}

	public void setOrder(Order[] order) {
		this.order = order;
	}
	
	public int getDdtId() {
		return ddtId;
	}

	public void setDdtId(int ddtId) {
		this.ddtId = ddtId;
	}
}
