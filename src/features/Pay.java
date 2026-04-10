package features;

import data.OrderState;
import data.SampleData;

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
        for (OrderState.OrderItem orderedItem : orderState.items) {
            int remainingQty = orderedItem.quantity;

            for (String[] inventoryRow : SampleData.INVENTORY) {
                if (remainingQty <= 0) {
                    break;
                }

                if (!isMatchingProduct(orderedItem.name, inventoryRow[4])) {
                    continue;
                }

                int stock = Integer.parseInt(inventoryRow[6]);
                if (stock <= 0) {
                    continue;
                }

                int deduct = Math.min(stock, remainingQty);
                inventoryRow[6] = String.valueOf(stock - deduct);
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
