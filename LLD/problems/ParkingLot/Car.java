public class Car implements Vehicle {

    private final int registrationNo;

    Car(int no) {
        this.registrationNo = no;
    }

    @Override
    public String vehicleType() {
        return "car";
    }

    @Override
    public int registrationNo() {
        return registrationNo;
    }
}
