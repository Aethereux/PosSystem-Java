package features;

import data.OrderState;

public class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(OrderState orderState) {
        strategy.pay(orderState);
    }
}
