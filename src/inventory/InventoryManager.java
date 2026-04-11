package inventory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class InventoryManager {

    private static InventoryManager instance;

    public static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;

    private final Map<String, InventoryItem> items = new LinkedHashMap<>();
    private final List<InventoryObserver> observers = new ArrayList<>();

    private InventoryManager() {
        loadDefaults();
    }

    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }



    private void loadDefaults() {
        seed("SN-001", "Nike Air Force 1 '07 (US 8)",  4995, 12, 0, "2026-01-15", "N/A",  1);
        seed("BT-001", "Dr. Martens 1460 (US 8)",       9995,  4, 1, "2025-11-01", "N/A",  2);
        seed("TP-001", "Levi's Classic Tee (M)",         1295, 45, 2, "2026-03-01", "N/A",  3);
        seed("JN-001", "Levi's 501 Original (30x32)",    2995, 25, 3, "2026-02-01", "N/A",  4);
        seed("JK-001", "Levi's Sherpa Jacket (M)",       5995,  3, 4, "2026-01-20", "N/A",  3);
        seed("AC-001", "Levi's Leather Belt (M)",         1495, 30, 5, "2026-03-01", "N/A",  5);
        seed("SK-001", "Crew Socks 3-Pack (White)",        595, 80, 6, "2026-03-01", "N/A",  5);
        seed("PG-001", "Shoe Box Standard (50pc)",        2500,120, 7, "2026-01-01", "N/A", -1);
        seed("OT-001", "Mannequin Half Body",             4500,  2, 8, "2026-03-01", "N/A", -1);
    }

    private void seed(String id, String name, double price, int stock,
                      int invCat, String mfg, String expiry, int posCat) {
        items.put(id, new InventoryItem(id, name, price, stock, invCat, mfg, expiry, posCat));
    }

    public void addItem(InventoryItem item) {
        items.put(item.productId, item);
        if (item.stock <= 0) {
            fireOutOfStock(item);
        } else if (item.stock <= item.lowStockThreshold) {
            fireLowStock(item);
        }
    }

    public void removeItem(String productId) {
        InventoryItem item = items.remove(productId);

        if (item != null) fireStockRestored(item);
    }

    public void updateItem(InventoryItem updated) {
        InventoryItem existing = items.get(updated.productId);
        if (existing == null) return;
        int prevStock = existing.stock;
        existing.name             = updated.name;
        existing.unitPrice        = updated.unitPrice;
        existing.categoryIndex    = updated.categoryIndex;
        existing.manufacturedDate = updated.manufacturedDate;
        existing.expiryDate       = updated.expiryDate;
        existing.posCategory      = updated.posCategory;
        existing.stock            = updated.stock;
        checkTransition(existing, prevStock);
    }

    public boolean containsId(String productId) {
        return items.containsKey(productId);
    }

    public void addObserver(InventoryObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    public void scanAndNotify() {
        for (InventoryItem item : items.values()) {
            if (item.stock <= 0) {
                fireOutOfStock(item);
            } else if (item.stock <= item.lowStockThreshold) {
                fireLowStock(item);
            }
        }
    }


    public void stockOut(String productId, int qty) {
        InventoryItem item = items.get(productId);
        if (item == null) return;
        int prev = item.stock;
        item.stock = Math.max(0, item.stock - qty);
        checkTransition(item, prev);
    }

    public void stockIn(String productId, int qty) {
        InventoryItem item = items.get(productId);
        if (item == null) return;
        int prev = item.stock;
        item.stock += qty;
        checkTransition(item, prev);
    }

    private void checkTransition(InventoryItem item, int prev) {
        boolean wasHealthy = prev > item.lowStockThreshold;
        boolean wasLow     = prev > 0 && prev <= item.lowStockThreshold;
        boolean wasOut     = prev <= 0;

        boolean nowOut     = item.stock <= 0;
        boolean nowLow     = item.stock > 0 && item.stock <= item.lowStockThreshold;
        boolean nowHealthy = item.stock > item.lowStockThreshold;

        if (!wasOut && nowOut)         fireOutOfStock(item);
        else if (!wasLow && nowLow)    fireLowStock(item);
        else if (!wasHealthy && nowHealthy) fireStockRestored(item);
    }


    private void fireLowStock(InventoryItem item) {
        for (InventoryObserver o : observers) o.onLowStock(item);
    }

    private void fireOutOfStock(InventoryItem item) {
        for (InventoryObserver o : observers) o.onOutOfStock(item);
    }

    private void fireStockRestored(InventoryItem item) {
        for (InventoryObserver o : observers) o.onStockRestored(item);
    }

    public Map<String, InventoryItem> getItems() {
        return items;
    }

    public InventoryItem getItem(String productId) {
        return items.get(productId);
    }
}
