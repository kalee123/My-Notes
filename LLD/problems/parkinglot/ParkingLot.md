# Parking Lot

## Problem definition

A parking lot is a designated area for parking vehicles, commonly found in venues like shopping malls, sports stadiums, and office buildings.

- It has a **fixed number of parking spots**, allocated for different types of vehicles.
- Each spot is **charged based on the duration** a vehicle remains parked.
- Parking time is tracked using a **parking ticket** issued at the entrance.
- On exit, the customer pays at an **automated exit panel** or through a **parking agent**, using a **credit/debit card or cash**.

![The layout of the parking lot](../../images/parking-lot-layout.png)

What the layout shows:

- There are **two entrances and two exits**, so several vehicles can enter and leave at the same time.
- Spots are grouped by type: **cars**, **motorcycles**, **van / truck**, and **accessible**.
- Spot sizes differ: a van / truck spot is much larger than a motorcycle spot.

## What the design must cover

1. **Vehicle types**: manage parking for car, van, truck, motorcycle, etc., across potentially multiple floors and zones.
2. **Spot types**: allocate and track spot availability by type (compact, large, accessible, motorcycle).
3. **Tickets**: issue and manage parking tickets to track entry, parking duration, and exit.
4. **Payments**: pay at automated exit panels or via human agents, with multiple payment methods (cash, card, coupon).
5. **Pricing**: pricing models may vary by hour, spot type, or vehicle type.
6. **Occupancy**: track spot occupancy in real time and keep entry/exit flows efficient during busy periods.
7. **Special cases**: accessible parking spots and overflow situations (the lot is full).

## The simplest story

Before designing for all seven points, start with one path through the system:

> A car enters, gets a ticket, parks in a free spot, exits, and pays.

Everything else is an upgrade to this story.

## Scenarios

The situations the design has to handle, starting from the simplest story.

| # | Scenario | Expected behaviour |
|---|----------|--------------------|
| 1 | A vehicle arrives and a suitable spot is free | It gets a ticket with the entry time and parks in that spot. |
| 2 | A vehicle exits | The fee is worked out from the parked duration, the customer pays, and the spot becomes free. |
| 3 | Different vehicle types arrive (bike, car, van, truck) | Each is given only a spot that fits it: a truck never gets a motorcycle spot. |
| 4 | The lot is full | The vehicle is refused at the entrance. The lot can be full for one type (trucks) while spots for another (bikes) are free. |
| 5 | Two vehicles arrive at the two entrances at the same moment | Each gets a different spot; the same spot is never given to both. |
| 6 | Vehicles of different types stay for the same time | The fee can differ by vehicle type or spot type. |
| 7 | The customer pays by cash, card, or UPI | Any method works at the exit, and adding a new method does not change the exit flow. |
| 8 | The payment fails | The vehicle is not let out until a payment succeeds, possibly with another method. |
| 9 | A vehicle needs an accessible spot | It is given an accessible spot, and other vehicles are not. |
| 10 | The lot has several floors | A vehicle is sent to a floor with a free spot, and free-spot counts are known per floor. |

- My solution and its current status: [ParkingLot-Solution.md](ParkingLot-Solution.md)
- The reference solution: [ParkingLot-Reference-Solution.md](ParkingLot-Reference-Solution.md)
