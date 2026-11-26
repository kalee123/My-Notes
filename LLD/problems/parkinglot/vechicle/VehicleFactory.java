package parkinglot.vechicle;

public class VehicleFactory {

    public static Vehicle create(VehicleType type, String registrationNo) {
        switch (type) {
            case CAR:   return new Car(registrationNo);
            case BIKE:  return new Bike(registrationNo);
            case VAN:   return new Van(registrationNo);
            case TRUCK: return new Truck(registrationNo);
            default:    throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
