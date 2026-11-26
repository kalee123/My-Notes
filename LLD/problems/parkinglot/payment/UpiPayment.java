package parkinglot.payment;

public class UpiPayment implements Payment {

    @Override
    public PaymentType paymentType() {
        return PaymentType.UPI;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " by UPI");
        return true;
    }
}
