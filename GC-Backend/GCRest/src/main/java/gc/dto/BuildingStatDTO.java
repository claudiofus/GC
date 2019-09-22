package gc.dto;

import java.math.BigDecimal;

import gc.model.Building;

public class BuildingStatDTO {
	private Building building;
	private BigDecimal ordersTotal;
	private BigDecimal workersTotal;

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public BigDecimal getOrdersTotal() {
		return ordersTotal;
	}

	public void setOrdersTotal(BigDecimal ordersTotal) {
		this.ordersTotal = ordersTotal;
	}

	public BigDecimal getWorkersTotal() {
		return workersTotal;
	}

	public void setWorkersTotal(BigDecimal workersTotal) {
		this.workersTotal = workersTotal;
	}
}
