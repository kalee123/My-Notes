package parkinglot.vechicle;

public class Truck implements Vehicle {

    private final String registrationNo;

    public Truck(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    @Override
    public VehicleType vehicleType() {
        return VehicleType.TRUCK;
    }

    @Override
    public String registrationNo() {
        return registrationNo;
    }
}
