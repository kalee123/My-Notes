# Parking Lot: My Solution

- Problem statement and scenarios: [ParkingLot.md](ParkingLot.md)
- The reference solution: [ParkingLot-Reference-Solution.md](ParkingLot-Reference-Solution.md)

## What runs today

The simplest story runs end to end from [ParkingLotMain.java](ParkingLotMain.java): a car enters, parks in a free spot, exits, and pays by UPI.

```
CAR TN01AB1234 parked at A1
Paid 20.0 by UPI
Spot A1 is free again
Fee: 20.0
```

## Classes

| Package | Class | Job |
|---------|-------|-----|
| `parkinglot` | [ParkingLotMain](ParkingLotMain.java) | Runs the story. Talks only to the facade. |
| `parkinglot` | [ParkingLotFacade](ParkingLotFacade.java) | `enter(vehicle)` and `exit(spot, payment)`. Runs parking, fee calculation, and payment in order. |
| `parkinglot` | [ParkingSpot](ParkingSpot.java) | One spot: floor, label, the parked vehicle (`null` means free), in time and exit time. |
| `parkinglot.parking` | [ParkingOperations](parking/ParkingOperations.java) | Holds the 100 spots. `entry` finds the first free spot; `exit` frees it. |
| `parkinglot.vechicle` | [Vehicle](vechicle/Vehicle.java), [VehicleType](vechicle/VehicleType.java) | Interface and enum for vehicles. |
| `parkinglot.vechicle` | `Car`, `Bike`, `Van`, `Truck` | One class per vehicle type. |
| `parkinglot.payment` | [Payment](payment/Payment.java), [PaymentType](payment/PaymentType.java) | Interface and enum for payments. |
| `parkinglot.payment` | `CashPayment`, `CardPayment`, `UpiPayment` | One class per payment method. |
| `parkinglot.payment` | [FeeCalculator](payment/FeeCalculator.java) | 20 per started hour, minimum one hour. |

## Patterns used

| Pattern | Where | Why |
|---------|-------|-----|
| **Singleton** | [ParkingOperations.java](parking/ParkingOperations.java) | There is one lot, so every entrance and exit must share one list of spots. |
| **Strategy** | [Payment.java](payment/Payment.java) with `CashPayment`, `CardPayment`, `UpiPayment` | `exit` calls `payment.pay(fee)` without knowing which payment it was given. A new payment method is a new class, with no change to existing code. |
| **Facade** | [ParkingLotFacade.java](ParkingLotFacade.java) | `main` makes one call; the facade runs parking, fee calculation, and payment in the right order. |

### Singleton: the three parts

1. A `private` constructor, so no other class can call `new ParkingOperations(...)`.
2. A `private static final` field holding the one instance.
3. A `public static getInstance()` that returns it.

Writing any constructor also stops Java from adding the hidden no-argument one, so `new ParkingOperations()` fails too.

### Facade is not encapsulation

Hiding a loop inside a method is encapsulation, and every class does that. It is a Facade only because it coordinates **several other classes**: parking, pricing, and payment.

### Factory vs Strategy

Factory decides which object to **create**. Strategy decides which behaviour **runs**. A class that returns the object is a Factory; a class that calls a method on the object it was given is using a Strategy.

Factory is not used yet. It would turn a `PaymentType` chosen by the customer into the right `Payment` object, which Strategy then uses:

```
PaymentType.UPI  ->  PaymentFactory.create()  ->  UpiPayment object  ->  exit() calls pay()
                     (Factory: creates it)                              (Strategy: uses it)
```

### Interface vs abstract class for Payment

`Payment` is an interface because cash, card, and UPI share only *what* they do, not *how*. An abstract class is worth adding under it only when the implementations start sharing fields or code.

## Status against the scenarios

Scenario numbers are from [ParkingLot.md](ParkingLot.md).

| # | Scenario | Status | Notes |
|---|----------|--------|-------|
| 1 | Vehicle arrives, spot is free | Partly | Parks in the first free spot. No `Ticket`: the entry time is stored on the spot. |
| 2 | Vehicle exits | Done | Fee from the parked duration, payment taken, spot freed. |
| 3 | Different vehicle types | Partly | The four vehicle classes exist, but every spot is identical, so any vehicle fits any spot. |
| 4 | Lot is full | Partly | `entry` throws `IllegalStateException("Parking lot is full")`. Not per type. |
| 5 | Two vehicles at the same moment | Missing | `entry` and `exit` are not `synchronized`. |
| 6 | Fee differs by type | Missing | One flat rate for every vehicle. |
| 7 | Cash, card, or UPI | Done | Through the `Payment` interface. |
| 8 | Payment fails | Partly | The facade throws if `pay` returns `false`, but the spot has already been freed by then. |
| 9 | Accessible spot | Missing | |
| 10 | Several floors | Missing | All 100 spots are on floor 1. |

## What to add next

In priority order. The first three are what an interviewer expects to see.

1. **Ticket**
   - `enter` returns a `Ticket` holding the vehicle, the spot, and the entry time; `exit` takes that `Ticket`.
   - Fixes a flaw in the current code: times live on the spot, so the next vehicle in the same spot overwrites them.
2. **Spot types and matching**
   - Add a `SpotType` enum (`MOTORCYCLE`, `COMPACT`, `LARGE`) and give each `ParkingSpot` a type.
   - `entry` picks only a free spot that fits the vehicle. "Lot full" then becomes per type.
3. **Pricing by vehicle type**
   - Different hourly rates, for example bike 10, car 20, truck 40.
   - Put it behind a `PricingStrategy` interface, which is a second use of Strategy.
4. **Payment before freeing the spot**
   - In `exit`, take the payment first and free the spot only if it succeeds.
5. **Multiple floors**
   - A `Floor` class holds spots; the lot holds floors, with free-spot counts per floor and per type.
6. **Two vehicles entering at the same time**
   - Mark `entry` and `exit` as `synchronized`, so only one gate assigns a spot at a time.
7. **Accessible spots**
   - One more value in `SpotType`, given only to vehicles flagged as needing it.
8. **PaymentFactory**
   - `PaymentFactory.create(PaymentType)` so the caller passes a type, not `new UpiPayment()`.
