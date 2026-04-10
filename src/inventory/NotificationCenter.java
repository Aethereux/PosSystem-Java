package inventory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete Observer — listens to InventoryManager events and maintains
 * the live list of active alerts shown on the Dashboard.
 */
public class NotificationCenter implements InventoryObserver {

    private static NotificationCenter instance;

    // Keyed by productId so each item has at most one active notification
    private final Map<String, Notification> active = new LinkedHashMap<>();

    public static NotificationCenter getInstance() {
        if (instance == null) {
            instance = new NotificationCenter();
        }
        return instance;
    }

    @Override
    public void onLowStock(InventoryItem item) {
        active.put(item.productId,
                new Notification(item.productId,
                        item.productId + " - " + item.name,
                        "Low stock",
                        item.stock));
    }

    @Override
    public void onOutOfStock(InventoryItem item) {
        active.put(item.productId,
                new Notification(item.productId,
                        item.productId + " - " + item.name,
                        "Out of stock",
                        item.stock));
    }

    @Override
    public void onStockRestored(InventoryItem item) {
        active.remove(item.productId);
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(active.values());
    }

    public int size() {
        return active.size();
    }
}
