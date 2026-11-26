package parkinglot;

import parkinglot.vechicle.Vehicle;

import java.time.LocalDateTime;

public class ParkingSpot {

    private final int parkingFloor;
    private final String parkingSpace;   // spot label on the floor, e.g. "A1"
    private Vehicle vehicle;             // null means the spot is free
    private LocalDateTime inTime;        // set when a vehicle parks
    private LocalDateTime exitTime;      // set when the vehicle leaves

    public ParkingSpot(int parkingFloor, String parkingSpace) {
        this.parkingFloor = parkingFloor;
        this.parkingSpace = parkingSpace;
    }

    public boolean isAvailable() {
        return vehicle == null;
    }

    public void park(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot " + parkingSpace + " is already occupied");
        }
        this.vehicle = vehicle;
        this.inTime = LocalDateTime.now();
        this.exitTime = null;
    }

    public void leave() {
        this.vehicle = null;
        this.exitTime = LocalDateTime.now();
    }

    public int getParkingFloor() {
        return parkingFloor;
    }

    public String getParkingSpace() {
        return parkingSpace;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }
}
