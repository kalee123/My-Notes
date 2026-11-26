package parkinglot.vechicle;

public class Van implements Vehicle {

    private final String registrationNo;

    public Van(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    @Override
    public VehicleType vehicleType() {
        return VehicleType.VAN;
    }

    @Override
    public String registrationNo() {
        return registrationNo;
    }
}
