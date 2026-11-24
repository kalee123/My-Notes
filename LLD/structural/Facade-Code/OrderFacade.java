public class OrderFacade {

    private final Inventory inventory = new Inventory();
    private final Payment payment = new Payment();
    private final Shipping shipping = new Shipping();

    /* The one simple method the client calls.
       It knows which classes are needed and in what order. */
    public void placeOrder(String item, int amount) {
        if (!inventory.inStock(item)) {
            System.out.println(item + " is out of stock");
            return;
        }
        payment.pay(amount);
        shipping.ship(item);
        System.out.println("Order placed for " + item);
    }
}
