package parkinglot;

import parkinglot.parking.ParkingOperations;
import parkinglot.payment.FeeCalculator;
import parkinglot.payment.Payment;
import parkinglot.vechicle.Vehicle;

/* Facade: the one class the client talks to.
   It knows which classes are needed and in what order. */
public class ParkingLotFacade {

    private final ParkingOperations parking = ParkingOperations.getInstance();
    private final FeeCalculator feeCalculator = new FeeCalculator();

    public ParkingSpot enter(Vehicle vehicle) {
        ParkingSpot spot = parking.entry(vehicle);
        System.out.println(vehicle.vehicleType() + " " + vehicle.registrationNo()
                + " parked at " + spot.getParkingSpace());
        return spot;
    }

    public double exit(ParkingSpot spot, Payment payment) {
        parking.exit(spot);                                                          // 1. free the spot
        double fee = feeCalculator.calculate(spot.getInTime(), spot.getExitTime());  // 2. work out the fee
        if (!payment.pay(fee)) {                                                     // 3. take the payment
            throw new IllegalStateException("Payment failed");
        }
        System.out.println("Spot " + spot.getParkingSpace() + " is free again");
        return fee;
    }
}
