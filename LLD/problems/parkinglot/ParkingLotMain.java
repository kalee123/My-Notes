package parkinglot;

import parkinglot.payment.UpiPayment;
import parkinglot.vechicle.Car;

public class ParkingLotMain {

    public static void main(String[] args) {

        // facade pattern: main talks to one class only
        ParkingLotFacade parkingLot = new ParkingLotFacade();
        Car car = new Car("TN01AB1234");

        ParkingSpot spot = parkingLot.enter(car);
        double fee = parkingLot.exit(spot, new UpiPayment()); // Strategy pattern

        System.out.println("Fee: " + fee);
    }
}
