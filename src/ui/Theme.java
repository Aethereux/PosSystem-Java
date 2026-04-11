package ui;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
public class Theme {



    public static final ImVec4 PRIMARY = new ImVec4(0.29f, 0.56f, 0.89f, 1.0f);
    public static final ImVec4 PRIMARY_DARK = new ImVec4(0.22f, 0.45f, 0.75f, 1.0f);
    public static final ImVec4 PRIMARY_LIGHT = new ImVec4(0.45f, 0.68f, 0.95f, 1.0f);


    public static final ImVec4 ACCENT = new ImVec4(0.35f, 0.82f, 0.63f, 1.0f);
    public static final ImVec4 SUCCESS = new ImVec4(0.30f, 0.78f, 0.47f, 1.0f);
    public static final ImVec4 WARNING = new ImVec4(0.98f, 0.75f, 0.18f, 1.0f);
    public static final ImVec4 DANGER = new ImVec4(0.91f, 0.30f, 0.24f, 1.0f);
    public static final ImVec4 INFO = new ImVec4(0.20f, 0.71f, 0.90f, 1.0f);


    public static final ImVec4 BG_DARK = new ImVec4(0.11f, 0.12f, 0.14f, 1.0f);
    public static final ImVec4 BG_PANEL = new ImVec4(0.15f, 0.16f, 0.19f, 1.0f);
    public static final ImVec4 BG_CARD = new ImVec4(0.19f, 0.20f, 0.24f, 1.0f);
    public static final ImVec4 BG_HOVER = new ImVec4(0.24f, 0.25f, 0.30f, 1.0f);
    public static final ImVec4 BG_ACTIVE = new ImVec4(0.28f, 0.30f, 0.36f, 1.0f);


    public static final ImVec4 TEXT_PRIMARY = new ImVec4(0.95f, 0.95f, 0.97f, 1.0f);
    public static final ImVec4 TEXT_SECONDARY = new ImVec4(0.65f, 0.67f, 0.72f, 1.0f);
    public static final ImVec4 TEXT_MUTED = new ImVec4(0.45f, 0.47f, 0.52f, 1.0f);


    public static final ImVec4 SIDEBAR_BG = new ImVec4(0.09f, 0.10f, 0.12f, 1.0f);
    public static final ImVec4 SIDEBAR_ITEM = new ImVec4(0.18f, 0.19f, 0.23f, 1.0f);
    public static final ImVec4 SIDEBAR_HOVER = new ImVec4(0.22f, 0.24f, 0.29f, 1.0f);


    public static final ImVec4[] CHART_COLORS = {
        new ImVec4(0.29f, 0.56f, 0.89f, 1.0f),
        new ImVec4(0.35f, 0.82f, 0.63f, 1.0f),
        new ImVec4(0.98f, 0.75f, 0.18f, 1.0f),
        new ImVec4(0.91f, 0.30f, 0.24f, 1.0f),
        new ImVec4(0.60f, 0.40f, 0.80f, 1.0f),
        new ImVec4(0.98f, 0.50f, 0.20f, 1.0f),
        new ImVec4(0.20f, 0.71f, 0.90f, 1.0f),
        new ImVec4(0.85f, 0.45f, 0.65f, 1.0f),
    };


    public static final ImVec4 NOTIF_LOW_STOCK = new ImVec4(0.98f, 0.75f, 0.18f, 0.15f);
    public static final ImVec4 NOTIF_OUT_OF_STOCK = new ImVec4(0.91f, 0.30f, 0.24f, 0.20f);
    public static final ImVec4 NOTIF_DISPOSE = new ImVec4(0.60f, 0.40f, 0.80f, 0.15f);
    public static final ImVec4 NOTIF_DEFECTIVE = new ImVec4(0.98f, 0.50f, 0.20f, 0.15f);
    public static final ImVec4 NOTIF_NEAR_EXPIRY = new ImVec4(0.20f, 0.71f, 0.90f, 0.15f);

    public static boolean isDarkMode = true;

    public static void setDarkMode(boolean dark) {
        isDarkMode = dark;
        styleApplied = false;
        if (dark) {
            BG_DARK.x = 0.11f;      BG_DARK.y = 0.12f;      BG_DARK.z = 0.14f;
            BG_PANEL.x = 0.15f;     BG_PANEL.y = 0.16f;     BG_PANEL.z = 0.19f;
            BG_CARD.x = 0.19f;      BG_CARD.y = 0.20f;      BG_CARD.z = 0.24f;
            BG_HOVER.x = 0.24f;     BG_HOVER.y = 0.25f;     BG_HOVER.z = 0.30f;
            BG_ACTIVE.x = 0.28f;    BG_ACTIVE.y = 0.30f;    BG_ACTIVE.z = 0.36f;
            TEXT_PRIMARY.x = 0.95f;   TEXT_PRIMARY.y = 0.95f;   TEXT_PRIMARY.z = 0.97f;
            TEXT_SECONDARY.x = 0.65f; TEXT_SECONDARY.y = 0.67f; TEXT_SECONDARY.z = 0.72f;
            TEXT_MUTED.x = 0.45f;     TEXT_MUTED.y = 0.47f;     TEXT_MUTED.z = 0.52f;
            SIDEBAR_BG.x = 0.09f;   SIDEBAR_BG.y = 0.10f;   SIDEBAR_BG.z = 0.12f;
            SIDEBAR_ITEM.x = 0.18f; SIDEBAR_ITEM.y = 0.19f; SIDEBAR_ITEM.z = 0.23f;
            SIDEBAR_HOVER.x = 0.22f; SIDEBAR_HOVER.y = 0.24f; SIDEBAR_HOVER.z = 0.29f;
        } else {
            BG_DARK.x = 0.93f;      BG_DARK.y = 0.93f;      BG_DARK.z = 0.95f;
            BG_PANEL.x = 0.96f;     BG_PANEL.y = 0.96f;     BG_PANEL.z = 0.97f;
            BG_CARD.x = 1.00f;      BG_CARD.y = 1.00f;      BG_CARD.z = 1.00f;
            BG_HOVER.x = 0.90f;     BG_HOVER.y = 0.91f;     BG_HOVER.z = 0.93f;
            BG_ACTIVE.x = 0.85f;    BG_ACTIVE.y = 0.86f;    BG_ACTIVE.z = 0.89f;
            TEXT_PRIMARY.x = 0.10f;   TEXT_PRIMARY.y = 0.11f;   TEXT_PRIMARY.z = 0.12f;
            TEXT_SECONDARY.x = 0.35f; TEXT_SECONDARY.y = 0.36f; TEXT_SECONDARY.z = 0.42f;
            TEXT_MUTED.x = 0.55f;     TEXT_MUTED.y = 0.56f;     TEXT_MUTED.z = 0.62f;
            SIDEBAR_BG.x = 0.89f;   SIDEBAR_BG.y = 0.89f;   SIDEBAR_BG.z = 0.91f;
            SIDEBAR_ITEM.x = 0.92f; SIDEBAR_ITEM.y = 0.92f; SIDEBAR_ITEM.z = 0.93f;
            SIDEBAR_HOVER.x = 0.85f; SIDEBAR_HOVER.y = 0.85f; SIDEBAR_HOVER.z = 0.87f;
        }
    }

    private static boolean styleApplied = false;

    public static void applyModernTheme() {
        if (!styleApplied) {
            var style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setFrameRounding(8.0f);
            style.setGrabRounding(6.0f);
            style.setTabRounding(6.0f);
            style.setChildRounding(8.0f);
            style.setPopupRounding(8.0f);
            style.setScrollbarRounding(8.0f);
            style.setWindowPadding(16, 16);
            style.setFramePadding(10, 6);
            style.setItemSpacing(10, 8);
            style.setItemInnerSpacing(8, 6);
            style.setScrollbarSize(10);
            styleApplied = true;
        }


        ImGui.pushStyleColor(ImGuiCol.WindowBg, BG_DARK.x, BG_DARK.y, BG_DARK.z, BG_DARK.w);
        ImGui.pushStyleColor(ImGuiCol.ChildBg, BG_PANEL.x, BG_PANEL.y, BG_PANEL.z, 0.0f);


        ImGui.pushStyleColor(ImGuiCol.FrameBg, BG_CARD.x, BG_CARD.y, BG_CARD.z, BG_CARD.w);
        ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, BG_HOVER.w);
        ImGui.pushStyleColor(ImGuiCol.FrameBgActive, BG_ACTIVE.x, BG_ACTIVE.y, BG_ACTIVE.z, BG_ACTIVE.w);


        ImGui.pushStyleColor(ImGuiCol.Button, PRIMARY.x, PRIMARY.y, PRIMARY.z, PRIMARY.w);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, PRIMARY_LIGHT.x, PRIMARY_LIGHT.y, PRIMARY_LIGHT.z, PRIMARY_LIGHT.w);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, PRIMARY_DARK.x, PRIMARY_DARK.y, PRIMARY_DARK.z, PRIMARY_DARK.w);


        ImGui.pushStyleColor(ImGuiCol.Header, BG_CARD.x, BG_CARD.y, BG_CARD.z, BG_CARD.w);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, BG_HOVER.w);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, BG_ACTIVE.x, BG_ACTIVE.y, BG_ACTIVE.z, BG_ACTIVE.w);


        ImGui.pushStyleColor(ImGuiCol.Tab, BG_CARD.x, BG_CARD.y, BG_CARD.z, BG_CARD.w);
        ImGui.pushStyleColor(ImGuiCol.TabHovered, PRIMARY.x, PRIMARY.y, PRIMARY.z, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.TabActive, PRIMARY.x, PRIMARY.y, PRIMARY.z, PRIMARY.w);


        ImGui.pushStyleColor(ImGuiCol.Text, TEXT_PRIMARY.x, TEXT_PRIMARY.y, TEXT_PRIMARY.z, TEXT_PRIMARY.w);
        ImGui.pushStyleColor(ImGuiCol.TextDisabled, TEXT_MUTED.x, TEXT_MUTED.y, TEXT_MUTED.z, TEXT_MUTED.w);


        ImGui.pushStyleColor(ImGuiCol.Separator, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, 0.5f);


        ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, BG_PANEL.x, BG_PANEL.y, BG_PANEL.z, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, BG_HOVER.w);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, BG_ACTIVE.x, BG_ACTIVE.y, BG_ACTIVE.z, BG_ACTIVE.w);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, PRIMARY.x, PRIMARY.y, PRIMARY.z, PRIMARY.w);


        ImGui.pushStyleColor(ImGuiCol.TableHeaderBg, BG_CARD.x, BG_CARD.y, BG_CARD.z, BG_CARD.w);
        ImGui.pushStyleColor(ImGuiCol.TableBorderStrong, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.TableBorderLight, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, 0.3f);
        ImGui.pushStyleColor(ImGuiCol.TableRowBg, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.TableRowBgAlt, BG_CARD.x, BG_CARD.y, BG_CARD.z, 0.3f);


        ImGui.pushStyleColor(ImGuiCol.PopupBg, BG_PANEL.x, BG_PANEL.y, BG_PANEL.z, 0.95f);


        ImGui.pushStyleColor(ImGuiCol.Border, BG_HOVER.x, BG_HOVER.y, BG_HOVER.z, 0.4f);
    }

    public static void popThemeColors() {
        ImGui.popStyleColor(28);
    }



    public static int toColor(ImVec4 c) {
        return ImGui.colorConvertFloat4ToU32(c.x, c.y, c.z, c.w);
    }

    public static int toColor(ImVec4 c, float alpha) {
        return ImGui.colorConvertFloat4ToU32(c.x, c.y, c.z, alpha);
    }

    public static int toColor(float r, float g, float b, float a) {
        return ImGui.colorConvertFloat4ToU32(r, g, b, a);
    }

    public static void drawRoundedRect(ImDrawList dl, float x, float y, float w, float h, int color, float rounding) {
        dl.addRectFilled(x, y, x + w, y + h, color, rounding);
    }

    public static void drawRoundedRectOutline(ImDrawList dl, float x, float y, float w, float h, int color, float rounding, float thickness) {
        dl.addRect(x, y, x + w, y + h, color, rounding, 0, thickness);
    }

    public static void drawGradientRect(ImDrawList dl, float x, float y, float w, float h, int topColor, int bottomColor) {
        dl.addRectFilledMultiColor(x, y, x + w, y + h, topColor, topColor, bottomColor, bottomColor);
    }

    public static void drawTextCentered(ImDrawList dl, String text, float x, float y, float w, float h, int color) {
        ImVec2 textSize = new ImVec2();
        ImGui.calcTextSize(textSize, text);
        float tx = x + (w - textSize.x) / 2;
        float ty = y + (h - textSize.y) / 2;
        dl.addText(tx, ty, color, text);
    }

    public static void drawTextRight(ImDrawList dl, String text, float x, float y, float w, float h, int color) {
        ImVec2 textSize = new ImVec2();
        ImGui.calcTextSize(textSize, text);
        float tx = x + w - textSize.x;
        float ty = y + (h - textSize.y) / 2;
        dl.addText(tx, ty, color, text);
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static ImVec4 lerpColor(ImVec4 a, ImVec4 b, float t) {
        return new ImVec4(
            lerp(a.x, b.x, t),
            lerp(a.y, b.y, t),
            lerp(a.z, b.z, t),
            lerp(a.w, b.w, t)
        );
    }

    public static void drawShadow(ImDrawList dl, float x, float y, float w, float h, float rounding, float intensity) {
        int shadowColor = toColor(0, 0, 0, 0.3f * intensity);
        dl.addRectFilled(x + 3, y + 3, x + w + 3, y + h + 3, shadowColor, rounding);
        int shadowColor2 = toColor(0, 0, 0, 0.15f * intensity);
        dl.addRectFilled(x + 6, y + 6, x + w + 6, y + h + 6, shadowColor2, rounding);
    }

    public static void drawGradientRoundedRect(ImDrawList dl, float x, float y, float w, float h,
                                                 int topLeft, int topRight, int bottomLeft, int bottomRight, float rounding) {

        dl.addRectFilled(x, y, x + w, y + h, topLeft, rounding);

        dl.addRectFilledMultiColor(x, y, x + w, y + h, topLeft, topRight, bottomRight, bottomLeft);
    }
}
