package parkinglot.vechicle;

public class Car implements Vehicle {

    private final String registrationNo;

    public Car(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    @Override
    public VehicleType vehicleType() {
        return VehicleType.CAR;
    }

    @Override
    public String registrationNo() {
        return registrationNo;
    }
}
