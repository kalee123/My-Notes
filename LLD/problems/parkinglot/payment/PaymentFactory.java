package parkinglot.payment;

public class PaymentFactory {

    public static Payment create(PaymentType type) {
        switch (type) {
            case CASH: return new CashPayment();
            case CARD: return new CardPayment();
            case UPI:  return new UpiPayment();
            default:   throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
