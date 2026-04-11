package inventory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class NotificationCenter implements InventoryObserver {

    private static NotificationCenter instance;


    private final Map<String, Notification> active = new LinkedHashMap<>();
    private boolean enabled = true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) active.clear();
    }

    public boolean isEnabled() { return enabled; }

    public static NotificationCenter getInstance() {
        if (instance == null) {
            instance = new NotificationCenter();
        }
        return instance;
    }

    @Override
    public void onLowStock(InventoryItem item) {
        if (!enabled) return;
        active.put(item.productId,
                new Notification(item.productId,
                        item.productId + " - " + item.name,
                        "Low stock",
                        item.stock));
    }

    @Override
    public void onOutOfStock(InventoryItem item) {
        if (!enabled) return;
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
