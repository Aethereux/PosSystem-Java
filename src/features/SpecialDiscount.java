package features;

import data.OrderState;

// Senior, PWD
public class SpecialDiscount implements DiscountStrategy {

    private static final float specialRate = 0.20f;

    @Override
    public float calculateDiscount(OrderState orderState) {
        return orderState.getTotal() * specialRate;
    }
}
