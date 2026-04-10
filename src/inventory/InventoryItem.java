package inventory;

public class InventoryItem {
    public final String productId;
    public String name;
    public double unitPrice;
    public int categoryIndex;
    public int stock;
    public int lowStockThreshold;
    public String manufacturedDate;
    public String expiryDate;

    /** -1 = not listed in POS; 0-6 = POS category index */
    public int posCategory;

    public InventoryItem(String productId, String name, double unitPrice, int stock,
                         int categoryIndex, String manufacturedDate, String expiryDate,
                         int posCategory) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.categoryIndex = categoryIndex;
        this.manufacturedDate = manufacturedDate;
        this.expiryDate = expiryDate;
        this.posCategory = posCategory;
        this.lowStockThreshold = 10;
    }
}
