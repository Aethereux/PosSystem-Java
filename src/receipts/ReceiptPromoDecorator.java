package receipts;

import data.OrderState;
import imgui.ImGui;

public class ReceiptPromoDecorator extends ReceiptDecorator {

    private final String promoCode;
    private final boolean isApplied;

    public ReceiptPromoDecorator(ReceiptInterface wrappedReceipt, String promoCode, boolean isApplied) {
        super(wrappedReceipt);
        this.promoCode = promoCode;
        this.isApplied = isApplied;
    }

    @Override
    public void render(OrderState orderState, String paymentMethod, float discountAmount) {
        super.render(orderState, paymentMethod, discountAmount);

        if (!isApplied || promoCode == null || promoCode.isEmpty()) {
            return;
        }

        float padX = 16f;

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.setCursorPosX(padX);
        ImGui.text("Promo Code");

        ImGui.sameLine(ImGui.getWindowWidth() - padX - ImGui.calcTextSizeX(promoCode));
        ImGui.text(promoCode);

        ImGui.setCursorPosX(padX);
        ImGui.text("Discount Applied");

        String discountText = String.format("-%.2f", discountAmount);
        ImGui.sameLine(ImGui.getWindowWidth() - padX - ImGui.calcTextSizeX(discountText));
        ImGui.text(discountText);
    }
}