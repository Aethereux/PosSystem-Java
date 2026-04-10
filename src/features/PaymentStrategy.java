package features;

import data.OrderState;

public interface PaymentStrategy {
    String getMethodName();

    void pay (OrderState amount);
}
