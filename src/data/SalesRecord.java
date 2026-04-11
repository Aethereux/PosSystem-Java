package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesRecord {
    public final int orderNumber;
    public final String staffName;
    public final String dateTime;
    public final String paymentMethod;
    public final List<OrderState.OrderItem> items;
    public final float subtotal;
    public final float discountAmount;
    public final float total;

    public SalesRecord(int orderNumber, String staffName, String dateTime,
                       String paymentMethod, List<OrderState.OrderItem> items,
                       float subtotal, float discountAmount) {
        this.orderNumber = orderNumber;
        this.staffName = staffName;
        this.dateTime = dateTime;
        this.paymentMethod = paymentMethod;
        List<OrderState.OrderItem> copy = new ArrayList<>();
        for (OrderState.OrderItem item : items) {
            copy.add(new OrderState.OrderItem(item.name, item.unitPrice, item.quantity));
        }
        this.items = Collections.unmodifiableList(copy);
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.total = Math.max(0, subtotal - discountAmount);
    }
}
