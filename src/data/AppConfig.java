package data;

import resources.FontAwesomeData;

public class AppConfig {

    // --- Store ---
    public static final String STORE_NAME = "Sole & Stitch";
    public static final String STAFF_NAME = "Andy";

    // --- POS Categories ---
    public static final String[] POS_CAT_NAMES = {
        "New Arrivals", "Sneakers", "Boots",
        "Levi's Tops", "Levi's Bottoms", "Accessories", "Sale"
    };
    public static final String[] POS_CAT_ICONS = {
        FontAwesomeData.ICON_FA_TAGS,
        FontAwesomeData.ICON_FA_SHOE_PRINTS,
        FontAwesomeData.ICON_FA_SHOPPING_BAG,  // Boots (FA_BOOT is Pro-only)
        FontAwesomeData.ICON_FA_TSHIRT,
        FontAwesomeData.ICON_FA_CUT,
        FontAwesomeData.ICON_FA_GLASSES,        // Accessories (FA_SUNGLASSES is Pro-only)
        FontAwesomeData.ICON_FA_PERCENT,
    };

    // --- Inventory Categories ---
    public static final String[] INV_CAT_NAMES = {
        "Sneakers", "Boots", "Tops", "Jeans", "Jackets",
        "Accessories", "Socks", "Packaging", "Others"
    };
    public static final String[] INV_CAT_ICONS = {
        FontAwesomeData.ICON_FA_SHOE_PRINTS,
        FontAwesomeData.ICON_FA_SHOPPING_BAG,
        FontAwesomeData.ICON_FA_TSHIRT,
        FontAwesomeData.ICON_FA_RULER,
        FontAwesomeData.ICON_FA_LAYER_GROUP,
        FontAwesomeData.ICON_FA_GLASSES,
        FontAwesomeData.ICON_FA_SOCKS,
        FontAwesomeData.ICON_FA_BOX,
        FontAwesomeData.ICON_FA_PLUS,
    };
}
