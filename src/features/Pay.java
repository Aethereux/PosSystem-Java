package features;

import data.OrderState;

public class Pay implements Command {
    private final OrderState orderState;
    private final PaymentStrategy paymentStrategy;


    public Pay(OrderState orderState, PaymentStrategy paymentStrategy) {
        this.orderState = orderState;
        this.paymentStrategy = paymentStrategy;
    }

    @Override
    public void execute() {
        paymentStrategy.pay(orderState);
    }
}
