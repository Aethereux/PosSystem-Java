package receipts;

import data.OrderState;

public interface ReceiptInterface {
    void render(OrderState orderState, String paymentMethod, float discountAmount);
}
