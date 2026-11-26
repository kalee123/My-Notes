package parkinglot.vechicle;

public class Bike implements Vehicle {

    private final String registrationNo;

    public Bike(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    @Override
    public VehicleType vehicleType() {
        return VehicleType.BIKE;
    }

    @Override
    public String registrationNo() {
        return registrationNo;
    }
}
