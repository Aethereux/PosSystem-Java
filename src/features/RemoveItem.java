package features;

import data.OrderState;

public class RemoveItem implements Command {
    private final OrderState orderState;
    private final int itemIndex;

    public RemoveItem(OrderState orderState, int itemIndex) {
        this.orderState = orderState;
        this.itemIndex = itemIndex;
    }

    @Override
    public void execute() {
        orderState.removeItem(itemIndex);
    }
}
