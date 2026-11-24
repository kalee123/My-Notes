public class RazorAdapter implements PaymentProcessor {

    private final RazorGateway gateway;          // the class that does not fit

    public RazorAdapter(RazorGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void pay(int amount) {
        gateway.makePayment(amount, "INR");      // translate the call
    }
}
