public class ParkingLotMain {

    //A car enters, gets a ticket, parks in a free spot, exits, and pays.
    public static void main(String[] args) {
//        System.out.println("hello");
        Car car=new Car(10);
        System.out.println(car.vehicleType()+" = "+ car.registrationNo());
    }
}

