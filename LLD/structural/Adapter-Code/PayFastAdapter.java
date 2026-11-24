public class PayFastAdapter implements PaymentProcessor {

    private final PayFastGateway gateway;

    public PayFastAdapter(PayFastGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void pay(int amount) {
        gateway.send("Rs." + amount);            // translate the call
    }
}
