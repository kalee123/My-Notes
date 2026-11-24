/* Third-party library. It works, but its method does not match
   PaymentProcessor, and we are not allowed to change it. */
public class RazorGateway {

    public void makePayment(double rupees, String currency) {
        System.out.println("Razor paid " + rupees + " " + currency);
    }
}
