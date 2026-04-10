package features;

import data.OrderState;

public class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(OrderState orderState) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy has not been set.");
        }
        strategy.pay(orderState);
    }

    public String getMethodName() {
        if (strategy == null) {
            return "N/A";
        }
        return strategy.getMethodName();
    }
}
