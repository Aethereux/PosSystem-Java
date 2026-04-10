package inventory;

public interface InventoryObserver {
    void onLowStock(InventoryItem item);
    void onOutOfStock(InventoryItem item);
    void onStockRestored(InventoryItem item);
}
