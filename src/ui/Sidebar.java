package ui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import menu.MenuManager;
import resources.FontAwesomeData;

public class Sidebar {

    private static final String[] NAV_ICONS = {
        FontAwesomeData.ICON_FA_CASH_REGISTER,
        FontAwesomeData.ICON_FA_CHART_BAR,
        FontAwesomeData.ICON_FA_BOXES,
        FontAwesomeData.ICON_FA_COG,
    };
    private static final String[] NAV_LABELS = {"POS", "Sales", "Inventory", "Settings"};
    private static final int[] NAV_SCREENS = {
        MenuManager.SCREEN_POS,
        MenuManager.SCREEN_SALES,
        MenuManager.SCREEN_INVENTORY,
        MenuManager.SCREEN_SETTINGS,
    };

    private static final float[] hoverAnim = new float[4];
    private static float homeHoverAnim = 0;

    public static float getWidth(float windowWidth) {
        return Math.max(60, Math.min(90, windowWidth * 0.06f));
    }

    public static float render(int activeScreen) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        float WIDTH = getWidth(windowSize.x);
        float h = windowSize.y;

        ImFont font = ImGui.getFont();
        float globalScale = ImGui.getIO().getFontGlobalScale();
        float effectiveFontSize = font.getFontSize() * globalScale;




        int smallFontSize = (int)(effectiveFontSize * 0.7f);
        int iconFontSize = (int)(effectiveFontSize * 1.2f);
        int homeIconSize = (int)(effectiveFontSize * 1.3f);


        ImGui.setCursorPos(0, 0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        int childFlags = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##sidebar_child", WIDTH, h, false, childFlags);

        ImDrawList dl = ImGui.getWindowDrawList();
        ImVec2 childPos = new ImVec2();
        ImGui.getWindowPos(childPos);
        float x = childPos.x;
        float y = childPos.y;


        Theme.drawRoundedRect(dl, x, y, WIDTH, h, Theme.toColor(Theme.SIDEBAR_BG), 0);


        dl.addRectFilled(x + WIDTH - 1, y, x + WIDTH, y + h, Theme.toColor(Theme.BG_HOVER, 0.3f));

        float pad = WIDTH * 0.12f;
        float btnSize = WIDTH - pad * 2;
        float btnX = x + pad;


        float homeY = y + pad;
        ImGui.setCursorScreenPos(btnX, homeY);
        ImGui.invisibleButton("##sidebar_home", btnSize, btnSize);
        boolean homeHovered = ImGui.isItemHovered();
        if (homeHovered) ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
        if (ImGui.isItemClicked()) {
            MenuManager.getInstance().navigateTo(MenuManager.SCREEN_DASHBOARD);
        }

        float homeTarget = homeHovered ? 1.0f : 0.0f;
        homeHoverAnim += (homeTarget - homeHoverAnim) * 0.15f;

        Theme.drawRoundedRect(dl, btnX, homeY, btnSize, btnSize,
                Theme.toColor(Theme.BG_HOVER, homeHoverAnim * 0.8f), 12);


        ImVec2 iconSize = new ImVec2();
        font.calcTextSizeA(iconSize, homeIconSize, Float.MAX_VALUE, -1, FontAwesomeData.ICON_FA_HOME);
        dl.addText(font, homeIconSize,
                btnX + (btnSize - iconSize.x) / 2, homeY + (btnSize - iconSize.y) / 2,
                Theme.toColor(Theme.TEXT_PRIMARY, 0.6f + homeHoverAnim * 0.4f),
                FontAwesomeData.ICON_FA_HOME);


        float sepY = homeY + btnSize + pad;
        dl.addLine(x + pad, sepY, x + WIDTH - pad, sepY, Theme.toColor(Theme.BG_HOVER, 0.5f), 1);


        float navStartY = sepY + pad;
        float availH = h - (navStartY - y) - pad;
        float itemSpacing = 6;
        float itemHeight = Math.min(62, (availH - itemSpacing * (NAV_ICONS.length - 1)) / NAV_ICONS.length);

        for (int i = 0; i < NAV_ICONS.length; i++) {
            float itemY = navStartY + i * (itemHeight + itemSpacing);
            boolean isActive = activeScreen == NAV_SCREENS[i];

            ImGui.setCursorScreenPos(btnX, itemY);
            ImGui.invisibleButton("##sidebar_" + i, btnSize, itemHeight);
            boolean hovered = ImGui.isItemHovered();
            if (hovered) ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            if (ImGui.isItemClicked()) {
                MenuManager.getInstance().navigateTo(NAV_SCREENS[i]);
            }

            float target = (hovered || isActive) ? 1.0f : 0.0f;
            hoverAnim[i] += (target - hoverAnim[i]) * 0.15f;


            if (isActive || hoverAnim[i] > 0.05f) {
                float barH = itemHeight * 0.45f * hoverAnim[i];
                float barY = itemY + (itemHeight - barH) / 2;
                dl.addRectFilled(x + 2, barY, x + 5, barY + barH,
                        Theme.toColor(Theme.PRIMARY, hoverAnim[i]), 2);
            }


            Theme.drawRoundedRect(dl, btnX, itemY, btnSize, itemHeight,
                    Theme.toColor(Theme.BG_HOVER, hoverAnim[i] * 0.4f), 12);


            float iconAlpha = isActive ? 1.0f : (0.5f + hoverAnim[i] * 0.3f);
            int iconColor = isActive ? Theme.toColor(Theme.PRIMARY)
                    : Theme.toColor(Theme.TEXT_PRIMARY, iconAlpha);

            ImVec2 icoSize = new ImVec2();
            font.calcTextSizeA(icoSize, iconFontSize, Float.MAX_VALUE, -1, NAV_ICONS[i]);
            dl.addText(font, iconFontSize,
                    btnX + (btnSize - icoSize.x) / 2, itemY + itemHeight * 0.15f,
                    iconColor, NAV_ICONS[i]);


            float labelAlpha = isActive ? 1.0f : (0.4f + hoverAnim[i] * 0.4f);
            int labelColor = isActive ? Theme.toColor(Theme.PRIMARY, labelAlpha)
                    : Theme.toColor(Theme.TEXT_MUTED, labelAlpha);

            ImVec2 lblSize = new ImVec2();
            font.calcTextSizeA(lblSize, smallFontSize, Float.MAX_VALUE, -1, NAV_LABELS[i]);
            dl.addText(font, smallFontSize,
                    x + (WIDTH - lblSize.x) / 2, itemY + itemHeight - lblSize.y - 4,
                    labelColor, NAV_LABELS[i]);
        }

        ImGui.endChild();
        ImGui.popStyleVar(2);


        ImGui.setCursorPos(WIDTH, 0);

        return WIDTH;
    }
}
