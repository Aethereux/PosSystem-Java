package features;

import data.OrderState;

public class AddItem implements Command {
    private final OrderState orderState;
    private final String itemName;
    private final float itemPrice;

    public AddItem(OrderState orderState, String itemName, float itemPrice) {
        this.orderState = orderState;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    @Override
    public void execute() {
        orderState.addItem(itemName, itemPrice);
    }
}
