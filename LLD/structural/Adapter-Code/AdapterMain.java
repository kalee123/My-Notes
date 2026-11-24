public class AdapterMain {

    public static void main(String[] args) {

        /* This class shows example of adapter pattern.
           The app only knows PaymentProcessor.pay(amount).
           Each adapter translates that call into what its library understands.
         */
        System.out.println("Adapter Pattern");

        PaymentProcessor razor = new RazorAdapter(new RazorGateway());
        PaymentProcessor payFast = new PayFastAdapter(new PayFastGateway());

        checkout(razor, 500);
        checkout(payFast, 500);
    }

    /* App code: written once, works with any library that has an adapter. */
    static void checkout(PaymentProcessor processor, int amount) {
        processor.pay(amount);
    }
}
