# wait, notify and deadlock

*2026-11-27*

## What is it?

**In one line:** `wait()` takes a thread out of the scheduler's queue until another thread says something has changed. `notify()` puts it back.

The scheduler only gives turns to threads that are ready to run
(see [Threads and processes](01-threads-and-processes.md)). Sometimes a thread cannot continue until
another thread does something: a customer cannot eat until the chef has cooked.

- Without `wait`, the customer stays in the queue, and on every turn it asks "is the food ready?",
  gets "no", and wastes its time slice.
- With `wait`, the customer leaves the queue and uses no CPU. When the chef finishes, `notify` puts the
  customer back, and on its next turn it eats.

| Call | Meaning |
|---|---|
| `lock.wait()` | "I have nothing to do until something changes. I am sleeping at `lock`." |
| `lock.notify()` | "Something changed. Wake **one** thread sleeping at `lock`." Java picks which. |
| `lock.notifyAll()` | "Wake **all** threads sleeping at `lock`." |

A sleeping thread has to sleep somewhere, so the right one can be found later. That place is an object.
This is why both threads must use the **same** object.

## The smallest program

```java
public class WaitNotifyDemo {

    private static final Object lock = new Object();
    private static boolean foodReady = false;

    public static void main(String[] args) {

        Thread customer = new Thread(() -> {
            synchronized (lock) {
                while (!foodReady) {
                    System.out.println("Customer: food not ready, waiting...");
                    try {
                        lock.wait();              // releases the lock and sleeps
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.println("Customer: eating");
            }
        });

        Thread chef = new Thread(() -> {
            try {
                Thread.sleep(2000);               // cooking
            } catch (InterruptedException e) {
                return;
            }
            synchronized (lock) {
                foodReady = true;
                System.out.println("Chef: food is ready");
                lock.notify();                    // wakes the waiting customer
            }
        });

        customer.start();
        chef.start();
    }
}
```

```
Customer: food not ready, waiting...
Chef: food is ready                       (2 seconds later)
Customer: eating
```

It always has four pieces:

| Piece | In the code |
|---|---|
| A shared lock object | `lock` |
| A condition | `foodReady` |
| A waiter | `while (!foodReady) lock.wait();` |
| A notifier | `foodReady = true; lock.notify();` |

## What happens, step by step

| Time | Customer | Chef | Who holds `lock` |
|---|---|---|---|
| 0s | Enters `synchronized (lock)`, sees no food, calls `lock.wait()` | Cooking (`sleep`) | **Nobody**: `wait()` gave it up |
| 0 to 2s | Asleep, no CPU | Cooking | Nobody |
| 2s | Asleep | Enters `synchronized (lock)`, sets `foodReady = true`, calls `notify()` | Chef |
| 2s | Woken, takes the lock back, checks `foodReady` again, eats | Done | Customer |

`wait()` **releases the lock** while sleeping. If it kept the lock, the chef could never enter its own
`synchronized` block, and both would be stuck forever.

## The rules

1. `wait()` and `notify()` must be inside `synchronized` **on the same object**. Otherwise:
   `IllegalMonitorStateException`.
2. Always wait in a `while` loop on a condition, not an `if`. After waking, the thread must check again.
3. A `notify()` sent when nobody is waiting is **lost**. It is not remembered. This is why the
   `foodReady` flag is needed: a customer who arrives late sees the flag and never waits.
4. Every `wait()` needs someone to call `notify()`, or the thread sleeps forever and the program never ends.

## synchronized: what can be locked

- `synchronized` goes on a **method** or a **block**. Never on a variable.
- A `synchronized` method locks on `this`. A block locks on the object named in the brackets.
- Any **object** can be the lock. Primitives (`int`, `boolean`) cannot: `synchronized (foodReady)` does not compile.
- Every object has `wait()` and `notify()`, because they come from the `Object` class.
  So `list.wait()` is legal, but only inside `synchronized (list)`.
- Locking an object does not protect it. It only means no other thread can enter a block synchronized on
  that same object. A variable is safe only when every thread touches it inside the same lock.

## Many customers

With several customers waiting, the one-customer program breaks in two ways:

- `notify()` wakes only **one**. The others sleep forever.
- A `boolean` cannot count. With `notifyAll()`, everyone wakes, sees `true`, and eats one plate.

Fix: count the plates, and wake everyone.

```java
public class ManyCustomers {

    private static final Object lock = new Object();
    private static int plates = 0;                 // how many plates are ready

    public static void main(String[] args) {
        int n = 3;

        for (int i = 1; i <= n; i++) {
            int id = i;
            new Thread(() -> {
                synchronized (lock) {
                    while (plates == 0) {
                        System.out.println("Customer " + id + ": waiting...");
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    plates--;                      // take one plate
                    System.out.println("Customer " + id + ": eating");
                }
            }).start();
        }

        new Thread(() -> {
            for (int i = 1; i <= n; i++) {
                try {
                    Thread.sleep(1000);            // cooking one plate
                } catch (InterruptedException e) {
                    return;
                }
                synchronized (lock) {
                    plates++;
                    System.out.println("Chef: plate " + i + " is ready");
                    lock.notifyAll();              // wake every waiting customer
                }
            }
        }).start();
    }
}
```

```
Customer 1: waiting...
Customer 3: waiting...
Customer 2: waiting...
Chef: plate 1 is ready
Customer 1: eating
Customer 2: waiting...
Customer 3: waiting...
Chef: plate 2 is ready
Customer 2: eating
Customer 3: waiting...
Chef: plate 3 is ready
Customer 3: eating
```

The order of the customers can change from run to run.

When one plate is ready, all customers wake. One gets the lock, takes the plate, and eats. The others
check `plates == 0` again, find nothing, and go back to sleep. That re-check is why it must be `while`:
with `if`, they would take a plate that does not exist and `plates` would go negative.

All the customers can be waiting on the one `lock` at the same time, because each `wait()` releases the
lock for the next customer to enter.

This is the **producer-consumer** problem: the chef produces, the customers consume.

## notify vs notifyAll with 10 waiting threads

```java
public class TenWaiters {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 1; i <= 10; i++) {
            int id = i;
            new Thread(() -> {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    System.out.println("Thread " + id + " woke up");
                }
            }).start();
        }

        Thread.sleep(1000);                 // let all 10 start waiting

        new Thread(() -> {
            synchronized (lock) {
                System.out.println("--- notify() ---");
                lock.notify();
            }
        }).start();

        Thread.sleep(1000);

        new Thread(() -> {
            synchronized (lock) {
                System.out.println("--- notifyAll() ---");
                lock.notifyAll();
            }
        }).start();
    }
}
```

```
--- notify() ---
Thread 1 woke up
--- notifyAll() ---
Thread 2 woke up
Thread 10 woke up
Thread 9 woke up
...
Thread 3 woke up
```

- `notify()` woke **one** of the ten. I cannot choose which, and it can differ between runs.
- `notifyAll()` woke the other **nine**. They do not run together: each must take `lock` back first,
  so they print one after another, in no fixed order.

## sleep vs wait

| | `Thread.sleep(ms)` | `lock.wait()` |
|---|---|---|
| Ends when | The time is up | Another thread calls `notify()` / `notifyAll()` on the same object |
| Lock | **Keeps** it | **Releases** it |
| Needs `synchronized` | No | Yes |
| Use for | A pause of known length | Waiting for another thread |

`sleep` is setting an alarm and hoping the food is ready when it rings. `wait` is "call me when it is ready".

Both pause **only the thread that calls them**.

## Waking one particular thread

`notify()` cannot choose. To wake a specific thread, wake everyone and let each one check if it is its turn:

```java
private static int turn = 0;

// thread with its own id
while (turn != id) {
    lock.wait();
}

// notifier
turn = 7;
lock.notifyAll();
```

Other ways: one lock object per thread, or `ReentrantLock` with several `Condition` objects (later).

## Deadlock

**In one line:** two threads each hold one lock and each waits for the lock the other holds. Neither lets go, so both are stuck forever.

Both threads are out of the scheduler's queue, each waiting for the other. Nobody is left to put them back.

```java
public class DeadlockDemo {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {                    // t1 takes A
                System.out.println("T1: got A, wants B");
                sleep(100);
                synchronized (lockB) {                // t1 waits for B
                    System.out.println("T1: got both");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {                    // t2 takes B
                System.out.println("T2: got B, wants A");
                sleep(100);
                synchronized (lockA) {                // t2 waits for A
                    System.out.println("T2: got both");
                }
            }
        });

        t1.start();
        t2.start();
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }
}
```

```
T1: got A, wants B
T2: got B, wants A
                          ← hangs here, "got both" never prints
```

| | Holds | Waiting for |
|---|---|---|
| T1 | A | B |
| T2 | B | A |

### Where it occurs

It needs **two or more locks, taken in a different order by different threads**.

- Nested `synchronized` blocks, as above.
- Bank transfers: thread 1 moves money X → Y (locks X, then Y) while thread 2 moves Y → X (locks Y, then X).
- A `synchronized` method calling another object's `synchronized` method, while another thread does the reverse.
- Databases: two transactions each lock one row and then ask for the other's row. The database detects it
  and cancels one of them.

### The fix

Every thread takes the locks **in the same order**. If both take A first and then B, one gets A, the other
waits at A while holding nothing, and no deadlock is possible.

With T2 changed to take `lockA` first and then `lockB`:

```
T1: got A, wants B
T1: got both
T2: got A, wants B
T2: got both
```

### How to find one

`jstack <pid>` on the hung program prints `Found one Java-level deadlock` and names the threads and the locks.

A deadlock is different from a missing `notify()`. That is a thread waiting for a call that never comes;
a deadlock is two threads blocking each other over locks. Both look like a hang.

## Tried

- Put a loop with `Thread.sleep(2000)` at the top of `main`, before `customer.start()` and `chef.start()`.
  The whole program paused for 6 seconds and only then the customer and chef started.
  **Why:** `sleep` pauses the thread that calls it, here `main`, and the other two threads did not exist yet.
  `main` cannot reach `start()` until the loop above it finishes.

## To try

- Remove `lock.notify()` → should hang forever.
- Remove `synchronized (lock)` around `lock.wait()` → should throw `IllegalMonitorStateException`.
- Many customers: change `while` to `if`, and `notifyAll()` to `notify()`.
- Write from a blank file: two threads printing 1 to 10 in turn, one odd and one even.
- Run `DeadlockDemo`, then apply the fix and run it again.

## Remember

- `wait` = leave the queue until told. `notify` = put one back. `notifyAll` = put all back.
- Same lock object on both sides, and always inside `synchronized`.
- `while`, not `if`.
- `wait` releases the lock. `sleep` does not.
- In real code, `BlockingQueue` does all of this for me: `take()` waits when empty, `put()` wakes the waiter.
- Deadlock = each thread holds what the other needs. It needs at least two locks. Fix: always take locks in the same order.
