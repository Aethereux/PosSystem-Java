package inventory;

public class Notification {
    public final String productId;
    public final String label;
    public final String type;
    public final int stockLevel;
    public final long timestamp;

    public Notification(String productId, String label, String type, int stockLevel) {
        this.productId = productId;
        this.label = label;
        this.type = type;
        this.stockLevel = stockLevel;
        this.timestamp = System.currentTimeMillis();
    }
}
