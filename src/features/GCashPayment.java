package features;

import data.OrderState;

public class GCashPayment implements PaymentStrategy {

    @Override
    public String getMethodName() {
        return "GCash Payment";
    }

    @Override
    public void pay(OrderState amount) {

    }
}