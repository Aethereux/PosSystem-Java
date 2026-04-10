package features;

import data.OrderState;

public class PromoDiscount implements DiscountStrategy {

    private static final float promoRate = 0.10f;

    @Override
    public float calculateDiscount(OrderState orderState) {
        return orderState.getTotal() * promoRate;
    }
}
