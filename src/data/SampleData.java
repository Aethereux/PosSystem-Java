package data;

import resources.FontAwesomeData;

public class SampleData {

    public static final String STORE_NAME = "Sole & Stitch";
    public static final String STAFF_NAME = "Andy";

    // --- POS Categories ---
    public static final String[] POS_CAT_NAMES = {"New Arrivals", "Sneakers", "Boots", "Levi's Tops", "Levi's Bottoms", "Accessories", "Sale"};
    public static final String[] POS_CAT_ICONS = {
        FontAwesomeData.ICON_FA_TAGS,
        FontAwesomeData.ICON_FA_SHOE_PRINTS,
        FontAwesomeData.ICON_FA_SHOPPING_BAG,   // Boots (FA_BOOT is Pro-only)
        FontAwesomeData.ICON_FA_TSHIRT,
        FontAwesomeData.ICON_FA_CUT,
        FontAwesomeData.ICON_FA_GLASSES,         // Accessories (FA_SUNGLASSES is Pro-only)
        FontAwesomeData.ICON_FA_PERCENT,
    };

    // --- Inventory Categories ---
    public static final String[] INV_CAT_NAMES = {"Sneakers", "Boots", "Tops", "Jeans", "Jackets", "Accessories", "Socks", "Packaging", "Others"};
    public static final String[] INV_CAT_ICONS = {
        FontAwesomeData.ICON_FA_SHOE_PRINTS,
        FontAwesomeData.ICON_FA_SHOPPING_BAG,    // Boots
        FontAwesomeData.ICON_FA_TSHIRT,
        FontAwesomeData.ICON_FA_RULER,
        FontAwesomeData.ICON_FA_LAYER_GROUP,     // Jackets (FA_SHIELD_ALT may be Pro)
        FontAwesomeData.ICON_FA_GLASSES,         // Accessories
        FontAwesomeData.ICON_FA_SOCKS,
        FontAwesomeData.ICON_FA_BOX,
        FontAwesomeData.ICON_FA_PLUS,
    };

    // --- Products (POS) ---
    // {name, category_index, price}
    public static final String[][] PRODUCTS = {
        // New Arrivals (0)
        {"Nike Dunk Low Panda", "0", "5495.00"},
        {"Levi's 501 Original 2026", "0", "3295.00"},
        {"Jordan 1 Low SE", "0", "6495.00"},
        {"Levi's Trucker Jacket", "0", "4995.00"},
        // Sneakers (1)
        {"Nike Air Force 1 '07", "1", "4995.00"},
        {"Nike Dunk Low Retro", "1", "5495.00"},
        {"Adidas Stan Smith", "1", "4495.00"},
        {"Adidas Superstar", "1", "4995.00"},
        {"New Balance 574", "1", "4295.00"},
        {"Converse Chuck 70", "1", "3995.00"},
        {"Vans Old Skool", "1", "3495.00"},
        {"Puma Suede Classic", "1", "3795.00"},
        {"Jordan 1 Mid", "1", "6295.00"},
        {"Nike Air Max 90", "1", "6495.00"},
        // Boots (2)
        {"Dr. Martens 1460", "2", "9995.00"},
        {"Timberland 6-Inch Premium", "2", "10495.00"},
        {"Chelsea Boot Classic", "2", "5995.00"},
        {"Levi's Emerson Boot", "2", "4495.00"},
        {"Dr. Martens Chelsea", "2", "8995.00"},
        // Levi's Tops (3)
        {"Levi's Classic Tee", "3", "1295.00"},
        {"Levi's Batwing Logo Tee", "3", "1495.00"},
        {"Levi's Flannel Shirt", "3", "2995.00"},
        {"Levi's Denim Shirt", "3", "3495.00"},
        {"Levi's Trucker Jacket", "3", "4995.00"},
        {"Levi's Hoodie Classic", "3", "2795.00"},
        {"Levi's Crewneck Sweater", "3", "2495.00"},
        // Levi's Bottoms (4)
        {"Levi's 501 Original", "4", "2995.00"},
        {"Levi's 505 Regular Fit", "4", "2795.00"},
        {"Levi's 511 Slim Fit", "4", "3295.00"},
        {"Levi's 510 Skinny", "4", "3295.00"},
        {"Levi's 550 Relaxed", "4", "2995.00"},
        {"Levi's 502 Taper", "4", "3495.00"},
        {"Levi's Ribcage Wide Leg", "4", "3995.00"},
        {"Levi's XX Chino", "4", "2495.00"},
        // Accessories (5)
        {"Levi's Leather Belt", "5", "1495.00"},
        {"Nike Cap Classic", "5", "995.00"},
        {"Levi's Trucker Cap", "5", "895.00"},
        {"Crew Socks 3-Pack", "5", "595.00"},
        {"Levi's Canvas Tote", "5", "1295.00"},
        {"Shoe Cleaning Kit", "5", "495.00"},
        {"Sneaker Protector Spray", "5", "395.00"},
        // Sale (6)
        {"Nike Cortez (Clearance)", "6", "2995.00"},
        {"Levi's 501 Shorts", "6", "1795.00"},
        {"Vans Slip-On (Last Stock)", "6", "1995.00"},
        {"Levi's Basic Tee 2-Pack", "6", "1495.00"},
    };

    // --- Sales Data ---
    // {productName, unitsSold, unitPrice, totalSales, costPerUnit, totalCost, profit}
    public static final String[] SALES_CATEGORIES = {"Sneakers", "Levi's Tops", "Levi's Bottoms", "Boots"};
    public static final String[][][] SALES_DATA = {
        // Sneakers
        {
            {"Nike Air Force 1", "120", "4995", "599400", "2800", "336000", "263400"},
            {"Nike Dunk Low", "95", "5495", "522025", "3100", "294500", "227525"},
            {"Adidas Stan Smith", "80", "4495", "359600", "2500", "200000", "159600"},
            {"New Balance 574", "65", "4295", "279175", "2400", "156000", "123175"},
            {"Converse Chuck 70", "110", "3995", "439450", "2000", "220000", "219450"},
            {"Jordan 1 Mid", "45", "6295", "283275", "3500", "157500", "125775"},
        },
        // Levi's Tops
        {
            {"Classic Tee", "200", "1295", "259000", "450", "90000", "169000"},
            {"Batwing Logo Tee", "180", "1495", "269100", "500", "90000", "179100"},
            {"Flannel Shirt", "75", "2995", "224625", "1200", "90000", "134625"},
            {"Trucker Jacket", "50", "4995", "249750", "2200", "110000", "139750"},
            {"Hoodie Classic", "90", "2795", "251550", "1100", "99000", "152550"},
        },
        // Levi's Bottoms
        {
            {"501 Original", "150", "2995", "449250", "1200", "180000", "269250"},
            {"511 Slim Fit", "130", "3295", "428350", "1300", "169000", "259350"},
            {"505 Regular", "100", "2795", "279500", "1100", "110000", "169500"},
            {"502 Taper", "85", "3495", "297075", "1400", "119000", "178075"},
            {"Ribcage Wide Leg", "60", "3995", "239700", "1600", "96000", "143700"},
            {"XX Chino", "70", "2495", "174650", "1000", "70000", "104650"},
        },
        // Boots
        {
            {"Dr. Martens 1460", "35", "9995", "349825", "5500", "192500", "157325"},
            {"Timberland 6-Inch", "30", "10495", "314850", "5800", "174000", "140850"},
            {"Chelsea Boot Classic", "45", "5995", "269775", "3000", "135000", "134775"},
            {"Levi's Emerson", "55", "4495", "247225", "2200", "121000", "126225"},
        },
    };

    // --- Inventory Data ---
    // {no, manufacturedDate, expiryDate(or N/A), productId, productName, unitPrice, stock, categoryIndex}
    public static final String[][] INVENTORY = {
        // Sneakers (0)
        {"1", "2026-01-15", "N/A", "SN-001", "Nike Air Force 1 '07 (US 8)", "4995", "12", "0"},
        {"2", "2026-01-15", "N/A", "SN-002", "Nike Air Force 1 '07 (US 9)", "4995", "18", "0"},
        {"3", "2026-02-01", "N/A", "SN-003", "Nike Dunk Low Retro (US 8)", "5495", "8", "0"},
        {"4", "2026-02-01", "N/A", "SN-004", "Nike Dunk Low Retro (US 9)", "5495", "15", "0"},
        {"5", "2026-01-20", "N/A", "SN-005", "Adidas Stan Smith (US 8)", "4495", "20", "0"},
        {"6", "2026-01-20", "N/A", "SN-006", "Adidas Stan Smith (US 10)", "4495", "6", "0"},
        {"7", "2026-02-10", "N/A", "SN-007", "New Balance 574 (US 9)", "4295", "14", "0"},
        {"8", "2026-01-25", "N/A", "SN-008", "Converse Chuck 70 (US 8)", "3995", "22", "0"},
        {"9", "2026-03-01", "N/A", "SN-009", "Jordan 1 Mid (US 9)", "6295", "5", "0"},
        {"10", "2026-02-15", "N/A", "SN-010", "Vans Old Skool (US 9)", "3495", "25", "0"},
        // Boots (1)
        {"1", "2025-11-01", "N/A", "BT-001", "Dr. Martens 1460 (US 8)", "9995", "4", "1"},
        {"2", "2025-11-01", "N/A", "BT-002", "Dr. Martens 1460 (US 9)", "9995", "7", "1"},
        {"3", "2025-12-15", "N/A", "BT-003", "Timberland 6-Inch (US 9)", "10495", "3", "1"},
        {"4", "2026-01-10", "N/A", "BT-004", "Chelsea Boot Classic (US 8)", "5995", "10", "1"},
        {"5", "2026-02-01", "N/A", "BT-005", "Levi's Emerson (US 9)", "4495", "12", "1"},
        // Tops (2)
        {"1", "2026-03-01", "N/A", "TP-001", "Levi's Classic Tee (S)", "1295", "45", "2"},
        {"2", "2026-03-01", "N/A", "TP-002", "Levi's Classic Tee (M)", "1295", "60", "2"},
        {"3", "2026-03-01", "N/A", "TP-003", "Levi's Classic Tee (L)", "1295", "38", "2"},
        {"4", "2026-03-05", "N/A", "TP-004", "Levi's Batwing Logo Tee (M)", "1495", "42", "2"},
        {"5", "2026-02-20", "N/A", "TP-005", "Levi's Flannel Shirt (M)", "2995", "15", "2"},
        {"6", "2026-02-20", "N/A", "TP-006", "Levi's Flannel Shirt (L)", "2995", "12", "2"},
        {"7", "2026-02-15", "N/A", "TP-007", "Levi's Denim Shirt (M)", "3495", "18", "2"},
        {"8", "2026-01-20", "N/A", "TP-008", "Levi's Trucker Jacket (M)", "4995", "8", "2"},
        {"9", "2026-01-20", "N/A", "TP-009", "Levi's Trucker Jacket (L)", "4995", "6", "2"},
        {"10", "2026-03-10", "N/A", "TP-010", "Levi's Hoodie Classic (M)", "2795", "20", "2"},
        // Jeans (3)
        {"1", "2026-02-01", "N/A", "JN-001", "Levi's 501 Original (30x32)", "2995", "25", "3"},
        {"2", "2026-02-01", "N/A", "JN-002", "Levi's 501 Original (32x32)", "2995", "30", "3"},
        {"3", "2026-02-01", "N/A", "JN-003", "Levi's 501 Original (34x32)", "2995", "18", "3"},
        {"4", "2026-02-10", "N/A", "JN-004", "Levi's 511 Slim (30x32)", "3295", "22", "3"},
        {"5", "2026-02-10", "N/A", "JN-005", "Levi's 511 Slim (32x32)", "3295", "28", "3"},
        {"6", "2026-02-15", "N/A", "JN-006", "Levi's 505 Regular (32x30)", "2795", "15", "3"},
        {"7", "2026-02-15", "N/A", "JN-007", "Levi's 505 Regular (34x32)", "2795", "20", "3"},
        {"8", "2026-03-01", "N/A", "JN-008", "Levi's 502 Taper (32x32)", "3495", "12", "3"},
        {"9", "2026-03-05", "N/A", "JN-009", "Levi's Ribcage Wide (28)", "3995", "8", "3"},
        {"10", "2026-03-10", "N/A", "JN-010", "Levi's XX Chino (32x32)", "2495", "16", "3"},
        // Jackets (4)
        {"1", "2026-01-15", "N/A", "JK-001", "Levi's Trucker Jacket (S)", "4995", "5", "4"},
        {"2", "2026-01-15", "N/A", "JK-002", "Levi's Trucker Jacket (M)", "4995", "8", "4"},
        {"3", "2026-01-15", "N/A", "JK-003", "Levi's Trucker Jacket (L)", "4995", "6", "4"},
        {"4", "2026-01-20", "N/A", "JK-004", "Levi's Sherpa Jacket (M)", "5995", "3", "4"},
        {"5", "2026-01-20", "N/A", "JK-005", "Levi's Sherpa Jacket (L)", "5995", "4", "4"},
        // Accessories (5)
        {"1", "2026-03-01", "N/A", "AC-001", "Levi's Leather Belt (M)", "1495", "30", "5"},
        {"2", "2026-03-01", "N/A", "AC-002", "Levi's Leather Belt (L)", "1495", "25", "5"},
        {"3", "2026-02-15", "N/A", "AC-003", "Nike Cap Classic", "995", "40", "5"},
        {"4", "2026-02-15", "N/A", "AC-004", "Levi's Trucker Cap", "895", "35", "5"},
        {"5", "2026-03-10", "N/A", "AC-005", "Levi's Canvas Tote", "1295", "18", "5"},
        {"6", "2026-03-15", "N/A", "AC-006", "Shoe Cleaning Kit", "495", "50", "5"},
        {"7", "2026-03-15", "N/A", "AC-007", "Sneaker Protector Spray", "395", "45", "5"},
        // Socks (6)
        {"1", "2026-03-01", "N/A", "SK-001", "Crew Socks 3-Pack (White)", "595", "80", "6"},
        {"2", "2026-03-01", "N/A", "SK-002", "Crew Socks 3-Pack (Black)", "595", "75", "6"},
        {"3", "2026-03-05", "N/A", "SK-003", "Ankle Socks 3-Pack", "495", "60", "6"},
        {"4", "2026-03-10", "N/A", "SK-004", "Levi's Logo Socks 2-Pack", "395", "45", "6"},
        // Packaging (7)
        {"1", "2026-01-01", "N/A", "PG-001", "Shoe Box Standard (50pc)", "2500", "120", "7"},
        {"2", "2026-01-01", "N/A", "PG-002", "Paper Bag Large (100pc)", "1800", "200", "7"},
        {"3", "2026-01-01", "N/A", "PG-003", "Tissue Paper (500pc)", "800", "300", "7"},
        {"4", "2026-01-01", "N/A", "PG-004", "Branded Stickers (500pc)", "600", "250", "7"},
        // Others (8)
        {"1", "2026-02-01", "N/A", "OT-001", "Display Shelf Unit", "8500", "3", "8"},
        {"2", "2026-03-01", "N/A", "OT-002", "Mannequin Half Body", "4500", "2", "8"},
    };

    // --- Notification Dummy Data ---
    public static final String[][] NOTIFICATIONS = {
        {"SN-009 - Jordan 1 Mid (US 9)", "Low stock", "5"},
        {"BT-003 - Timberland 6-Inch", "Low stock", "3"},
        {"JK-004 - Sherpa Jacket (M)", "Low stock", "3"},
        {"BT-001 - Dr. Martens 1460", "Low stock", "4"},
        {"OT-002 - Mannequin Half Body", "Low stock", "2"},
        {"JN-009 - Ribcage Wide (28)", "Low stock", "8"},
        {"SN-006 - Stan Smith (US 10)", "Low stock", "6"},
        {"JK-001 - Trucker Jacket (S)", "Low stock", "5"},
    };
}
