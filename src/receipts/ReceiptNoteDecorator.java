package receipts;

import data.OrderState;
import imgui.ImGui;

public class ReceiptNoteDecorator extends ReceiptDecorator {
    private final String note;

    public ReceiptNoteDecorator(ReceiptInterface wrappedReceipt, String note) {
        super(wrappedReceipt);
        this.note = note;
    }

    @Override
    public void render(OrderState orderState, String paymentMethod, float discountAmount) {
        super.render(orderState, paymentMethod, discountAmount);

        float padX = 16f;
        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.setCursorPosX(padX);
        ImGui.text(note);
    }
}
