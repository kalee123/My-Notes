public class FacadeMain {

    public static void main(String[] args) {

        /* This class shows example of facade pattern.
           The client calls only OrderFacade. It does not know about
           Inventory, Payment or Shipping, or the order they run in.
         */
        /*
            With out facade the min class need to call each objects seperately
            Spring service class is best example where each cntoller calls the respective service
         */
        System.out.println("Facade Pattern");
        OrderFacade orderFacade = new OrderFacade();
        orderFacade.placeOrder("Phone", 20000);
    }
}
