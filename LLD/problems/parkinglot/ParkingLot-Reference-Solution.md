# Parking Lot: Reference Solution

A reference design for this problem. These are class skeletons: they show the classes and how they relate, and most method bodies are left empty.

- Problem statement and scenarios: [ParkingLot.md](ParkingLot.md)
- My own solution: [ParkingLot-Solution.md](ParkingLot-Solution.md)

## Classes at a glance

| Group | Classes | Job |
|-------|---------|-----|
| Status enums | `PaymentStatus`, `AccountStatus`, `TicketStatus` | Track the state of a payment, an account, and a ticket. |
| Data types | `Person`, `Address` | Personal and address details. |
| Parking spots | `ParkingSpot` (abstract), `Accessible`, `Compact`, `Large`, `MotorcycleSpot` | One subclass per spot type. Each decides which vehicles it accepts. |
| Vehicles | `Vehicle` (abstract), `Car`, `Van`, `Truck`, `Motorcycle` | One subclass per vehicle type. A vehicle holds its ticket. |
| Accounts | `Account` (abstract), `Admin` | An admin adds spots, display boards, entrances, and exits. |
| Display and pricing | `DisplayBoard`, `ParkingRate` | Show free spots by type; calculate the fee. |
| Gates | `Entrance`, `Exit` | The entrance issues a ticket; the exit validates it. |
| Ticket | `ParkingTicket` | Records one visit: times, amount, status, vehicle, payment, and the gates used. |
| Payment | `Payment` (abstract), `Cash`, `CreditCard` | One subclass per payment method, with shared amount, status, and timestamp. |
| Parking lot | `ParkingLot` | The singleton that owns the entrances, exits, spots, tickets, and display boards. |
| Demo | `Driver` | Runs three scenarios against the system. |

## Enumerations and custom data types

```java
// Enumerations for status tracking in the system
enum PaymentStatus { COMPLETED, FAILED, PENDING, UNPAID, REFUNDED }
enum AccountStatus { ACTIVE, CLOSED, CANCELED, BLOCKLISTED, NONE }
enum TicketStatus { ISSUED, IN_USE, PAID, VALIDATED, CANCELED, REFUNDED }

// Custom data type for personal and address information
class Person {
    private String name;
    private String address;
    private String phone;
    private String email;
}

class Address {
    private int zipCode;
    private String street;
    private String city;
    private String state;
    private String country;
}
```

## Parking spots

```java
// Abstract base class for parking spots
abstract class ParkingSpot {
    private int id;
    private boolean isFree;
    private Vehicle vehicle; // Association: Each spot can be assigned to one vehicle

    public abstract boolean assignVehicle(Vehicle vehicle);

    public boolean removeVehicle() {
        // Logic to remove vehicle from spot and mark as free
        return true;
    }
}

// Four types of parking spots as subclasses
class Accessible extends ParkingSpot {
    public boolean assignVehicle(Vehicle vehicle) { /* ... */ return true; }
}

class Compact extends ParkingSpot {
    public boolean assignVehicle(Vehicle vehicle) { /* ... */ return true; }
}

class Large extends ParkingSpot {
    public boolean assignVehicle(Vehicle vehicle) { /* ... */ return true; }
}

class MotorcycleSpot extends ParkingSpot {
    public boolean assignVehicle(Vehicle vehicle) { /* ... */ return true; }
}
```

`assignVehicle` is abstract so that each spot type applies its own rule for which vehicles fit. `removeVehicle` is the same for every spot, so it lives in the base class. This is the case where an abstract class fits better than an interface: the subclasses share fields (`id`, `isFree`, `vehicle`) and code.

## Vehicle

```java
abstract class Vehicle {
    private String licenseNo;
    private ParkingTicket ticket; // Association: Each vehicle has a ticket

    public abstract void assignTicket(ParkingTicket ticket);
}

class Car extends Vehicle {
    public void assignTicket(ParkingTicket ticket) { /* ... */ }
}
class Van extends Vehicle {
    public void assignTicket(ParkingTicket ticket) { /* ... */ }
}
class Truck extends Vehicle {
    public void assignTicket(ParkingTicket ticket) { /* ... */ }
}
class Motorcycle extends Vehicle {
    public void assignTicket(ParkingTicket ticket) { /* ... */ }
}
```

## Account

```java
abstract class Account {
    private String userName;
    private String password;
    private Person person;
    private AccountStatus status;

    public abstract boolean resetPassword();
}

class Admin extends Account {
    public boolean addParkingSpot(ParkingSpot spot) { /* ... */ return true; }
    public boolean addDisplayBoard(DisplayBoard board) { /* ... */ return true; }
    public boolean addEntrance(Entrance entrance) { /* ... */ return true; }
    public boolean addExit(Exit exit) { /* ... */ return true; }

    public boolean resetPassword() { /* ... */ return true; }
}
```

## Display board and parking rate

```java
class DisplayBoard {
    private int id;
    private Map<String, List<ParkingSpot>> parkingSpots;

    public DisplayBoard(int id) { /* ... */ }
    public void addParkingSpot(String spotType, List<ParkingSpot> spots) { /* ... */ }
    public void showFreeSlot() { /* ... */ }
}

class ParkingRate {
    private double hours;
    private double rate;

    public double calculate(double duration, Vehicle vehicle, ParkingSpot spot) {
        // Pricing logic can use duration, vehicle type, spot type, etc.
        return 0.0;
    }
}
```

## Entrance and exit

```java
class Entrance {
    private int id;
    public ParkingTicket getTicket(Vehicle vehicle) { /* ... */ return null; }
}

class Exit {
    private int id;
    public void validateTicket(ParkingTicket ticket) { /* ... */ }
}
```

## Parking ticket

```java
class ParkingTicket {
    private int ticketNo;
    private Date entryTime;
    private Date exitTime;
    private double amount;
    private TicketStatus status;

    private Vehicle vehicle;
    private Payment payment; // Composition: Ticket owns Payment
    private Entrance entrance;
    private Exit exitIns;
}
```

The ticket is the record of one visit, and everything about that visit hangs off it: when the vehicle came and left, how much was charged, which vehicle, which payment, and which entrance and exit were used. Because each visit has its own ticket, one vehicle's times can never be overwritten by the next vehicle in the same spot.

"Composition" means the payment has no life of its own: it is created for this ticket and is meaningless without it.

## Payment

```java
abstract class Payment {
    private double amount;
    private PaymentStatus status;
    private Date timestamp;

    public abstract boolean initiateTransaction();
}

class Cash extends Payment {
    public boolean initiateTransaction() { /* ... */ return true; }
}

class CreditCard extends Payment {
    public boolean initiateTransaction() { /* ... */ return true; }
}
```

Here `Payment` is an abstract class because every payment shares the same three fields: `amount`, `status`, and `timestamp`. Only `initiateTransaction` differs per method, so only that is abstract. The calling code still uses a `Payment` without knowing which subclass it has, so this is Strategy as well.

## Parking lot

```java
class ParkingLot {
    private int id;
    private String name;
    private Address address;
    private ParkingRate parkingRate;

    private Map<String, Entrance> entrances;
    private Map<String, Exit> exits;
    private Map<Integer, ParkingSpot> spots;
    private Map<String, ParkingTicket> tickets;
    private List<DisplayBoard> displayBoards;

    // Singleton implementation
    private static ParkingLot parkingLot = null;
    private ParkingLot() { /* ... */ }
    public static ParkingLot getInstance() {
        if (parkingLot == null) { parkingLot = new ParkingLot(); }
        return parkingLot;
    }

    public boolean addEntrance(Entrance entrance) { /* ... */ return true; }
    public boolean addExit(Exit exit) { /* ... */ return true; }
    public boolean addParkingSpot(ParkingSpot spot) { /* ... */ return true; }
    public boolean addDisplayBoard(DisplayBoard board) { /* ... */ return true; }

    public ParkingTicket getParkingTicket(Vehicle vehicle) { /* ... */ return null; }
    public boolean isFull(ParkingSpot spotType) { /* ... */ return false; }
}
```

`ParkingLot` is the central class: it owns every entrance, exit, spot, ticket, and display board, each kept in a map so it can be looked up by id.

The singleton here is the **lazy** form: the instance starts as `null` and is created on the first call to `getInstance()`. This form is not thread-safe as written, because two threads calling `getInstance()` at the same moment can both see `null` and create two lots.

## Executable code: the demo

The reference design also comes with a full runnable program. Its `Driver` class shows the core workflows:

- **System initialization**: creates the lot, spots, entrances, exits, and display boards.
- **Scenario 1**: a customer parks and receives a ticket.
- **Scenario 2**: the customer exits, the system calculates the fee, and the payment is processed.
- **Scenario 3**: more customers enter and park, and a new customer is rejected when the lot is full.

```java
public class Driver {
    public static void main(String[] args) throws InterruptedException {
        // -------------- SYSTEM INITIALIZATION --------------
        System.out.println("\n====================== PARKING LOT SYSTEM DEMO ======================\n");

        ParkingLot lot = ParkingLot.getInstance();
        lot.addSpot(new Accessible(1));
        lot.addSpot(new Compact(2));
        lot.addSpot(new Large(3));
        lot.addSpot(new MotorcycleSpot(4));

        DisplayBoard board = new DisplayBoard(1);
        lot.addDisplayBoard(board);

        Entrance entrance = new Entrance(1);
        Exit exit = new Exit(1);

        // ----------------- SCENARIO 1: CUSTOMER ENTERS, PARKS -----------------
        System.out.println("\n→→→ SCENARIO 1: Customer enters and parks a car\n");

        Vehicle car = new Car("KA-01-HH-1234");
        System.out.println("-> Car " + car.getLicenseNo() + " arrives at entrance");
        ParkingTicket ticket1 = entrance.getTicket(car);

        System.out.println("-> Updating display board after parking:");
        board.update(lot.getAllSpots());
        board.showFreeSlot();

        // ----------------- SCENARIO 2: CUSTOMER EXITS AND PAYS -----------------
        System.out.println("\n→→→ SCENARIO 2: Customer exits and pays\n");

        System.out.println("-> Car " + car.getLicenseNo() + " proceeds to exit panel");
        Thread.sleep(1500); // Simulate parking duration (1.5 sec)
        exit.validateTicket(ticket1);

        System.out.println("-> Updating display board after exit:");
        board.update(lot.getAllSpots());
        board.showFreeSlot();

        // --------- SCENARIO 3: FILLING LOT AND REJECTING ENTRY IF FULL ---------
        System.out.println("\n→→→ SCENARIO 3: Multiple customers attempt to enter; lot may become full\n");

        // Vehicles arriving
        Vehicle van = new Van("KA-01-HH-9999");
        Vehicle motorcycle = new Motorcycle("KA-02-XX-3333");
        Vehicle truck = new Truck("KA-04-AA-9998");
        Vehicle car2 = new Car("DL-09-YY-1234");

        System.out.println("-> Van " + van.getLicenseNo() + " arrives at entrance");
        ParkingTicket ticket2 = entrance.getTicket(van);

        System.out.println("-> Motorcycle " + motorcycle.getLicenseNo() + " arrives at entrance");
        ParkingTicket ticket3 = entrance.getTicket(motorcycle);

        System.out.println("-> Truck " + truck.getLicenseNo() + " arrives at entrance");
        ParkingTicket ticket4 = entrance.getTicket(truck);

        System.out.println("-> Car " + car2.getLicenseNo() + " arrives at entrance");
        ParkingTicket ticket5 = entrance.getTicket(car2);

        System.out.println("-> Updating display board after several parkings:");
        board.update(lot.getAllSpots());
        board.showFreeSlot();

        // Try to park another car (lot may now be full)
        Vehicle car3 = new Car("UP-01-CC-1001");
        System.out.println("-> Car " + car3.getLicenseNo() + " attempts to park (should be denied if lot is full):");
        ParkingTicket ticket6 = entrance.getTicket(car3);

        board.update(lot.getAllSpots());
        board.showFreeSlot();

        System.out.println("\n====================== END OF DEMONSTRATION ======================\n");
    }
}
```

How the demo flows:

1. The lot is fetched with `ParkingLot.getInstance()` and given four spots, one of each type.
2. `entrance.getTicket(vehicle)` is the whole entry step: it finds a spot, parks the vehicle, and returns the ticket.
3. `exit.validateTicket(ticket)` is the whole exit step: it calculates the fee, takes the payment, and frees the spot.
4. After each step, `board.update(lot.getAllSpots())` and `board.showFreeSlot()` refresh and print the free spots.
5. With only four spots, the later vehicles in scenario 3 show the "lot is full" rejection.

Only `Driver` is copied here. It calls methods that the skeletons above do not show, such as `lot.addSpot`, `lot.getAllSpots`, `board.update`, `car.getLicenseNo`, and constructors that take an id, so this file alone does not compile without the full versions of the other classes.
