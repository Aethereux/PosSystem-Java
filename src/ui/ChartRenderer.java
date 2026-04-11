package ui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;

public class ChartRenderer {

    
    public static void drawPieChart(ImDrawList dl, float cx, float cy, float radius,
                                     float[] values, String[] labels, ImVec4[] colors,
                                     float animProgress) {
        float total = 0;
        for (float v : values) total += v;
        if (total <= 0) return;

        float animatedTotal = total * animProgress;
        float startAngle = (float) (-Math.PI / 2);
        float accumulated = 0;

        for (int i = 0; i < values.length; i++) {
            accumulated += values[i];
            if (accumulated > animatedTotal) {
                float sliceValue = values[i] - (accumulated - animatedTotal);
                float endAngle = startAngle + (sliceValue / total) * 2 * (float) Math.PI;
                drawSlice(dl, cx, cy, radius, startAngle, endAngle, Theme.toColor(colors[i % colors.length]));
                break;
            }
            float endAngle = startAngle + (values[i] / total) * 2 * (float) Math.PI;
            drawSlice(dl, cx, cy, radius, startAngle, endAngle, Theme.toColor(colors[i % colors.length]));
            startAngle = endAngle;
        }


        float innerRadius = radius * 0.55f;
        drawFilledCircle(dl, cx, cy, innerRadius, Theme.toColor(Theme.BG_PANEL));


        ImFont font = ImGui.getFont();
        float baseFontSize = font.getFontSize() * ImGui.getIO().getFontGlobalScale();
        int lblSize = Math.max(8, (int) (baseFontSize * 0.72f));
        float labelRadius = (innerRadius + radius) / 2f;

        startAngle = (float) (-Math.PI / 2);
        accumulated = 0;
        for (int i = 0; i < values.length; i++) {
            float sliceAngle = (values[i] / total) * 2 * (float) Math.PI;
            accumulated += values[i];


            if (accumulated <= animatedTotal) {
                float midAngle = startAngle + sliceAngle / 2f;
                float maxW = 2f * labelRadius * (float) Math.sin(sliceAngle / 2f) - 6f;

                if (maxW >= 16f && sliceAngle >= 0.25f) {
                    String text = labels[i];
                    ImVec2 sz = new ImVec2();
                    font.calcTextSizeA(sz, lblSize, Float.MAX_VALUE, -1, text);


                    while (sz.x > maxW && text.length() > 1) {
                        text = text.substring(0, text.length() - 1);
                        font.calcTextSizeA(sz, lblSize, Float.MAX_VALUE, -1, text + "...");
                    }
                    if (!text.equals(labels[i])) text += "...";
                    font.calcTextSizeA(sz, lblSize, Float.MAX_VALUE, -1, text);

                    float lx = cx + (float) Math.cos(midAngle) * labelRadius - sz.x / 2f;
                    float ly = cy + (float) Math.sin(midAngle) * labelRadius - sz.y / 2f;
                    dl.addText(font, lblSize, lx, ly, Theme.toColor(1f, 1f, 1f, animProgress), text);
                }
            }

            startAngle += sliceAngle;
        }


        String totalLabel = String.valueOf((int) total);
        ImVec2 textSize = new ImVec2();
        ImGui.calcTextSize(textSize, totalLabel);
        dl.addText(cx - textSize.x / 2, cy - textSize.y / 2 - 4, Theme.toColor(Theme.TEXT_PRIMARY), totalLabel);
        ImGui.calcTextSize(textSize, "Total");
        dl.addText(cx - textSize.x / 2, cy + 6, Theme.toColor(Theme.TEXT_MUTED), "Total");
    }

    
    public static void drawPieLegend(ImDrawList dl, float x, float y, float[] values, String[] labels,
                                      ImVec4[] colors, float animProgress) {
        float total = 0;
        for (float v : values) total += v;
        if (total <= 0) return;

        float lineHeight = 22;
        for (int i = 0; i < labels.length; i++) {
            float itemY = y + i * lineHeight;
            float alpha = AnimationHelper.staggered(animProgress, i, labels.length, 0.6f);
            alpha = AnimationHelper.easeOutCubic(alpha);
            if (alpha <= 0) continue;

            int color = Theme.toColor(colors[i % colors.length], alpha);
            dl.addRectFilled(x, itemY + 3, x + 12, itemY + 15, color, 3);

            float pct = (values[i] / total) * 100;
            String label = String.format("%s (%.1f%%)", labels[i], pct);
            dl.addText(x + 18, itemY, Theme.toColor(Theme.TEXT_SECONDARY, alpha), label);
        }
    }

    
    public static void drawHorizontalBarChart(ImDrawList dl, float x, float y, float w, float h,
                                               String[] labels, float[][] data, String[] datasetLabels,
                                               ImVec4[] datasetColors, float animProgress) {
        if (labels.length == 0) return;

        ImFont font = ImGui.getFont();
        float baseFontSize = font.getFontSize() * ImGui.getIO().getFontGlobalScale();


        float labelWidth = Math.min(w * 0.25f, 140);
        float valueWidth = 70;
        float barAreaWidth = w - labelWidth - valueWidth;
        float barGroupHeight = h / labels.length;
        float barHeight = (barGroupHeight - 8) / data.length;


        float maxVal = 0;
        for (float[] dataset : data) {
            for (float v : dataset) maxVal = Math.max(maxVal, v);
        }
        if (maxVal <= 0) return;

        for (int row = 0; row < labels.length; row++) {
            float rowY = y + row * barGroupHeight;
            float itemAnim = AnimationHelper.staggered(animProgress, row, labels.length, 0.5f);
            itemAnim = AnimationHelper.easeOutCubic(itemAnim);
            if (itemAnim <= 0) continue;


            int lblFontSize = (int) baseFontSize;
            ImVec2 lblSize = new ImVec2();
            font.calcTextSizeA(lblSize, lblFontSize, Float.MAX_VALUE, -1, labels[row]);
            while (lblSize.x > labelWidth - 4 && lblFontSize > 8) {
                lblFontSize--;
                font.calcTextSizeA(lblSize, lblFontSize, Float.MAX_VALUE, -1, labels[row]);
            }
            dl.addText(font, lblFontSize, x, rowY + (barGroupHeight - lblSize.y) / 2,
                       Theme.toColor(Theme.TEXT_SECONDARY, itemAnim), labels[row]);


            for (int d = 0; d < data.length; d++) {
                float barY = rowY + d * (barHeight + 2) + 2;
                float barW = (data[d][row] / maxVal) * barAreaWidth * itemAnim;
                int barColor = Theme.toColor(datasetColors[d % datasetColors.length], itemAnim);

                dl.addRectFilled(x + labelWidth, barY, x + labelWidth + barW, barY + barHeight, barColor, 4);


                if (itemAnim > 0.5f) {
                    String val = String.format("%.0f", data[d][row]);
                    dl.addText(x + labelWidth + barW + 5, barY,
                               Theme.toColor(Theme.TEXT_MUTED, itemAnim), val);
                }
            }
        }


        float legendX = x + w - 200;
        float legendY = y - 25;
        for (int d = 0; d < datasetLabels.length; d++) {
            float lx = legendX + d * 100;
            dl.addRectFilled(lx, legendY + 2, lx + 12, legendY + 14,
                             Theme.toColor(datasetColors[d % datasetColors.length]), 3);
            dl.addText(lx + 18, legendY, Theme.toColor(Theme.TEXT_SECONDARY), datasetLabels[d]);
        }
    }

    private static void drawSlice(ImDrawList dl, float cx, float cy, float radius,
                                    float startAngle, float endAngle, int color) {
        int segments = Math.max(3, (int) (Math.abs(endAngle - startAngle) / (Math.PI * 2) * 64));
        float angleStep = (endAngle - startAngle) / segments;

        for (int i = 0; i < segments; i++) {
            float a1 = startAngle + i * angleStep;
            float a2 = startAngle + (i + 1) * angleStep;
            float x1 = cx + (float) Math.cos(a1) * radius;
            float y1 = cy + (float) Math.sin(a1) * radius;
            float x2 = cx + (float) Math.cos(a2) * radius;
            float y2 = cy + (float) Math.sin(a2) * radius;
            dl.addTriangleFilled(cx, cy, x1, y1, x2, y2, color);
        }
    }

    private static void drawFilledCircle(ImDrawList dl, float cx, float cy, float radius, int color) {
        dl.addCircleFilled(cx, cy, radius, color, 48);
    }
}
