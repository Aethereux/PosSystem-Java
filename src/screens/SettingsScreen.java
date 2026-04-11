package screens;

import data.AppConfig;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import inventory.NotificationCenter;
import menu.MenuManager;
import resources.FontAwesomeData;
import ui.*;

public class SettingsScreen {

    private double enterTime = 0;
    private final ImString storeName    = new ImString(AppConfig.STORE_NAME, 128);
    private final ImString storeAddress = new ImString("Unit 205, SM Mall, Quezon City", 256);
    private final ImBoolean darkMode      = new ImBoolean(Theme.isDarkMode);
    private final ImBoolean notifications = new ImBoolean(NotificationCenter.getInstance().isEnabled());
    private final int[]     fontSize      = {18};

    private static final float FONT_HIREZ = 42.0f;

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        Sidebar.render(MenuManager.SCREEN_SETTINGS);

        ImGui.sameLine();
        float contentW = ImGui.getContentRegionAvailX();
        float contentH = ImGui.getContentRegionAvailY();

        ImGui.beginChild("##settings_content", contentW, contentH);

        float pad = Math.max(10, contentW * 0.015f);
        ImGui.setCursorPos(pad, pad);


        ImGui.setWindowFontScale(1.5f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1,
                FontAwesomeData.ICON_FA_COG + "  Settings");
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();
        ImGui.spacing();

        int headerFlags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Framed;


        if (ImGui.collapsingHeader(FontAwesomeData.ICON_FA_STORE + "  Store Settings", headerFlags)) {
            ImGui.indent(16);
            ImGui.spacing();

            ImGui.text("Store Name");
            ImGui.pushItemWidth(contentW * 0.5f);
            ImGui.inputText("##store_name", storeName, ImGuiInputTextFlags.ReadOnly);
            ImGui.popItemWidth();
            ImGui.spacing();

            ImGui.text("Store Address");
            ImGui.pushItemWidth(contentW * 0.5f);
            ImGui.inputText("##store_address", storeAddress, ImGuiInputTextFlags.ReadOnly);
            ImGui.popItemWidth();
            ImGui.spacing();

            ImGui.text("Staff: " + AppConfig.STAFF_NAME);
            ImGui.spacing();
            ImGui.spacing();

            ImGui.unindent(16);
        }

        ImGui.spacing();


        if (ImGui.collapsingHeader(FontAwesomeData.ICON_FA_PALETTE + "  Display Settings", headerFlags)) {
            ImGui.indent(16);
            ImGui.spacing();


            boolean prevDark = darkMode.get();
            ImGui.checkbox("Dark Mode", darkMode);
            if (darkMode.get() != prevDark) {
                Theme.setDarkMode(darkMode.get());
            }
            ImGui.spacing();


            boolean prevNotif = notifications.get();
            ImGui.checkbox("Enable Notifications", notifications);
            if (notifications.get() != prevNotif) {
                NotificationCenter.getInstance().setEnabled(notifications.get());
            }
            ImGui.spacing();


            ImGui.text("Font Size");
            ImGui.pushItemWidth(contentW * 0.3f);
            if (ImGui.sliderInt("##fontsize", fontSize, 12, 28)) {
                ImGui.getIO().setFontGlobalScale(fontSize[0] / FONT_HIREZ);
            }
            ImGui.popItemWidth();
            ImGui.sameLine();
            ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1,
                    fontSize[0] + "px");
            ImGui.spacing();
            ImGui.spacing();

            ImGui.unindent(16);
        }

        ImGui.spacing();


        if (ImGui.collapsingHeader(FontAwesomeData.ICON_FA_INFO_CIRCLE + "  About", headerFlags)) {
            ImGui.indent(16);
            ImGui.spacing();

            ImGui.text("POS Inventory System");
            ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1, "Version 1.0.0");
            ImGui.spacing();
            ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1,
                    "Built with ImGui Java + LWJGL3");
            ImGui.spacing();
            ImGui.textColored(Theme.TEXT_SECONDARY.x, Theme.TEXT_SECONDARY.y, Theme.TEXT_SECONDARY.z, 1,
                    "Runtime: " + System.getProperty("java.version"));
            ImGui.spacing();

            ImGui.unindent(16);
        }

        ImGui.endChild();
    }
}
