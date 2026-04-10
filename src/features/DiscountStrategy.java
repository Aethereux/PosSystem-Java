package features;

import data.OrderState;

public interface DiscountStrategy {
    float calculateDiscount(OrderState orderState);
}
