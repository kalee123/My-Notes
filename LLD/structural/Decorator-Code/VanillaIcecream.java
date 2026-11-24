public class VanillaIcecream implements Icecream{

    @Override
    public String description() {
        return "Plain vanilla icecream cone";
    }

    @Override
    public int cost(){
        return 10;
    }
}
