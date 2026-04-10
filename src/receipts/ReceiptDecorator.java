package receipts;

import data.OrderState;

public abstract class ReceiptDecorator implements ReceiptInterface {
    protected final ReceiptInterface wrappedReceipt;

    protected ReceiptDecorator(ReceiptInterface wrappedReceipt) {
        this.wrappedReceipt = wrappedReceipt;
    }

    @Override
    public void render(OrderState orderState, String paymentMethod, float discountAmount) {
        if (wrappedReceipt != null) {
            wrappedReceipt.render(orderState, paymentMethod, discountAmount);
        }
    }
}