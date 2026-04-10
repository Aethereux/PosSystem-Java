package data;

import java.util.ArrayList;
import java.util.List;

public class OrderState {
    public int orderNumber = 1;
    public String staffName = SampleData.STAFF_NAME;
    public int tableNumber = 1;

    public List<OrderItem> items = new ArrayList<>();

    public static class OrderItem {
        public String name;
        public float unitPrice;
        public int quantity;

        public OrderItem(String name, float unitPrice, int quantity) {
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public float getLineTotal() {
            return unitPrice * quantity;
        }
    }

    public void addItem(String name, float price) {
        for (OrderItem item : items) {
            if (item.name.equals(name)) {
                item.quantity++;
                return;
            }
        }
        items.add(new OrderItem(name, price, 1));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void incrementQty(int index) {
        if (index >= 0 && index < items.size()) {
            items.get(index).quantity++;
        }
    }

    public void decrementQty(int index) {
        if (index >= 0 && index < items.size()) {
            OrderItem item = items.get(index);
            if (item.quantity > 1) {
                item.quantity--;
            } else {
                items.remove(index);
            }
        }
    }

    public float getSubtotal() {
        float total = 0;
        for (OrderItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    public float getVatableAmount() {
        return getSubtotal() / 1.12f;
    }

    public float getVatAmount() {
        return getSubtotal() - getVatableAmount();
    }

    public float getTotal() {
        return getSubtotal();
    }

    public void clear() {
        items.clear();
        orderNumber++;
        tableNumber = 1;
    }
}
