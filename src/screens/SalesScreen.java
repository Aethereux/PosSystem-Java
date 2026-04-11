package screens;

import data.SalesManager;
import data.SalesRecord;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.flag.ImGuiWindowFlags;
import menu.MenuManager;
import ui.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalesScreen {

    private double enterTime = 0;

    public void onEnter() {
        enterTime = ImGui.getTime();
    }

    public void render() {
        ImDrawList dl = ImGui.getWindowDrawList();

        Sidebar.render(MenuManager.SCREEN_SALES);

        ImGui.sameLine();
        float contentW = ImGui.getContentRegionAvailX();
        float contentH = ImGui.getContentRegionAvailY();

        int noScroll = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.beginChild("##sales_content", contentW, contentH, false, noScroll);

        float pad = Math.max(10, contentW * 0.015f);


        ImGui.setCursorPos(pad, pad);
        ImGui.setWindowFontScale(1.4f);
        ImGui.textColored(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, 1, "Sales Report");
        ImGui.setWindowFontScale(1.0f);
        ImGui.spacing();

        List<SalesRecord> records = SalesManager.getInstance().getRecords();


        float cardAnim = AnimationHelper.easeOutCubic(AnimationHelper.computeProgress(enterTime + 0.1, 0.7f));
        renderSummaryCards(dl, pad, contentW, records, cardAnim);
        ImGui.spacing();


        float availH = ImGui.getContentRegionAvailY();
        float tableH = records.isEmpty() ? availH - pad : availH * 0.46f;

        ImGui.setCursorPosX(pad);
        float tableW = ImGui.getContentRegionAvailX() - pad;

        int tableFlags = ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg | ImGuiTableFlags.ScrollY
                | ImGuiTableFlags.Resizable | ImGuiTableFlags.SizingStretchProp;

        if (ImGui.beginTable("##sales_table", 6, tableFlags, tableW, tableH)) {
            ImGui.tableSetupColumn("Order #",   ImGuiTableColumnFlags.None, 0.6f);
            ImGui.tableSetupColumn("Date/Time", ImGuiTableColumnFlags.None, 1.5f);
            ImGui.tableSetupColumn("Staff",     ImGuiTableColumnFlags.None, 1.0f);
            ImGui.tableSetupColumn("Payment",   ImGuiTableColumnFlags.None, 0.8f);
            ImGui.tableSetupColumn("Discount",  ImGuiTableColumnFlags.None, 0.8f);
            ImGui.tableSetupColumn("Total",     ImGuiTableColumnFlags.None, 0.8f);
            ImGui.tableHeadersRow();

            if (records.isEmpty()) {
                ImGui.tableNextRow();
                ImGui.tableNextColumn();
                ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1,
                        "No transactions yet.");
            }

            float tableAnim = AnimationHelper.computeProgress(enterTime, 0.6f);

            for (int i = 0; i < records.size(); i++) {
                float rowAnim = AnimationHelper.staggered(tableAnim, i, records.size(), 0.5f);
                if (rowAnim <= 0) continue;

                SalesRecord rec = records.get(i);
                ImGui.tableNextRow();

                ImGui.tableNextColumn();
                ImGui.text(String.format("#%04d", rec.orderNumber));

                ImGui.tableNextColumn();
                ImGui.text(rec.dateTime);

                ImGui.tableNextColumn();
                ImGui.text(rec.staffName);

                ImGui.tableNextColumn();
                ImGui.text(rec.paymentMethod);

                ImGui.tableNextColumn();
                if (rec.discountAmount > 0) {
                    ImGui.textColored(Theme.SUCCESS.x, Theme.SUCCESS.y, Theme.SUCCESS.z, 1,
                            String.format("-%.2f", rec.discountAmount));
                } else {
                    ImGui.textColored(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, 1, "—");
                }

                ImGui.tableNextColumn();
                ImGui.textColored(Theme.PRIMARY.x, Theme.PRIMARY.y, Theme.PRIMARY.z, 1,
                        String.format("%.2f", rec.total));
            }

            ImGui.endTable();
        }


        if (records.isEmpty()) {
            ImGui.endChild();
            return;
        }

        float chartAvailH = ImGui.getContentRegionAvailY();
        if (chartAvailH < 60) {
            ImGui.endChild();
            return;
        }

        float chartAnim = AnimationHelper.easeOutCubic(
                AnimationHelper.computeProgress(enterTime + 0.3, 1.0f));

        float chartW = ImGui.getContentRegionAvailX() - pad;
        float pieChartW = chartW * 0.38f;
        float barChartW = chartW - pieChartW - pad;

        int productCount = SalesManager.getInstance().getRecords().stream()
                .flatMap(r -> r.items.stream())
                .map(i -> i.name)
                .collect(java.util.stream.Collectors.toSet()).size();
        float cardH = Math.max(280f, 240f + productCount * 18f);

        ImGui.setCursorPosX(pad);
        ImGui.setNextWindowContentSize(chartW, cardH);
        ImGui.beginChild("##charts_scroll", chartW, Math.min(chartAvailH, cardH), false, 0);



        ImVec2 winPos = new ImVec2();
        ImGui.getWindowPos(winPos);
        float scrollY = ImGui.getScrollY();
        ImVec2 chartStart = new ImVec2(winPos.x, winPos.y - scrollY);

        dl = ImGui.getWindowDrawList();


        Map<String, float[]> productStats = new LinkedHashMap<>();
        for (SalesRecord r : records) {
            for (data.OrderState.OrderItem item : r.items) {
                float[] stats = productStats.computeIfAbsent(item.name, k -> new float[2]);
                stats[0] += item.quantity;
                stats[1] += item.getLineTotal();
            }
        }


        List<Map.Entry<String, float[]>> sorted = new java.util.ArrayList<>(productStats.entrySet());
        sorted.sort((a, b) -> Float.compare(b.getValue()[0], a.getValue()[0]));

        int pieN = sorted.size();
        int barN = Math.min(sorted.size(), 5);

        String[] pieNames    = new String[pieN];
        float[]  unitsSold   = new float[pieN];
        for (int i = 0; i < pieN; i++) {
            pieNames[i]  = shortName(sorted.get(i).getKey());
            unitsSold[i] = sorted.get(i).getValue()[0];
        }

        List<Map.Entry<String, float[]>> byRevenue = new java.util.ArrayList<>(productStats.entrySet());
        byRevenue.sort((a, b) -> Float.compare(b.getValue()[1], a.getValue()[1]));
        String[] barNames  = new String[barN];
        float[]  revenues  = new float[barN];
        for (int i = 0; i < barN; i++) {
            barNames[i] = shortName(byRevenue.get(i).getKey());
            revenues[i] = byRevenue.get(i).getValue()[1];
        }


        Theme.drawShadow(dl, chartStart.x, chartStart.y, pieChartW, cardH, 12, 0.4f);
        Theme.drawRoundedRect(dl, chartStart.x, chartStart.y, pieChartW, cardH,
                Theme.toColor(Theme.BG_PANEL), 12);
        ImGui.setWindowFontScale(1.1f);
        dl.addText(chartStart.x + 16, chartStart.y + 12, Theme.toColor(Theme.TEXT_PRIMARY), "Units Sold by Product");
        ImGui.setWindowFontScale(1.0f);

        float pieRadius = Math.min(pieChartW * 0.38f, (cardH - 40) * 0.46f);
        float pieCx = chartStart.x + pieChartW * 0.45f;
        float pieCy = chartStart.y + 40 + pieRadius + 8;
        ChartRenderer.drawPieChart(dl, pieCx, pieCy, pieRadius, unitsSold, pieNames,
                Theme.CHART_COLORS, chartAnim);


        float barX = chartStart.x + pieChartW + pad;
        Theme.drawShadow(dl, barX, chartStart.y, barChartW, cardH, 12, 0.4f);
        Theme.drawRoundedRect(dl, barX, chartStart.y, barChartW, cardH,
                Theme.toColor(Theme.BG_PANEL), 12);
        ImGui.setWindowFontScale(1.1f);
        dl.addText(barX + 16, chartStart.y + 12, Theme.toColor(Theme.TEXT_PRIMARY), "Revenue by Product");
        ImGui.setWindowFontScale(1.0f);

        float[][] barData = new float[1][barN];
        for (int i = 0; i < barN; i++) barData[0][i] = revenues[i];
        float barChartH = barN * 38f;
        ChartRenderer.drawHorizontalBarChart(dl, barX + 16, chartStart.y + 50,
                barChartW - 32, barChartH,
                barNames, barData, new String[]{"Revenue"}, new ImVec4[]{Theme.PRIMARY}, chartAnim);

        ImGui.endChild();

        ImGui.endChild();
    }

    
    private String shortName(String name) {
        int paren = name.indexOf('(');
        return paren > 0 ? name.substring(0, paren).trim() : name;
    }

    private void renderSummaryCards(ImDrawList dl, float pad, float contentW,
                                    List<SalesRecord> records, float anim) {
        float totalRevenue = SalesManager.getInstance().getTotalRevenue();
        float avgOrder = SalesManager.getInstance().getAverageOrderValue();

        ImVec2 cursor = new ImVec2();
        ImGui.getCursorScreenPos(cursor);

        float cardW = (contentW - pad * 4) / 3;
        float cardH = 58;

        String[] labels = { "Total Revenue", "Transactions", "Avg Order Value" };
        String[] values = {
            String.format("P %,.2f", totalRevenue),
            String.valueOf(records.size()),
            String.format("P %,.2f", avgOrder)
        };
        ImVec4[] colors = { Theme.SUCCESS, Theme.PRIMARY, Theme.ACCENT };

        for (int i = 0; i < 3; i++) {
            float cx = cursor.x + pad + i * (cardW + pad);
            float cy = cursor.y;

            Theme.drawShadow(dl, cx, cy, cardW, cardH, 10, anim * 0.4f);
            Theme.drawRoundedRect(dl, cx, cy, cardW, cardH, Theme.toColor(Theme.BG_PANEL), 10);
            Theme.drawRoundedRect(dl, cx, cy, 4, cardH, Theme.toColor(colors[i], anim), 2);

            dl.addText(cx + 12, cy + 8,
                    Theme.toColor(Theme.TEXT_MUTED.x, Theme.TEXT_MUTED.y, Theme.TEXT_MUTED.z, anim),
                    labels[i]);
            ImGui.setWindowFontScale(1.2f);
            dl.addText(cx + 12, cy + 28,
                    Theme.toColor(Theme.TEXT_PRIMARY.x, Theme.TEXT_PRIMARY.y, Theme.TEXT_PRIMARY.z, anim),
                    values[i]);
            ImGui.setWindowFontScale(1.0f);
        }

        ImGui.setCursorPosX(pad);
        ImGui.dummy(contentW - pad * 2, cardH + 4);
    }
}
