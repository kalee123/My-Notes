package parkinglot.payment;

public class CashPayment implements Payment {

    @Override
    public PaymentType paymentType() {
        return PaymentType.CASH;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " in cash");
        return true;
    }
}
