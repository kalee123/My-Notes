package parkinglot.payment;

public interface Payment {

    PaymentType paymentType();
    boolean pay(double amount);


}
