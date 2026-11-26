package parkinglot;

import parkinglot.payment.Payment;
import parkinglot.payment.PaymentFactory;
import parkinglot.payment.PaymentType;
import parkinglot.vechicle.Vehicle;
import parkinglot.vechicle.VehicleFactory;
import parkinglot.vechicle.VehicleType;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ParkingLotMain {

    public static void main(String[] args) {

        System.out.println("Welcome To ParkingLot:");
        Scanner sc = new Scanner(System.in);

        // facade pattern: main talks to one class only
        ParkingLotFacade parkingLot = new ParkingLotFacade();

        // registration number -> spot, so exit can find what entry assigned
        Map<String, ParkingSpot> parked = new HashMap<>();

        while (true) {
            System.out.println();
            System.out.println("1. Entry  2. Exit  3. Quit");
            int option = sc.nextInt();

            if (option == 1) {
                System.out.println("1. Car  2. Bike  3. Van  4. Truck");
                int typeNo = sc.nextInt();
                if (typeNo < 1 || typeNo > VehicleType.values().length) {
                    System.out.println("Invalid vehicle type");
                    continue;
                }
                VehicleType type = VehicleType.values()[typeNo - 1];

                System.out.println("Registration number:");
                String regNo = sc.next();
                if (parked.containsKey(regNo)) {
                    System.out.println(regNo + " is already parked");
                    continue;
                }

                Vehicle vehicle = VehicleFactory.create(type, regNo);   // factory pattern
                try {
                    ParkingSpot spot = parkingLot.enter(vehicle);
                    parked.put(regNo, spot);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());                 // parking lot is full
                }

            } else if (option == 2) {
                System.out.println("Registration number:");
                String regNo = sc.next();
                ParkingSpot spot = parked.get(regNo);
                if (spot == null) {
                    System.out.println("No vehicle parked with number " + regNo);
                    continue;
                }

                System.out.println("1. Cash  2. Card  3. UPI");
                int payNo = sc.nextInt();
                if (payNo < 1 || payNo > PaymentType.values().length) {
                    System.out.println("Invalid payment type");
                    continue;
                }
                Payment payment = PaymentFactory.create(PaymentType.values()[payNo - 1]);   // factory pattern

                double fee = parkingLot.exit(spot, payment);            // strategy pattern
                parked.remove(regNo);
                System.out.println("Fee: " + fee);

            } else if (option == 3) {
                System.out.println("Bye");
                break;

            } else {
                System.out.println("Invalid option");
            }
        }
    }
}
