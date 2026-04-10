package features;

import data.OrderState;

public class Pay implements Command {
    private final OrderState orderState;

    public Pay(OrderState orderState) {
        this.orderState = orderState;
    }

    @Override
    public void execute() {
        orderState.clear();
    }
}