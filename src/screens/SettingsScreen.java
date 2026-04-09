package screens;

import data.SampleData;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import menu.MenuManager;
import resources.FontAwesomeData;
import ui.*;

public class SettingsScreen {

    private double enterTime = 0;
    private final ImString storeName = new ImString(SampleData.STORE_NAME, 128);
    private final ImString storeAddress = new ImString("Unit 205, SM Mall, Quezon City", 256);
    private final ImBoolean darkMode = new ImBoolean(true);
    private final ImBoolean notifications = new ImBoolean(true);
    private final ImBoolean sounds = new ImBoolean(false);

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        // Sidebar
        Sidebar.render(MenuManager.SCREEN_SETTINGS);

        // Content area
        ImGui.sameLine();
        float contentW = ImGui.getContentRegionAvailX();
        float contentH = ImGui.getContentRegionAvailY();

        ImGui.beginChild("##settings_content", contentW, contentH);

        float pad = Math.max(10, contentW * 0.015f);
        ImGui.setCursorPos(pad, pad);

        // Title
        ImGui.setWindowFontScale(1.5f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1,
                FontAwesomeData.ICON_FA_COG + "  Settings");
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();
        ImGui.spacing();

        // Store Settings
        int headerFlags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Framed;
        if (ImGui.collapsingHeader(FontAwesomeData.ICON_FA_STORE + "  Store Settings", headerFlags)) {
            ImGui.indent(16);
            ImGui.spacing();

            ImGui.text("Store Name");
            ImGui.pushItemWidth(contentW * 0.5f);
            ImGui.inputText("##store_name", storeName);
            ImGui.popItemWidth();
            ImGui.spacing();

            ImGui.text("Store Address");
            ImGui.pushItemWidth(contentW * 0.5f);
            ImGui.inputText("##store_address", storeAddress);
            ImGui.popItemWidth();
            ImGui.spacing();

            ImGui.text("Staff: " + SampleData.STAFF_NAME);
            ImGui.spacing();
            ImGui.spacing();

            ImGui.unindent(16);
        }

        ImGui.spacing();

        // Display Settings
        if (ImGui.collapsingHeader(FontAwesomeData.ICON_FA_PALETTE + "  Display Settings", headerFlags)) {
            ImGui.indent(16);
            ImGui.spacing();

            ImGui.checkbox("Dark Mode", darkMode);
            ImGui.spacing();
            ImGui.checkbox("Enable Notifications", notifications);
            ImGui.spacing();
            ImGui.checkbox("Sound Effects", sounds);
            ImGui.spacing();

            ImGui.text("Font Size");
            ImGui.sameLine();
            ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1, "(Default)");
            ImGui.spacing();
            ImGui.spacing();

            ImGui.unindent(16);
        }

        ImGui.spacing();

        // About
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
