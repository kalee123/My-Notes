

public class Main{

    public static void main(String[] args) {

        /* This class shows example of decorator pattern.
            Instead of changing actual vanillaIcecream class we wrapping gemVanilla with  vanilla
         */
        System.out.println("Decorator Pattern");
        Icecream vanilla = new VanillaIcecream();
        GemsTopings gemVanilla = new GemsTopings(vanilla);

        System.out.println(vanilla.description() +" "+ vanilla.cost());
        System.out.println(gemVanilla.description() +" "+ gemVanilla.cost());
    }
}