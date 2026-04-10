package ui;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseCursor;
import imgui.type.ImString;

public class WidgetHelper {

    /**
     * Renders a search bar with icon.
     */
    public static void searchBar(String id, ImString buffer, float width) {
        ImGui.pushStyleColor(ImGuiCol.FrameBg,
                Theme.BG_CARD.x, Theme.BG_CARD.y, Theme.BG_CARD.z, Theme.BG_CARD.w);
        ImGui.pushStyleColor(ImGuiCol.FrameBgHovered,
                Theme.BG_HOVER.x, Theme.BG_HOVER.y, Theme.BG_HOVER.z, Theme.BG_HOVER.w);
        ImGui.pushStyleColor(ImGuiCol.FrameBgActive,
                Theme.BG_ACTIVE.x, Theme.BG_ACTIVE.y, Theme.BG_ACTIVE.z, Theme.BG_ACTIVE.w);

        ImGui.pushItemWidth(width);
        ImGui.inputTextWithHint(id, resources.FontAwesomeData.ICON_FA_SEARCH + "  Search...", buffer);
        ImGui.popItemWidth();
        ImGui.popStyleColor(3);
    }

    /**
     * Renders a horizontal category row with icon buttons. Returns new selected index.
     */
    public static int categoryRow(String[] names, String[] icons, int selected, float animProgress) {
        int newSelected = selected;
        ImDrawList dl = ImGui.getWindowDrawList();

        // Responsive category button sizing based on available width
        float availWidth = ImGui.getContentRegionAvailX();
        float spacing = 8;
        float buttonW = Math.max(55, (availWidth - spacing * (names.length - 1)) / names.length);
        buttonW = Math.min(buttonW, 100);
        float buttonH = buttonW * 0.82f;

        ImVec2 cursorScreen = new ImVec2();
        ImGui.getCursorScreenPos(cursorScreen);
        float startX = cursorScreen.x;
        float startY = cursorScreen.y;

        for (int i = 0; i < names.length; i++) {
            float itemAnim = AnimationHelper.staggered(animProgress, i, names.length, 0.5f);
            itemAnim = AnimationHelper.easeOutBack(itemAnim);
            if (itemAnim <= 0) continue;

            float bx = startX + i * (buttonW + spacing);
            float by = startY + (1 - itemAnim) * 10; // slide down animation
            boolean isActive = (i == selected);

            // Invisible button for click
            ImGui.setCursorScreenPos(bx, by);
            ImGui.invisibleButton("##cat_" + i, buttonW, buttonH);
            boolean hovered = ImGui.isItemHovered();
            if (hovered) ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            if (ImGui.isItemClicked()) {
                newSelected = i;
            }

            // Background
            int bgColor;
            if (isActive) {
                bgColor = Theme.toColor(Theme.PRIMARY, itemAnim);
            } else if (hovered) {
                bgColor = Theme.toColor(Theme.BG_HOVER, itemAnim);
            } else {
                bgColor = Theme.toColor(Theme.BG_CARD, itemAnim);
            }
            Theme.drawRoundedRect(dl, bx, by, buttonW, buttonH, bgColor, 12);

            // Border for active
            if (isActive) {
                Theme.drawRoundedRectOutline(dl, bx, by, buttonW, buttonH,
                        Theme.toColor(Theme.PRIMARY_LIGHT, 0.5f * itemAnim), 12, 1.5f);
            }

            // Icon
            ImGui.setWindowFontScale(1.3f);
            Theme.drawTextCentered(dl, icons[i], bx, by, buttonW, buttonH * 0.6f,
                    Theme.toColor(Theme.TEXT_PRIMARY, itemAnim));
            ImGui.setWindowFontScale(1.0f);

            // Label — shrink font to fit button width
            imgui.ImFont font = ImGui.getFont();
            float baseFontSize = font.getFontSize() * ImGui.getIO().getFontGlobalScale();
            float maxLabelW = buttonW - 6; // small padding
            int labelFontSize = (int) baseFontSize;

            ImVec2 labelSize = new ImVec2();
            font.calcTextSizeA(labelSize, labelFontSize, Float.MAX_VALUE, -1, names[i]);
            while (labelSize.x > maxLabelW && labelFontSize > 8) {
                labelFontSize--;
                font.calcTextSizeA(labelSize, labelFontSize, Float.MAX_VALUE, -1, names[i]);
            }

            float labelX = bx + (buttonW - labelSize.x) / 2;
            float labelY = by + buttonH - labelSize.y - 4;
            dl.addText(font, labelFontSize, labelX, labelY,
                    Theme.toColor(isActive ? Theme.TEXT_PRIMARY : Theme.TEXT_SECONDARY, itemAnim), names[i]);
        }

        // Reserve space
        ImGui.setCursorScreenPos(startX, startY + buttonH + 8);
        ImGui.dummy(names.length * (buttonW + spacing), 1);

        return newSelected;
    }

    /**
     * Draws a product card for the POS grid.
     * Returns true if "Add to Basket" was clicked.
     */
    public static boolean productCard(String name, String price,
                                       float w, float h, float animProgress, int index) {
        ImDrawList dl = ImGui.getWindowDrawList();
        boolean clicked = false;

        ImVec2 startPos = new ImVec2();
        ImGui.getCursorScreenPos(startPos);
        float x = startPos.x;
        float y = startPos.y;

        float scale = AnimationHelper.easeOutBack(animProgress);
        if (scale <= 0) return false;

        float cardW = w * scale;
        float cardH = h * scale;
        float cx = x + (w - cardW) / 2;
        float cy = y + (h - cardH) / 2;
        float alpha = animProgress;

        // Shadow
        Theme.drawShadow(dl, cx, cy, cardW, cardH, 10, alpha * 0.5f);

        // Card background
        Theme.drawRoundedRect(dl, cx, cy, cardW, cardH, Theme.toColor(Theme.BG_CARD, alpha), 10);

        // Image placeholder area
        float imgH = cardH * 0.45f;
        float imgPad = 6;
        Theme.drawRoundedRect(dl, cx + imgPad, cy + imgPad, cardW - imgPad * 2, imgH,
                Theme.toColor(Theme.BG_HOVER, alpha * 0.5f), 8);

        // Placeholder icon
        ImGui.setWindowFontScale(1.5f);
        Theme.drawTextCentered(dl, resources.FontAwesomeData.ICON_FA_SHOPPING_BAG,
                cx + imgPad, cy + imgPad, cardW - imgPad * 2, imgH,
                Theme.toColor(Theme.TEXT_MUTED, alpha * 0.4f));
        ImGui.setWindowFontScale(1.0f);

        // Product name (truncate if too long)
        String displayName = name.length() > 18 ? name.substring(0, 16) + ".." : name;
        ImVec2 nameSize = new ImVec2();
        ImGui.calcTextSize(nameSize, displayName);
        float nameX = cx + (cardW - nameSize.x) / 2;
        float nameY = cy + imgH + imgPad + 4;
        dl.addText(nameX, nameY, Theme.toColor(Theme.TEXT_PRIMARY, alpha), displayName);

        // Price
        ImVec2 priceSize = new ImVec2();
        ImGui.calcTextSize(priceSize, price);
        float priceX = cx + (cardW - priceSize.x) / 2;
        float priceY = nameY + nameSize.y + 2;
        dl.addText(priceX, priceY, Theme.toColor(Theme.TEXT_SECONDARY, alpha), price);

        // "Add to Basket" button
        float btnW = cardW - 16;
        float btnH = 24;
        float btnX = cx + 8;
        float btnY = cy + cardH - btnH - 8;

        ImGui.setCursorScreenPos(btnX, btnY);
        ImGui.invisibleButton("##addBtn_" + index, btnW, btnH);
        boolean btnHovered = ImGui.isItemHovered();
        if (btnHovered) ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
        if (ImGui.isItemClicked()) clicked = true;

        int btnColor = btnHovered
                ? Theme.toColor(Theme.ACCENT, alpha)
                : Theme.toColor(Theme.SUCCESS, alpha * 0.85f);
        Theme.drawRoundedRect(dl, btnX, btnY, btnW, btnH, btnColor, 6);
        Theme.drawTextCentered(dl, "Add to Basket", btnX, btnY, btnW, btnH,
                Theme.toColor(1, 1, 1, alpha));

        return clicked;
    }

    /**
     * Draws a notification row for the dashboard.
     */
    public static void notificationRow(ImDrawList dl, String label, String type, ImVec4 rowColor,
                                        float x, float y, float w, float h, float animProgress) {
        float alpha = AnimationHelper.easeOutCubic(animProgress);
        if (alpha <= 0) return;

        float slideX = (1 - alpha) * -30;

        // Row background
        Theme.drawRoundedRect(dl, x + slideX, y, w, h,
                Theme.toColor(rowColor.x, rowColor.y, rowColor.z, rowColor.w * alpha), 6);

        // Type indicator dot
        int dotColor;
        switch (type) {
            case "Out of stock": dotColor = Theme.toColor(Theme.DANGER, alpha); break;
            case "Low stock": dotColor = Theme.toColor(Theme.WARNING, alpha); break;
            case "Near Expiry": dotColor = Theme.toColor(Theme.INFO, alpha); break;
            case "Dispose stock": dotColor = Theme.toColor(0.6f, 0.4f, 0.8f, alpha); break;
            case "Defective stock": dotColor = Theme.toColor(0.98f, 0.5f, 0.2f, alpha); break;
            default: dotColor = Theme.toColor(Theme.TEXT_MUTED, alpha); break;
        }
        dl.addCircleFilled(x + slideX + 16, y + h / 2, 5, dotColor);

        // Label
        dl.addText(x + slideX + 30, y + (h - 14) / 2, Theme.toColor(Theme.TEXT_PRIMARY, alpha), label);

        // Type badge
        ImVec2 typeSize = new ImVec2();
        ImGui.calcTextSize(typeSize, type);
        float badgeX = x + slideX + w - typeSize.x - 16;
        dl.addText(badgeX, y + (h - 14) / 2, Theme.toColor(Theme.TEXT_SECONDARY, alpha), type);
    }
}
