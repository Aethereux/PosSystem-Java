package features;

import data.OrderState;
import inventory.InventoryItem;
import inventory.InventoryManager;

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
        deductInventoryStock();
    }

    private void deductInventoryStock() {
        InventoryManager inv = InventoryManager.getInstance();

        for (OrderState.OrderItem orderedItem : orderState.items) {
            int remainingQty = orderedItem.quantity;

            for (InventoryItem invItem : inv.getItems().values()) {
                if (remainingQty <= 0) break;
                if (!isMatchingProduct(orderedItem.name, invItem.name)) continue;
                if (invItem.stock <= 0) continue;

                int deduct = Math.min(invItem.stock, remainingQty);
                inv.stockOut(invItem.productId, deduct);
                remainingQty -= deduct;
            }
        }
    }

    private boolean isMatchingProduct(String orderName, String inventoryName) {
        String normalizedOrder = normalizeName(orderName);
        String normalizedInventory = normalizeName(inventoryName);
        return normalizedInventory.contains(normalizedOrder) || normalizedOrder.contains(normalizedInventory);
    }

    private String normalizeName(String value) {
        String normalized = value.toLowerCase();

        int parenIndex = normalized.indexOf('(');
        if (parenIndex >= 0) {
            normalized = normalized.substring(0, parenIndex);
        }

        normalized = normalized
                .replace("'", "")
                .replace("-", " ")
                .replace("clearance", "")
                .replace("last stock", "")
                .trim();

        return normalized.replaceAll("\\s+", " ");
    }
}
