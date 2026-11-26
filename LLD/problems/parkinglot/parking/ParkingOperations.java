package parkinglot.parking;

import parkinglot.ParkingSpot;
import parkinglot.vechicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class ParkingOperations {

    private static final int TOTAL_SPOTS = 100;

    // 2. the one and only instance, created when the class loads
    private static final ParkingOperations INSTANCE = new ParkingOperations(TOTAL_SPOTS);

    private final List<ParkingSpot> spots = new ArrayList<>();

    // 1. private constructor: no other class can call new ParkingOperations(...)
    private ParkingOperations(int totalSpots) {
        for (int i = 1; i <= totalSpots; i++) {
            spots.add(new ParkingSpot(1, "A" + i));
        }
    }

    // 3. the only way to get the object
    public static ParkingOperations getInstance() {
        return INSTANCE;
    }

    public ParkingSpot entry(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable()) {
                spot.park(vehicle);
                return spot;
            }
        }
        throw new IllegalStateException("Parking lot is full");
    }

    public void exit(ParkingSpot spot) {
        spot.leave();
    }
}
