package receipts;

import data.OrderState;
import imgui.ImGui;

public class ReceiptEmployeeDiscountDecorator extends ReceiptDecorator {

    private final boolean isApplied;
    private final float discountAmount;

    public ReceiptEmployeeDiscountDecorator(ReceiptInterface wrappedReceipt,
                                            boolean isApplied,
                                            float discountAmount) {
        super(wrappedReceipt);
        this.isApplied = isApplied;
        this.discountAmount = discountAmount;
    }

    @Override
    public void render(OrderState orderState, String paymentMethod, float discountAmountParam) {
        super.render(orderState, paymentMethod, discountAmountParam);

        if (!isApplied || discountAmount <= 0) return;

        float padX = 16f;

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.setCursorPosX(padX);
        ImGui.text("Employee Discount");

        String value = String.format("-%.2f", discountAmount);
        ImGui.sameLine(ImGui.getWindowWidth() - padX - ImGui.calcTextSizeX(value));
        ImGui.text(value);
    }
}