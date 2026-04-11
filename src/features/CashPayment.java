package features;

import data.OrderState;

public class CashPayment implements PaymentStrategy{
    @Override
    public String getMethodName() {
        return "Cash Payment";
    }

    @Override
    public void pay(OrderState amount) {
        System.out.printf("[CashPayment] Order #%04d — Total: %.2f%n",
                amount.orderNumber, amount.getTotal());
    }
}
