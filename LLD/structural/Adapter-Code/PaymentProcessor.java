/* What our app expects. All our code calls pay(amount). */
public interface PaymentProcessor {

    void pay(int amount);
}
