package parkinglot.payment;

public class CardPayment implements Payment {

    @Override
    public PaymentType paymentType() {
        return PaymentType.CARD;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " by card");
        return true;
    }
}
