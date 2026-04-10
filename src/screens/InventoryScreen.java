package screens;

import data.AppConfig;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import inventory.InventoryItem;
import inventory.InventoryManager;
import menu.MenuManager;
import resources.FontAwesomeData;
import ui.*;

import java.util.ArrayList;
import java.util.List;

public class InventoryScreen {

    private double enterTime = 0;
    private int selectedCategory = 0;
    private final ImString searchBuffer = new ImString(128);

    // Modal state — shared between Add and Edit modes
    private boolean openModal      = false;
    private String  editingId      = null;   // null = Add mode, non-null = Edit mode
    private String  pendingDeleteId = null;  // set when delete is requested

    private final ImString  formId            = new ImString(32);
    private final ImString  formName          = new ImString(128);
    private final ImString  formPrice         = new ImString(16);
    private final ImString  formStock         = new ImString(8);
    private final ImString  formMfgDate       = new ImString(16);
    private final ImString  formExpiry        = new ImString(16);
    private final ImInt     formCategoryIndex = new ImInt(0);
    private final ImBoolean formShowInPOS     = new ImBoolean(false);
    private final ImInt     formPosCategory   = new ImInt(0);
    private String formError = "";

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        Sidebar.render(MenuManager.SCREEN_INVENTORY);

        ImGui.sameLine();
        float contentW = ImGui.getContentRegionAvailX();
        float contentH = ImGui.getContentRegionAvailY();

        int noScroll = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##inv_content", contentW, contentH, false, noScroll);

        float pad = Math.max(10, contentW * 0.015f);
        ImGui.setCursorPos(pad, pad);

        // Search bar
        WidgetHelper.searchBar("##inv_search", searchBuffer, ImGui.getContentRegionAvailX() - pad);
        ImGui.spacing();

        // Category label + row
        ImGui.setCursorPosX(pad);
        ImGui.textColored(Theme.TEXT_SECONDARY.x, Theme.TEXT_SECONDARY.y, Theme.TEXT_SECONDARY.z, 1, "Category");
        ImGui.spacing();
        ImGui.setCursorPosX(pad);
        float catAnim = AnimationHelper.computeProgress(enterTime, 0.8f);
        selectedCategory = WidgetHelper.categoryRow(AppConfig.INV_CAT_NAMES, AppConfig.INV_CAT_ICONS,
                selectedCategory, catAnim);
        ImGui.spacing();

        // Category title + Add button
        ImGui.setCursorPosX(pad);
        ImGui.setWindowFontScale(1.3f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1,
                AppConfig.INV_CAT_NAMES[selectedCategory]);
        ImGui.setWindowFontScale(1.0f);

        ImGui.sameLine();
        float addBtnW = 110;
        ImGui.setCursorPosX(ImGui.getContentRegionAvailX() - addBtnW + pad);
        ImGui.pushStyleColor(ImGuiCol.Button,        Theme.SUCCESS.x, Theme.SUCCESS.y, Theme.SUCCESS.z, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, Theme.ACCENT.x,  Theme.ACCENT.y,  Theme.ACCENT.z,  1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive,  Theme.ACCENT.x * 0.85f, Theme.ACCENT.y * 0.85f, Theme.ACCENT.z * 0.85f, 1f);
        if (ImGui.button("+ Add Item", addBtnW, 0)) {
            clearForm();
            formCategoryIndex.set(selectedCategory);
            editingId  = null;
            openModal  = true;
        }
        ImGui.popStyleColor(3);
        ImGui.spacing();

        // Table — 8 columns including Actions
        ImGui.setCursorPosX(pad);
        float tableW = ImGui.getContentRegionAvailX() - pad;
        float tableH = ImGui.getContentRegionAvailY() - pad;

        int tableFlags = ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg | ImGuiTableFlags.ScrollY
                | ImGuiTableFlags.Resizable | ImGuiTableFlags.SizingStretchProp;

        if (ImGui.beginTable("##inv_table", 8, tableFlags, tableW, tableH)) {
            ImGui.tableSetupColumn("No.",               ImGuiTableColumnFlags.None, 0.4f);
            ImGui.tableSetupColumn("Mfg. Date",         ImGuiTableColumnFlags.None, 1.2f);
            ImGui.tableSetupColumn("Expiry Date",       ImGuiTableColumnFlags.None, 1.2f);
            ImGui.tableSetupColumn("Product ID",        ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Product Name",      ImGuiTableColumnFlags.None, 2.0f);
            ImGui.tableSetupColumn("Unit Price",        ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Stock",             ImGuiTableColumnFlags.None, 0.7f);
            ImGui.tableSetupColumn("Actions",           ImGuiTableColumnFlags.NoResize, 0.8f);
            ImGui.tableHeadersRow();

            String searchStr = searchBuffer.get().toLowerCase().trim();
            float tableAnim = AnimationHelper.computeProgress(enterTime, 0.6f);

            List<InventoryItem> visible = new ArrayList<>();
            for (InventoryItem item : InventoryManager.getInstance().getItems().values()) {
                if (item.categoryIndex != selectedCategory) continue;
                if (!searchStr.isEmpty()) {
                    boolean match = item.productId.toLowerCase().contains(searchStr)
                            || item.name.toLowerCase().contains(searchStr);
                    if (!match) continue;
                }
                visible.add(item);
            }

            for (int i = 0; i < visible.size(); i++) {
                InventoryItem item = visible.get(i);
                float rowAnim = AnimationHelper.staggered(tableAnim, i, 10, 0.5f);
                if (rowAnim <= 0) continue;

                ImGui.tableNextRow();
                ImGui.tableNextColumn(); ImGui.text(String.valueOf(i + 1));
                ImGui.tableNextColumn(); ImGui.text(item.manufacturedDate);
                ImGui.tableNextColumn(); ImGui.text(item.expiryDate);
                ImGui.tableNextColumn(); ImGui.text(item.productId);
                ImGui.tableNextColumn(); ImGui.text(item.name);
                ImGui.tableNextColumn(); ImGui.text(String.format("%.2f", item.unitPrice));

                // Stock — colour-coded
                ImGui.tableNextColumn();
                if (item.stock <= 0) {
                    ImGui.textColored(Theme.DANGER.x,  Theme.DANGER.y,  Theme.DANGER.z,  1, String.valueOf(item.stock));
                } else if (item.stock < 20) {
                    ImGui.textColored(Theme.WARNING.x, Theme.WARNING.y, Theme.WARNING.z, 1, String.valueOf(item.stock));
                } else {
                    ImGui.textColored(Theme.SUCCESS.x, Theme.SUCCESS.y, Theme.SUCCESS.z, 1, String.valueOf(item.stock));
                }

                // Actions — Edit + Delete
                ImGui.tableNextColumn();
                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4, 2);

                // Edit button
                ImGui.pushStyleColor(ImGuiCol.Button,        Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 0.7f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 1f);
                if (ImGui.smallButton(FontAwesomeData.ICON_FA_EDIT + "##edit_" + item.productId)) {
                    populateFormForEdit(item);
                    editingId = item.productId;
                    openModal = true;
                }
                ImGui.popStyleColor(2);

                ImGui.sameLine();

                // Delete button
                ImGui.pushStyleColor(ImGuiCol.Button,        Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 0.7f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 1f);
                if (ImGui.smallButton(FontAwesomeData.ICON_FA_TRASH + "##del_" + item.productId)) {
                    pendingDeleteId = item.productId;
                }
                ImGui.popStyleColor(2);

                ImGui.popStyleVar();
            }

            ImGui.endTable();
        }

        // Trigger popups outside the table so the ID stack matches
        if (openModal) {
            ImGui.openPopup("##item_modal");
            openModal = false;
        }
        if (pendingDeleteId != null && !ImGui.isPopupOpen("Confirm Delete")) {
            ImGui.openPopup("Confirm Delete");
        }

        renderItemModal();
        renderDeleteConfirm();

        ImGui.endChild();
    }

    // -------------------------------------------------------------------------
    // Add / Edit modal
    // -------------------------------------------------------------------------

    private void renderItemModal() {
        ImGui.setNextWindowSize(420, 0);
        ImBoolean open = new ImBoolean(true);
        if (!ImGui.beginPopupModal("##item_modal", open, ImGuiWindowFlags.NoResize)) return;

        boolean isEdit = editingId != null;

        ImGui.setWindowFontScale(1.2f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1,
                isEdit ? "Edit Item" : "Add Item");
        ImGui.setWindowFontScale(1.0f);
        ImGui.separator();
        ImGui.spacing();

        // Product ID — read-only when editing
        ImGui.text("Product ID");
        ImGui.setNextItemWidth(-1);
        if (isEdit) {
            ImGui.pushStyleColor(ImGuiCol.FrameBg, Theme.BG_ACTIVE.x, Theme.BG_ACTIVE.y, Theme.BG_ACTIVE.z, 1f);
            ImGui.inputText("##fid", formId, imgui.flag.ImGuiInputTextFlags.ReadOnly);
            ImGui.popStyleColor();
        } else {
            ImGui.inputText("##fid", formId);
        }
        ImGui.spacing();

        // Product Name
        ImGui.text("Product Name");
        ImGui.setNextItemWidth(-1);
        ImGui.inputText("##fname", formName);
        ImGui.spacing();

        // Category
        ImGui.text("Inventory Category");
        ImGui.setNextItemWidth(-1);
        ImGui.combo("##fcat", formCategoryIndex, AppConfig.INV_CAT_NAMES);
        ImGui.spacing();

        // Unit Price + Stock
        float halfW = (ImGui.getContentRegionAvailX() - ImGui.getStyle().getItemSpacingX()) / 2;
        ImGui.text("Unit Price");
        ImGui.setNextItemWidth(halfW);
        ImGui.inputText("##fprice", formPrice);
        ImGui.sameLine();
        ImGui.text("Stock");
        ImGui.sameLine();
        ImGui.setNextItemWidth(halfW);
        ImGui.inputText("##fstock", formStock);
        ImGui.spacing();

        // Mfg Date + Expiry Date
        ImGui.text("Manufactured Date");
        ImGui.setNextItemWidth(halfW);
        ImGui.inputText("##fmfg", formMfgDate);
        ImGui.sameLine();
        ImGui.text("Expiry Date");
        ImGui.sameLine();
        ImGui.setNextItemWidth(halfW);
        ImGui.inputText("##fexpiry", formExpiry);
        ImGui.spacing();

        // Show in POS
        ImGui.checkbox("Show in POS", formShowInPOS);
        if (formShowInPOS.get()) {
            ImGui.sameLine();
            ImGui.setNextItemWidth(-1);
            ImGui.combo("##fposcat", formPosCategory, AppConfig.POS_CAT_NAMES);
        }

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        if (!formError.isEmpty()) {
            ImGui.textColored(Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 1, formError);
            ImGui.spacing();
        }

        float btnW = (ImGui.getContentRegionAvailX() - ImGui.getStyle().getItemSpacingX()) / 2;

        ImGui.pushStyleColor(ImGuiCol.Button,        Theme.SUCCESS.x, Theme.SUCCESS.y, Theme.SUCCESS.z, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, Theme.ACCENT.x,  Theme.ACCENT.y,  Theme.ACCENT.z,  1f);
        if (ImGui.button("Save", btnW, 0)) {
            if (trySave()) ImGui.closeCurrentPopup();
        }
        ImGui.popStyleColor(2);

        ImGui.sameLine();
        if (ImGui.button("Cancel", btnW, 0)) {
            formError = "";
            ImGui.closeCurrentPopup();
        }

        ImGui.spacing();
        ImGui.endPopup();
    }

    // -------------------------------------------------------------------------
    // Delete confirmation
    // -------------------------------------------------------------------------

    private void renderDeleteConfirm() {
        ImGui.setNextWindowSize(300, 0);
        if (!ImGui.beginPopupModal("Confirm Delete", new ImBoolean(true), ImGuiWindowFlags.NoResize)) return;

        ImGui.spacing();
        ImGui.textColored(Theme.WARNING.x, Theme.WARNING.y, Theme.WARNING.z, 1,
                "Delete \"" + pendingDeleteId + "\"?");
        ImGui.text("This cannot be undone.");
        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        float btnW = (ImGui.getContentRegionAvailX() - ImGui.getStyle().getItemSpacingX()) / 2;

        ImGui.pushStyleColor(ImGuiCol.Button,        Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, Theme.DANGER.x, Theme.DANGER.y, Theme.DANGER.z, 0.8f);
        if (ImGui.button("Delete", btnW, 0)) {
            InventoryManager.getInstance().removeItem(pendingDeleteId);
            pendingDeleteId = null;
            ImGui.closeCurrentPopup();
        }
        ImGui.popStyleColor(2);

        ImGui.sameLine();
        if (ImGui.button("Cancel", btnW, 0)) {
            pendingDeleteId = null;
            ImGui.closeCurrentPopup();
        }

        ImGui.spacing();
        ImGui.endPopup();
    }

    // -------------------------------------------------------------------------
    // Save logic
    // -------------------------------------------------------------------------

    private boolean trySave() {
        String id     = formId.get().trim();
        String name   = formName.get().trim();
        String price  = formPrice.get().trim();
        String stock  = formStock.get().trim();
        String mfg    = formMfgDate.get().trim();
        String expiry = formExpiry.get().trim();

        if (id.isEmpty())    { formError = "Product ID is required.";   return false; }
        if (name.isEmpty())  { formError = "Product Name is required."; return false; }
        if (price.isEmpty()) { formError = "Unit Price is required.";   return false; }
        if (stock.isEmpty()) { formError = "Stock is required.";        return false; }

        // Only check duplicate ID when adding a new item
        if (editingId == null && InventoryManager.getInstance().containsId(id)) {
            formError = "Product ID \"" + id + "\" already exists.";
            return false;
        }

        double unitPrice;
        int stockQty;
        try { unitPrice = Double.parseDouble(price); }
        catch (NumberFormatException e) { formError = "Unit Price must be a valid number."; return false; }
        try { stockQty = Integer.parseInt(stock); }
        catch (NumberFormatException e) { formError = "Stock must be a whole number."; return false; }

        if (unitPrice < 0) { formError = "Unit Price cannot be negative."; return false; }
        if (stockQty  < 0) { formError = "Stock cannot be negative.";      return false; }

        int posCategory = formShowInPOS.get() ? formPosCategory.get() : -1;
        InventoryItem item = new InventoryItem(
                id, name, unitPrice, stockQty, formCategoryIndex.get(),
                mfg.isEmpty()    ? "N/A" : mfg,
                expiry.isEmpty() ? "N/A" : expiry,
                posCategory
        );

        if (editingId != null) {
            InventoryManager.getInstance().updateItem(item);
        } else {
            InventoryManager.getInstance().addItem(item);
        }

        formError = "";
        return true;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void populateFormForEdit(InventoryItem item) {
        clearForm();
        formId.set(item.productId);
        formName.set(item.name);
        formPrice.set(String.valueOf(item.unitPrice));
        formStock.set(String.valueOf(item.stock));
        formMfgDate.set(item.manufacturedDate.equals("N/A") ? "" : item.manufacturedDate);
        formExpiry.set(item.expiryDate.equals("N/A")        ? "" : item.expiryDate);
        formCategoryIndex.set(item.categoryIndex);
        if (item.posCategory >= 0) {
            formShowInPOS.set(true);
            formPosCategory.set(item.posCategory);
        }
    }

    private void clearForm() {
        formId.set("");
        formName.set("");
        formPrice.set("");
        formStock.set("");
        formMfgDate.set("");
        formExpiry.set("");
        formCategoryIndex.set(0);
        formShowInPOS.set(false);
        formPosCategory.set(0);
        formError = "";
    }
}
