package features;

import data.OrderState;

public class CashPayment implements PaymentStrategy{
    @Override
    public String getMethodName() {
        return "Cash Payment";
    }

    @Override
    public void pay(OrderState amount) {

    }
}
