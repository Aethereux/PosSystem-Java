package receipts;
import data.OrderState;
import imgui.ImGui;

public class ReceiptPopUp implements ReceiptInterface{
    @Override
    public void render(OrderState orderState, String paymentMethod, float discountAmount) {
        float padX = 16f;

        ImGui.setCursorPosX(padX);
        float centerX = (ImGui.getContentRegionAvailX() / 2);
        float textW = ImGui.calcTextSizeX("RECEIPT");
        ImGui.setCursorPosX(centerX - textW / 2);
        ImGui.text("RECEIPT");

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        String dateNow = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        renderRow("Order #", String.format("#%04d", orderState.orderNumber), padX);
        renderRow("Staff", orderState.staffName, padX);
        renderRow("Date", dateNow, padX);
        renderRow("Payment", paymentMethod, padX);

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        for (OrderState.OrderItem item : orderState.items) {
            ImGui.setCursorPosX(padX);
            ImGui.text(item.name + " x" + item.quantity);
            ImGui.sameLine(ImGui.getWindowWidth() - padX - 70);
            ImGui.text(String.format("%.2f", item.getLineTotal()));
        }

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        float subtotal = orderState.getTotal();
        float vat = orderState.getVatAmount();
        float total = Math.max(0, subtotal - discountAmount);

        renderRow("Subtotal", String.format("%.2f", subtotal), padX);
        renderRow("Discount", String.format("-%.2f", discountAmount), padX);
        renderRow("VAT", String.format("%.2f", vat), padX);

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.setWindowFontScale(1.2f);
        renderRow("TOTAL", String.format("%.2f", total), padX);
        ImGui.setWindowFontScale(1.0f);
    }

    private void renderRow(String label, String value, float padX) {
        ImGui.setCursorPosX(padX);
        ImGui.text(label);
        float valueW = ImGui.calcTextSizeX(value);
        ImGui.sameLine(ImGui.getWindowWidth() - padX - valueW);
        ImGui.text(value);
    }
}
