public class GemsTopings implements Icecream{

    private final Icecream icecream;

    public GemsTopings(Icecream icecream) {
        this.icecream = icecream;
    }

    @Override
    public String description() {
        return icecream.description() + " + gems";
    }

    @Override
    public int cost() {
        return icecream.cost() + 5;
    }
}
