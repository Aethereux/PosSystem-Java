package screens;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImInt;
import menu.MenuManager;
import ui.*;

public class SalesScreen {

    private static final String[] CATEGORIES = {
        "Sneakers", "Levi's Tops", "Levi's Bottoms", "Boots"
    };

    // {productName, unitsSold, unitPrice, totalSales, costPerUnit, totalCost, profit}
    private static final String[][][] DATA = {
        { // Sneakers
            {"Nike Air Force 1", "120", "4995", "599400", "2800", "336000", "263400"},
            {"Nike Dunk Low",    "95",  "5495", "522025", "3100", "294500", "227525"},
            {"Adidas Stan Smith","80",  "4495", "359600", "2500", "200000", "159600"},
            {"New Balance 574",  "65",  "4295", "279175", "2400", "156000", "123175"},
            {"Converse Chuck 70","110", "3995", "439450", "2000", "220000", "219450"},
            {"Jordan 1 Mid",     "45",  "6295", "283275", "3500", "157500", "125775"},
        },
        { // Levi's Tops
            {"Classic Tee",      "200", "1295", "259000", "450",  "90000",  "169000"},
            {"Batwing Logo Tee", "180", "1495", "269100", "500",  "90000",  "179100"},
            {"Flannel Shirt",    "75",  "2995", "224625", "1200", "90000",  "134625"},
            {"Trucker Jacket",   "50",  "4995", "249750", "2200", "110000", "139750"},
            {"Hoodie Classic",   "90",  "2795", "251550", "1100", "99000",  "152550"},
        },
        { // Levi's Bottoms
            {"501 Original",     "150", "2995", "449250", "1200", "180000", "269250"},
            {"511 Slim Fit",     "130", "3295", "428350", "1300", "169000", "259350"},
            {"505 Regular",      "100", "2795", "279500", "1100", "110000", "169500"},
            {"502 Taper",        "85",  "3495", "297075", "1400", "119000", "178075"},
            {"Ribcage Wide Leg", "60",  "3995", "239700", "1600", "96000",  "143700"},
            {"XX Chino",         "70",  "2495", "174650", "1000", "70000",  "104650"},
        },
        { // Boots
            {"Dr. Martens 1460",     "35", "9995",  "349825", "5500", "192500", "157325"},
            {"Timberland 6-Inch",    "30", "10495", "314850", "5800", "174000", "140850"},
            {"Chelsea Boot Classic", "45", "5995",  "269775", "3000", "135000", "134775"},
            {"Levi's Emerson",       "55", "4495",  "247225", "2200", "121000", "126225"},
        },
    };

    private double enterTime = 0;
    private int selectedCategory = 0;
    private final ImInt comboState = new ImInt(0);

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        ImDrawList dl = ImGui.getWindowDrawList();

        // Sidebar
        Sidebar.render(MenuManager.SCREEN_SALES);

        // Content area as child window — cursor is now right of sidebar
        ImGui.sameLine();
        float contentW = ImGui.getContentRegionAvailX();
        float contentH = ImGui.getContentRegionAvailY();

        int noScroll = imgui.flag.ImGuiWindowFlags.NoScrollbar | imgui.flag.ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##sales_content", contentW, contentH, false, noScroll);

        float pad = Math.max(10, contentW * 0.015f);

        // Title
        ImGui.setCursorPos(pad, pad);
        ImGui.setWindowFontScale(1.4f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1, "Sales Report");
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();

        // Category dropdown
        ImGui.setCursorPosX(pad);
        ImGui.text("Category:");
        ImGui.sameLine();
        ImGui.pushItemWidth(200);
        if (ImGui.combo("##sales_cat", comboState, CATEGORIES)) {
            selectedCategory = comboState.get();
            enterTime = ImGui.getTime();
        }
        ImGui.popItemWidth();
        ImGui.spacing();

        // Sales data
        String[][] data = DATA[selectedCategory];

        // Table
        float availH = ImGui.getContentRegionAvailY();
        float tableH = availH * 0.42f;
        int tableFlags = ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg | ImGuiTableFlags.ScrollY
                | ImGuiTableFlags.Resizable | ImGuiTableFlags.SizingStretchProp;

        ImGui.setCursorPosX(pad);
        float tableW = ImGui.getContentRegionAvailX() - pad;

        if (ImGui.beginTable("##sales_table", 7, tableFlags, tableW, tableH)) {
            ImGui.tableSetupColumn("Product Name", ImGuiTableColumnFlags.None, 2.0f);
            ImGui.tableSetupColumn("Units Sold", ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Unit Price", ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Total Sales", ImGuiTableColumnFlags.None, 1.2f);
            ImGui.tableSetupColumn("Cost/Unit", ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Total Cost", ImGuiTableColumnFlags.None, 1.2f);
            ImGui.tableSetupColumn("Profit", ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableHeadersRow();

            float tableAnim = AnimationHelper.computeProgress(enterTime, 0.6f);

            for (int i = 0; i < data.length; i++) {
                float rowAnim = AnimationHelper.staggered(tableAnim, i, data.length, 0.5f);
                if (rowAnim <= 0) continue;

                ImGui.tableNextRow();
                for (int col = 0; col < 7; col++) {
                    ImGui.tableNextColumn();
                    if (col == 0) {
                        ImGui.text(data[i][col]);
                    } else {
                        float val = Float.parseFloat(data[i][col]);
                        String formatted = col == 1 ? String.valueOf((int) val) : String.format("%,.0f", val);
                        if (col == 6) {
                            ImGui.textColored(Theme.SUCCESS.x, Theme.SUCCESS.y, Theme.SUCCESS.z, 1, formatted);
                        } else {
                            ImGui.text(formatted);
                        }
                    }
                }
            }

            // Totals row
            ImGui.tableNextRow();
            ImGui.tableNextColumn();
            ImGui.setWindowFontScale(1.05f);
            ImGui.textColored(Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 1, "TOTAL");
            for (int col = 1; col < 7; col++) {
                ImGui.tableNextColumn();
                float total = 0;
                for (String[] row : data) total += Float.parseFloat(row[col]);
                String formatted = col == 1 ? String.valueOf((int) total) : String.format("%,.0f", total);
                ImGui.textColored(Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 1, formatted);
            }
            ImGui.setWindowFontScale(1.0f);

            ImGui.endTable();
        }

        ImGui.spacing();

        // Charts
        float chartAvailH = ImGui.getContentRegionAvailY();
        if (chartAvailH < 60) {
            ImGui.endChild();
            return;
        }

        float chartAnim = AnimationHelper.computeProgress(enterTime + 0.3, 1.0f);
        chartAnim = AnimationHelper.easeOutCubic(chartAnim);

        float chartW = ImGui.getContentRegionAvailX() - pad;
        float pieChartW = chartW * 0.42f;
        float barChartW = chartW - pieChartW - pad;

        // Pie chart area
        ImGui.setCursorPosX(pad);
        ImVec2 chartStart = new ImVec2();
        ImGui.getCursorScreenPos(chartStart);

        // Pie chart card
        dl = ImGui.getWindowDrawList();
        Theme.drawShadow(dl, chartStart.x, chartStart.y, pieChartW, chartAvailH, 12, 0.4f);
        Theme.drawRoundedRect(dl, chartStart.x, chartStart.y, pieChartW, chartAvailH,
                Theme.toColor(Theme.BG_PANEL), 12);

        ImGui.setWindowFontScale(1.1f);
        dl.addText(chartStart.x + 16, chartStart.y + 12, Theme.toColor(Theme.TEXT_PRIMARY), "Units Sold");
        ImGui.setWindowFontScale(1.0f);

        float[] pieValues = new float[data.length];
        String[] pieLabels = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            pieValues[i] = Float.parseFloat(data[i][1]);
            pieLabels[i] = data[i][0];
        }

        float pieRadius = Math.min(pieChartW * 0.32f, (chartAvailH - 60) * 0.38f);
        float pieCx = chartStart.x + pieChartW * 0.45f;
        float pieCy = chartStart.y + 40 + pieRadius + 8;

        ChartRenderer.drawPieChart(dl, pieCx, pieCy, pieRadius, pieValues, pieLabels,
                Theme.CHART_COLORS, chartAnim);

        float legendY = pieCy + pieRadius + 16;
        ChartRenderer.drawPieLegend(dl, chartStart.x + 16, legendY, pieValues, pieLabels,
                Theme.CHART_COLORS, chartAnim);

        // Bar chart card
        float barX = chartStart.x + pieChartW + pad;
        Theme.drawShadow(dl, barX, chartStart.y, barChartW, chartAvailH, 12, 0.4f);
        Theme.drawRoundedRect(dl, barX, chartStart.y, barChartW, chartAvailH,
                Theme.toColor(Theme.BG_PANEL), 12);

        ImGui.setWindowFontScale(1.1f);
        dl.addText(barX + 16, chartStart.y + 12, Theme.toColor(Theme.TEXT_PRIMARY), "Total Cost and Profit");
        ImGui.setWindowFontScale(1.0f);

        String[] barLabels = new String[data.length];
        float[][] barData = new float[2][data.length];
        for (int i = 0; i < data.length; i++) {
            barLabels[i] = data[i][0];
            barData[0][i] = Float.parseFloat(data[i][5]);
            barData[1][i] = Float.parseFloat(data[i][6]);
        }

        ImVec4[] datasetColors = {Theme.PRIMARY, Theme.DANGER};
        ChartRenderer.drawHorizontalBarChart(dl, barX + 16, chartStart.y + 50,
                barChartW - 32, chartAvailH - 70,
                barLabels, barData, new String[]{"Total Cost", "Profit"}, datasetColors, chartAnim);

        // Reserve space so child window knows about chart area
        ImGui.dummy(chartW, chartAvailH);

        ImGui.endChild();
    }
}
