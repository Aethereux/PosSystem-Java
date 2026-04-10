package screens;

import data.AppConfig;
import inventory.Notification;
import inventory.NotificationCenter;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiMouseCursor;
import menu.MenuManager;
import resources.FontAwesomeData;
import ui.AnimationHelper;
import ui.Theme;
import ui.WidgetHelper;

public class DashboardScreen {

    private double enterTime = 0;
    private static final float ANIM_DURATION = 0.8f;

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        ImDrawList dl = ImGui.getWindowDrawList();
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        float progress = AnimationHelper.computeProgress(enterTime, ANIM_DURATION);
        float padding = Math.max(12, windowSize.x * 0.015f);
        float x = windowPos.x + padding;
        float y = windowPos.y + padding;
        float totalW = windowSize.x - padding * 2;
        float totalH = windowSize.y - padding * 2;

        float leftW = totalW * 0.55f;
        float rightW = totalW - leftW - padding;
        float topH = totalH * 0.38f;
        float bottomH = totalH - topH - padding;

        // === Store Name Card (top-left) ===
        float cardAnim = AnimationHelper.staggered(progress, 0, 5, 0.4f);
        cardAnim = AnimationHelper.easeOutCubic(cardAnim);
        {
            float slideY = (1 - cardAnim) * 30;
            float cx = x;
            float cy = y + slideY;
            float cw = leftW;
            float ch = topH;

            Theme.drawShadow(dl, cx, cy, cw, ch, 16, cardAnim * 0.6f);

            // Blue gradient background
            int topColor = Theme.toColor(Theme.PRIMARY_DARK, cardAnim);
            int bottomColor = Theme.toColor(Theme.PRIMARY, cardAnim);
            Theme.drawRoundedRect(dl, cx, cy, cw, ch, topColor, 16);
            Theme.drawGradientRect(dl, cx + 2, cy + ch * 0.3f, cw - 4, ch * 0.7f - 2,
                    Theme.toColor(0, 0, 0, 0), bottomColor);

            // Store icon
            ImGui.setWindowFontScale(2.0f);
            dl.addText(cx + 30, cy + ch * 0.2f, Theme.toColor(1, 1, 1, cardAnim * 0.3f),
                    FontAwesomeData.ICON_FA_STORE);
            ImGui.setWindowFontScale(1.0f);

            // Store name
            ImGui.setWindowFontScale(2.5f);
            ImVec2 nameSize = new ImVec2();
            ImGui.calcTextSize(nameSize, AppConfig.STORE_NAME);
            float nameX = cx + (cw - nameSize.x) / 2;
            float nameY = cy + (ch - nameSize.y) / 2;
            // Text shadow
            dl.addText(nameX + 2, nameY + 2, Theme.toColor(0, 0, 0, cardAnim * 0.3f), AppConfig.STORE_NAME);
            dl.addText(nameX, nameY, Theme.toColor(1, 1, 1, cardAnim), AppConfig.STORE_NAME);
            ImGui.setWindowFontScale(1.0f);

            // Animated shimmer
            float shimmerPos = (float) ((ImGui.getTime() * 0.3) % 2.0 - 0.5);
            float shimmerX = cx + shimmerPos * cw;
            if (shimmerX > cx && shimmerX + 80 < cx + cw) {
                dl.addRectFilledMultiColor(shimmerX, cy, shimmerX + 80, cy + ch,
                        Theme.toColor(1, 1, 1, 0), Theme.toColor(1, 1, 1, 0.04f * cardAnim),
                        Theme.toColor(1, 1, 1, 0), Theme.toColor(1, 1, 1, 0));
            }
        }

        // === POS Button (top-right, top half) ===
        float posAnim = AnimationHelper.staggered(progress, 1, 5, 0.4f);
        posAnim = AnimationHelper.easeOutBack(posAnim);
        renderNavButton(dl, x + leftW + padding, y + (1 - posAnim) * 20,
                rightW / 2 - padding / 2, topH,
                FontAwesomeData.ICON_FA_CASH_REGISTER, "POS",
                Theme.ACCENT, MenuManager.SCREEN_POS, posAnim, "##dash_pos");

        // === Sales Button (top-right, bottom half) ===
        float salesAnim = AnimationHelper.staggered(progress, 2, 5, 0.4f);
        salesAnim = AnimationHelper.easeOutBack(salesAnim);
        renderNavButton(dl, x + leftW + padding + rightW / 2 + padding / 2, y + (1 - salesAnim) * 20,
                rightW / 2 - padding / 2, topH,
                FontAwesomeData.ICON_FA_CHART_BAR, "Sales",
                Theme.WARNING, MenuManager.SCREEN_SALES, salesAnim, "##dash_sales");

        // === Notification Panel (bottom-left) ===
        float notifAnim = AnimationHelper.staggered(progress, 3, 5, 0.4f);
        notifAnim = AnimationHelper.easeOutCubic(notifAnim);
        {
            float slideY = (1 - notifAnim) * 30;
            float nx = x;
            float ny = y + topH + padding + slideY;
            float nw = leftW;
            float nh = bottomH;

            Theme.drawShadow(dl, nx, ny, nw, nh, 16, notifAnim * 0.5f);
            Theme.drawRoundedRect(dl, nx, ny, nw, nh, Theme.toColor(Theme.BG_PANEL, notifAnim), 16);

            // Header
            float headerH = 40;
            Theme.drawRoundedRect(dl, nx, ny, nw, headerH, Theme.toColor(Theme.DANGER, notifAnim * 0.8f), 16);
            // Fix bottom corners of header
            dl.addRectFilled(nx, ny + headerH - 16, nx + nw, ny + headerH,
                    Theme.toColor(Theme.DANGER, notifAnim * 0.8f));

            ImGui.setWindowFontScale(1.2f);
            float bellPulse = AnimationHelper.breathe(2.0f);
            dl.addText(nx + 16, ny + (headerH - 16) / 2,
                    Theme.toColor(1, 1, 1, notifAnim * (0.7f + bellPulse * 0.3f)),
                    FontAwesomeData.ICON_FA_BELL);
            dl.addText(nx + 40, ny + (headerH - 16) / 2,
                    Theme.toColor(1, 1, 1, notifAnim), "Notifications");
            ImGui.setWindowFontScale(1.0f);

            // Notification count badge
            java.util.List<Notification> notifications = NotificationCenter.getInstance().getNotifications();
            String countStr = String.valueOf(notifications.size());
            ImVec2 countSize = new ImVec2();
            ImGui.calcTextSize(countSize, countStr);
            float badgeX = nx + nw - 40;
            float badgeY = ny + (headerH - 20) / 2;
            dl.addCircleFilled(badgeX + 10, badgeY + 10, 12, Theme.toColor(1, 1, 1, notifAnim * 0.3f));
            Theme.drawTextCentered(dl, countStr, badgeX, badgeY, 20, 20, Theme.toColor(1, 1, 1, notifAnim));

            // Notification rows
            float rowH = 36;
            float rowStartY = ny + headerH + 8;

            for (int i = 0; i < notifications.size(); i++) {
                float rowAnim = AnimationHelper.staggered(notifAnim, i, notifications.size(), 0.4f);
                float ry = rowStartY + i * (rowH + 4);
                if (ry + rowH > ny + nh - 8) break; // don't overflow

                Notification notif = notifications.get(i);
                ImVec4 rowColor;
                switch (notif.type) {
                    case "Out of stock": rowColor = Theme.NOTIF_OUT_OF_STOCK; break;
                    case "Near Expiry":  rowColor = Theme.NOTIF_NEAR_EXPIRY;  break;
                    case "Dispose stock":    rowColor = Theme.NOTIF_DISPOSE;  break;
                    case "Defective stock":  rowColor = Theme.NOTIF_DEFECTIVE; break;
                    default: rowColor = Theme.NOTIF_LOW_STOCK; break;
                }
                WidgetHelper.notificationRow(dl, notif.label, notif.type, rowColor,
                        nx + 8, ry, nw - 16, rowH, rowAnim);
            }
        }

        // === Inventory Button (bottom-right, left) ===
        float invAnim = AnimationHelper.staggered(progress, 4, 5, 0.4f);
        invAnim = AnimationHelper.easeOutBack(invAnim);
        renderNavButton(dl, x + leftW + padding, y + topH + padding + (1 - invAnim) * 20,
                rightW / 2 - padding / 2, bottomH,
                FontAwesomeData.ICON_FA_BOXES, "Inventory",
                new ImVec4(0.98f, 0.82f, 0.25f, 1.0f), MenuManager.SCREEN_INVENTORY, invAnim, "##dash_inv");

        // === Settings Button (bottom-right, right) ===
        renderNavButton(dl, x + leftW + padding + rightW / 2 + padding / 2, y + topH + padding + (1 - invAnim) * 20,
                rightW / 2 - padding / 2, bottomH,
                FontAwesomeData.ICON_FA_COG, "Settings",
                Theme.BG_ACTIVE, MenuManager.SCREEN_SETTINGS, invAnim, "##dash_set");
    }

    private void renderNavButton(ImDrawList dl, float x, float y, float w, float h,
                                  String icon, String label, ImVec4 color, int targetScreen,
                                  float animProgress, String id) {
        if (animProgress <= 0) return;

        // Invisible button
        ImGui.setCursorScreenPos(x, y);
        ImGui.invisibleButton(id, w, h);
        boolean hovered = ImGui.isItemHovered();
        if (hovered) ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
        if (ImGui.isItemClicked()) {
            MenuManager.getInstance().navigateTo(targetScreen);
        }

        float hoverScale = hovered ? 0.97f : 1.0f;
        float scaledW = w * hoverScale;
        float scaledH = h * hoverScale;
        float sx = x + (w - scaledW) / 2;
        float sy = y + (h - scaledH) / 2;

        Theme.drawShadow(dl, sx, sy, scaledW, scaledH, 16, animProgress * (hovered ? 0.8f : 0.5f));

        // Card background
        float brighten = hovered ? 0.12f : 0.0f;
        int bgColor = Theme.toColor(
                Math.min(1, color.x + brighten),
                Math.min(1, color.y + brighten),
                Math.min(1, color.z + brighten),
                animProgress);
        Theme.drawRoundedRect(dl, sx, sy, scaledW, scaledH, bgColor, 16);

        // Icon
        ImGui.setWindowFontScale(2.5f);
        Theme.drawTextCentered(dl, icon, sx, sy, scaledW, scaledH * 0.65f,
                Theme.toColor(1, 1, 1, animProgress * 0.9f));
        ImGui.setWindowFontScale(1.0f);

        // Label
        ImGui.setWindowFontScale(1.4f);
        ImVec2 labelSize = new ImVec2();
        ImGui.calcTextSize(labelSize, label);
        float lx = sx + (scaledW - labelSize.x) / 2;
        float ly = sy + scaledH * 0.68f;
        // Text shadow
        dl.addText(lx + 1, ly + 1, Theme.toColor(0, 0, 0, animProgress * 0.3f), label);
        dl.addText(lx, ly, Theme.toColor(1, 1, 1, animProgress), label);
        ImGui.setWindowFontScale(1.0f);
    }
}
