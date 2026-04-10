package features;

import data.OrderState;

public class DiscountContext {
	private DiscountStrategy strategy;

	public void setStrategy(DiscountStrategy strategy) {
		this.strategy = strategy;
	}

	public float executeDiscount(OrderState orderState) {
		return strategy.calculateDiscount(orderState);
	}
}
