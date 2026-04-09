package screens;

import data.OrderState;
import data.SampleData;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import menu.MenuManager;
import resources.FontAwesomeData;
import ui.*;

public class POSScreen {

    private double enterTime = 0;
    private int selectedCategory = 0;
    private final ImString searchBuffer = new ImString(128);
    private final OrderState orderState = new OrderState();

    {
        orderState.addItem("Nike Dunk Low Panda", 5495.00f);
        orderState.addItem("Levi's 501 Original", 2995.00f);
        orderState.addItem("Crew Socks 3-Pack", 595.00f);
    }

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        float progress = AnimationHelper.computeProgress(enterTime, 0.6f);

        // Sidebar
        Sidebar.render(MenuManager.SCREEN_POS);

        // Remaining area after sidebar
        ImGui.sameLine();
        float totalAvailW = ImGui.getContentRegionAvailX();
        float totalAvailH = ImGui.getContentRegionAvailY();

        // Split: product area (left) and order panel (right)
        float orderPanelW = Math.max(240, totalAvailW * 0.26f);
        float productAreaW = totalAvailW - orderPanelW;

        // === Product Area (child window) — no scroll, grid child handles it ===
        int noScroll = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##pos_products", productAreaW, totalAvailH, false, noScroll);

        float pad = Math.max(8, productAreaW * 0.02f);
        ImGui.setCursorPos(pad, pad);

        // Search bar
        float searchW = ImGui.getContentRegionAvailX() - pad;
        WidgetHelper.searchBar("##pos_search", searchBuffer, searchW);
        ImGui.spacing();

        // Category label
        ImGui.setCursorPosX(pad);
        ImGui.textColored(Theme.TEXT_SECONDARY.x, Theme.TEXT_SECONDARY.y, Theme.TEXT_SECONDARY.z, 1, "Category");
        ImGui.spacing();

        // Category row
        ImGui.setCursorPosX(pad);
        float catAnim = AnimationHelper.computeProgress(enterTime, 0.8f);
        selectedCategory = WidgetHelper.categoryRow(SampleData.POS_CAT_NAMES, SampleData.POS_CAT_ICONS,
                selectedCategory, catAnim);

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        // Category title
        ImGui.setCursorPosX(pad);
        ImGui.setWindowFontScale(1.3f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1,
                SampleData.POS_CAT_NAMES[selectedCategory]);
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();

        // Product grid (scrollable child — this one SHOULD scroll)
        ImGui.setCursorPosX(pad);
        float gridW = ImGui.getContentRegionAvailX() - pad;
        float gridH = ImGui.getContentRegionAvailY() - pad;
        ImGui.beginChild("##product_grid", gridW, gridH);

        ImVec2 gridOrigin = new ImVec2();
        ImGui.getCursorScreenPos(gridOrigin);

        float gridAvailW = ImGui.getContentRegionAvailX();
        float cardSpacing = Math.max(8, gridAvailW * 0.02f);
        float cardW = Math.max(120, Math.min(170, (gridAvailW - cardSpacing * 4) / 5));
        float cardH = cardW * 1.28f;
        int cols = Math.max(1, (int) ((gridAvailW + cardSpacing) / (cardW + cardSpacing)));

        String searchStr = searchBuffer.get().toLowerCase().trim();
        int visibleIndex = 0;
        float scrollY = ImGui.getScrollY();

        for (int i = 0; i < SampleData.PRODUCTS.length; i++) {
            String[] product = SampleData.PRODUCTS[i];
            int catIdx = Integer.parseInt(product[1]);
            if (catIdx != selectedCategory) continue;
            if (!searchStr.isEmpty() && !product[0].toLowerCase().contains(searchStr)) continue;

            int col = visibleIndex % cols;
            int row = visibleIndex / cols;

            float screenX = gridOrigin.x + col * (cardW + cardSpacing);
            float screenY = gridOrigin.y + row * (cardH + cardSpacing) - scrollY;

            float cardAnim = AnimationHelper.staggered(progress, visibleIndex, Math.min(cols * 3, 15), 0.4f);

            String priceStr = String.format("P %.2f", Float.parseFloat(product[2]));
            boolean clicked = WidgetHelper.productCard(product[0], priceStr,
                    screenX, screenY, cardW, cardH, cardAnim, i);

            if (clicked) {
                orderState.addItem(product[0], Float.parseFloat(product[2]));
            }

            visibleIndex++;
        }

        if (visibleIndex > 0) {
            int totalRows = (visibleIndex + cols - 1) / cols;
            ImGui.setCursorScreenPos(gridOrigin.x, gridOrigin.y);
            ImGui.dummy(gridAvailW, totalRows * (cardH + cardSpacing));
        }

        ImGui.endChild(); // product_grid
        ImGui.endChild(); // pos_products

        // === Order Summary Panel (child window) ===
        ImGui.sameLine();
        renderOrderPanel(orderPanelW, totalAvailH, progress);
    }

    private void renderOrderPanel(float w, float h, float progress) {
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 16);
        int panelFlags = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##order_panel", w, h, true, panelFlags);

        ImDrawList dl = ImGui.getWindowDrawList();
        ImVec2 panelPos = new ImVec2();
        ImGui.getWindowPos(panelPos);
        ImVec2 panelSize = new ImVec2();
        ImGui.getWindowSize(panelSize);

        Theme.drawRoundedRectOutline(dl, panelPos.x, panelPos.y, panelSize.x, panelSize.y,
                Theme.toColor(Theme.PRIMARY, 0.3f), 16, 2);

        float pad = Math.max(8, ImGui.getContentRegionAvailX() * 0.05f);
        ImGui.setCursorPos(pad, pad);

        // Title
        ImGui.setWindowFontScale(1.3f);
        ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSizeX("Order Summary")) / 2);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1, "Order Summary");
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();

        // Order info — use getContentRegionAvailX for right-align
        ImGui.setCursorPosX(pad);
        ImGui.text("Order No.:");
        ImGui.sameLine(ImGui.getContentRegionAvailX() - 40);
        ImGui.text(String.format("#%04d", orderState.orderNumber));

        ImGui.setCursorPosX(pad);
        ImGui.text("Staff name:");
        ImGui.sameLine(ImGui.getContentRegionAvailX() - 40);
        ImGui.text(orderState.staffName);

        ImGui.setCursorPosX(pad);
        ImGui.text("Table No.:");
        ImGui.sameLine(ImGui.getContentRegionAvailX() - 40);
        ImGui.text(String.valueOf(orderState.tableNumber));

        ImGui.spacing();

        // Order type toggle
        ImGui.setCursorPosX(pad);
        String[] types = {"Dine In", "To Go", "Delivery"};
        float toggleW = ImGui.getContentRegionAvailX() - pad;
        float typeW = toggleW / 3;
        ImVec2 toggleStart = new ImVec2();
        ImGui.getCursorScreenPos(toggleStart);

        for (int i = 0; i < 3; i++) {
            float tx = toggleStart.x + i * typeW;
            boolean active = orderState.orderType == i;

            ImGui.setCursorScreenPos(tx, toggleStart.y);
            ImGui.invisibleButton("##orderType_" + i, typeW, 28);
            if (ImGui.isItemHovered()) ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            if (ImGui.isItemClicked()) orderState.orderType = i;

            int bgColor = active ? Theme.toColor(Theme.PRIMARY) : Theme.toColor(Theme.BG_CARD);
            Theme.drawRoundedRect(dl, tx + 1, toggleStart.y, typeW - 2, 28, bgColor, 8);
            Theme.drawTextCentered(dl, types[i], tx, toggleStart.y, typeW, 28,
                    Theme.toColor(active ? Theme.TEXT_PRIMARY : Theme.TEXT_SECONDARY));
        }
        ImGui.setCursorScreenPos(toggleStart.x, toggleStart.y + 34);
        ImGui.dummy(toggleW, 1);

        ImGui.separator();
        ImGui.spacing();

        // Items header
        ImGui.setCursorPosX(pad);
        ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1, "Item Name");
        ImGui.sameLine(ImGui.getContentRegionAvailX() - 60);
        ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1, "Qty   Price");
        ImGui.spacing();

        // Scrollable items area — only this child scrolls
        float lineH = ImGui.getTextLineHeightWithSpacing();
        float bottomReserve = lineH * 6 + 50;
        float itemsH = Math.max(lineH * 2, ImGui.getContentRegionAvailY() - bottomReserve);
        float itemsW = ImGui.getContentRegionAvailX();

        ImGui.beginChild("##order_items", itemsW, itemsH);

        for (int i = 0; i < orderState.items.size(); i++) {
            OrderState.OrderItem item = orderState.items.get(i);
            float rowAvail = ImGui.getContentRegionAvailX();

            // Name + delete
            ImGui.text(item.name.length() > 24 ? item.name.substring(0, 22) + ".." : item.name);
            ImGui.sameLine(rowAvail - 20);
            ImGui.pushStyleColor(imgui.flag.ImGuiCol.Button, 0, 0, 0, 0);
            ImGui.pushStyleColor(imgui.flag.ImGuiCol.ButtonHovered, Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 0.2f);
            ImGui.pushStyleColor(imgui.flag.ImGuiCol.ButtonActive, Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 0.4f);
            ImGui.pushStyleColor(imgui.flag.ImGuiCol.Text, Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 0.7f);
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 2, 2);
            if (ImGui.smallButton(FontAwesomeData.ICON_FA_TIMES + "##del_" + i)) {
                orderState.removeItem(i);
            }
            ImGui.popStyleVar();
            ImGui.popStyleColor(4);

            // Qty controls + price
            if (ImGui.smallButton("-##minus_" + i)) orderState.decrementQty(i);
            ImGui.sameLine();
            ImGui.text(String.valueOf(item.quantity));
            ImGui.sameLine();
            if (ImGui.smallButton("+##plus_" + i)) orderState.incrementQty(i);
            ImGui.sameLine(rowAvail - 70);
            ImGui.text(String.format("%.2f", item.getLineTotal()));

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();
        }

        ImGui.endChild(); // order_items

        // Totals
        ImGui.separator();
        ImGui.spacing();

        String[] totalLabels = {"VATable Sales:", "VAT Amount:", "VAT-Exempt:", "VAT-Zero:", "Total Amount:"};
        float[] totalValues = {orderState.getVatableAmount(), orderState.getVatAmount(), 0, 0, orderState.getTotal()};

        for (int i = 0; i < totalLabels.length; i++) {
            boolean isTotal = i == totalLabels.length - 1;
            ImGui.setCursorPosX(pad);

            if (isTotal) {
                ImGui.setWindowFontScale(1.1f);
                ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1, totalLabels[i]);
                ImGui.sameLine(ImGui.getContentRegionAvailX() - 70);
                ImGui.textColored(Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 1,
                        String.format("%.2f", totalValues[i]));
                ImGui.setWindowFontScale(1.0f);
            } else {
                ImGui.textColored(Theme.TEXT_SECONDARY.x, Theme.TEXT_SECONDARY.y, Theme.TEXT_SECONDARY.z, 1, totalLabels[i]);
                ImGui.sameLine(ImGui.getContentRegionAvailX() - 70);
                ImGui.text(String.format("%.2f", totalValues[i]));
            }
        }

        ImGui.spacing();

        // Buttons — use available width
        ImGui.setCursorPosX(pad);
        float btnAvail = ImGui.getContentRegionAvailX() - pad;
        float btnW = (btnAvail - 8) / 2;
        float btnH = 36;

        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 10);

        ImGui.pushStyleColor(imgui.flag.ImGuiCol.Button, Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 1);
        ImGui.pushStyleColor(imgui.flag.ImGuiCol.ButtonHovered, Theme.PRIMARY_LIGHT.x, Theme.PRIMARY_LIGHT.y, Theme.PRIMARY_LIGHT.z, 1);
        ImGui.button(FontAwesomeData.ICON_FA_CREDIT_CARD + " Payment", btnW, btnH);
        ImGui.popStyleColor(2);

        ImGui.sameLine();

        ImGui.pushStyleColor(imgui.flag.ImGuiCol.Button, Theme.BG_CARD.x, Theme.BG_CARD.y, Theme.BG_CARD.z, 1);
        ImGui.pushStyleColor(imgui.flag.ImGuiCol.ButtonHovered, Theme.BG_HOVER.x, Theme.BG_HOVER.y, Theme.BG_HOVER.z, 1);
        ImGui.button(FontAwesomeData.ICON_FA_PRINT + " Print", btnW, btnH);
        ImGui.popStyleColor(2);

        ImGui.popStyleVar(); // FrameRounding

        ImGui.endChild(); // order_panel
        ImGui.popStyleVar(); // ChildRounding
    }
}
