package screens;

import data.SampleData;
import imgui.ImGui;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImString;
import menu.MenuManager;
import ui.*;

public class InventoryScreen {

    private double enterTime = 0;
    private int selectedCategory = 0;
    private final ImString searchBuffer = new ImString(128);

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        // Sidebar
        Sidebar.render(MenuManager.SCREEN_INVENTORY);

        // Content area
        ImGui.sameLine();
        float contentW = ImGui.getContentRegionAvailX();
        float contentH = ImGui.getContentRegionAvailY();

        int noScroll = imgui.flag.ImGuiWindowFlags.NoScrollbar | imgui.flag.ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##inv_content", contentW, contentH, false, noScroll);

        float pad = Math.max(10, contentW * 0.015f);
        ImGui.setCursorPos(pad, pad);

        // Search bar
        float searchW = ImGui.getContentRegionAvailX() - pad;
        WidgetHelper.searchBar("##inv_search", searchBuffer, searchW);
        ImGui.spacing();

        // Category label
        ImGui.setCursorPosX(pad);
        ImGui.textColored(Theme.TEXT_SECONDARY.x, Theme.TEXT_SECONDARY.y, Theme.TEXT_SECONDARY.z, 1, "Category");
        ImGui.spacing();

        // Category row
        ImGui.setCursorPosX(pad);
        float catAnim = AnimationHelper.computeProgress(enterTime, 0.8f);
        selectedCategory = WidgetHelper.categoryRow(SampleData.INV_CAT_NAMES, SampleData.INV_CAT_ICONS,
                selectedCategory, catAnim);

        ImGui.spacing();

        // Category title
        ImGui.setCursorPosX(pad);
        ImGui.setWindowFontScale(1.3f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1,
                SampleData.INV_CAT_NAMES[selectedCategory]);
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();

        // Table
        ImGui.setCursorPosX(pad);
        float tableW = ImGui.getContentRegionAvailX() - pad;
        float tableH = ImGui.getContentRegionAvailY() - pad;

        int tableFlags = ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg | ImGuiTableFlags.ScrollY
                | ImGuiTableFlags.Resizable | ImGuiTableFlags.SizingStretchProp;

        if (ImGui.beginTable("##inv_table", 7, tableFlags, tableW, tableH)) {
            ImGui.tableSetupColumn("No.", ImGuiTableColumnFlags.None, 0.5f);
            ImGui.tableSetupColumn("Manufactured Date", ImGuiTableColumnFlags.None, 1.5f);
            ImGui.tableSetupColumn("Expiry Date", ImGuiTableColumnFlags.None, 1.5f);
            ImGui.tableSetupColumn("Product ID", ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Product Name", ImGuiTableColumnFlags.None, 2.0f);
            ImGui.tableSetupColumn("Unit Price", ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Stock", ImGuiTableColumnFlags.None, 0.8f);
            ImGui.tableHeadersRow();

            String searchStr = searchBuffer.get().toLowerCase().trim();
            float tableAnim = AnimationHelper.computeProgress(enterTime, 0.6f);
            int visibleRow = 0;

            for (String[] row : SampleData.INVENTORY) {
                int catIdx = Integer.parseInt(row[7]);
                if (catIdx != selectedCategory) continue;

                if (!searchStr.isEmpty()) {
                    boolean match = row[3].toLowerCase().contains(searchStr)
                            || row[4].toLowerCase().contains(searchStr);
                    if (!match) continue;
                }

                float rowAnim = AnimationHelper.staggered(tableAnim, visibleRow, 10, 0.5f);
                if (rowAnim <= 0) { visibleRow++; continue; }

                ImGui.tableNextRow();
                ImGui.tableNextColumn(); ImGui.text(row[0]);
                ImGui.tableNextColumn(); ImGui.text(row[1]);
                ImGui.tableNextColumn(); ImGui.text(row[2]);
                ImGui.tableNextColumn(); ImGui.text(row[3]);
                ImGui.tableNextColumn(); ImGui.text(row[4]);
                ImGui.tableNextColumn(); ImGui.text(row[5]);

                ImGui.tableNextColumn();
                int stock = Integer.parseInt(row[6]);
                if (stock <= 0) {
                    ImGui.textColored(Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 1, row[6]);
                } else if (stock < 20) {
                    ImGui.textColored(Theme.WARNING.x, Theme.WARNING.y, Theme.WARNING.z, 1, row[6]);
                } else {
                    ImGui.textColored(Theme.SUCCESS.x, Theme.SUCCESS.y, Theme.SUCCESS.z, 1, row[6]);
                }

                visibleRow++;
            }

            ImGui.endTable();
        }

        ImGui.endChild();
    }
}
