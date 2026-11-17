# volatile and synchronized

*2026-11-17*

Threads share objects on the heap. That causes two different problems, and each has its own fix.

## volatile: visibility

**In one line:** without `volatile`, a thread may never see another thread's change.

```java
static boolean running = true;

public static void main(String[] args) throws InterruptedException {
    Thread test = new Thread(() -> {
        int val = 0;
        while (running) {
            val++;
        }
        System.out.println(val);
    });
    test.start();
    Thread.sleep(2000);
    running = false;
}
```

In the above code, even though main sets `running` to `false`, thread `test` runs forever.

**Why:** the Java Memory Model doesn't guarantee that one thread ever sees another thread's change,
unless the variable is `volatile` (or protected by `synchronized`). The JIT (Just-In-Time compiler) uses
this freedom: when it turns the hot loop's bytecode into machine code, it sees that nothing inside the loop
changes `running`, so it reads `running` only **once**, keeps it in a CPU register, and never reads memory again.

```java
static volatile boolean running = true;
```

Adding `volatile` makes the JVM read the value from memory every time, so the loop ends after 2 seconds.
As the name suggests, volatile means changeable: we are promising the JVM that another thread will change
this value. Same meaning as in English: a volatile market changes suddenly.

### Tried
- without `volatile` → ran forever
- with `-Xint` (JIT off), still without `volatile` → stopped. That proves it's the JIT.
- with `volatile` → stopped

The worker wrote the answer on its hand and stopped looking at the notice board.
`volatile` forces it to look at the board every time.

## synchronized: one thread at a time

Threads **share** the same memory (the heap and static fields), so both threads below change the same `counter`.

```java
static int counter = 0;

Thread counter1 = new Thread(() -> {
    for (int i = 0; i < 1_000_000; i++) {
        counter++;
    }
});
Thread counter2 = new Thread(() -> {
    for (int i = 0; i < 1_000_000; i++) {
        counter++;
    }
});

counter1.start();
counter2.start();
counter1.join();
counter2.join();
System.out.println("Expected 2000000, got " + counter);
```

This causes a **race condition**. Why? `counter++` looks like one step but is actually three:
**read → add → write**. The OS can pause a thread between those steps, and the other thread then works
with the same old value:

```
counter = 5
Thread 1: read 5            → paused by the OS
Thread 2: read 5
Thread 2: add 1, write 6
Thread 1: (resumes) add 1, write 6     ← Thread 2's increment is lost
counter = 6, but it should be 7
```

Both threads read the latest value correctly, but neither knows the other is halfway through an update.

- `volatile` does **not** fix this. It only fixes visibility.
- `synchronized` is a lock: only one thread at a time inside. It also handles visibility.
- It goes on **methods or blocks**, not on variables:
  ```java
  static synchronized void increment() {
      counter++;
  }
  ```

### What join() does
`counter1.join()` means: **the current thread (main) waits here until `counter1` has finished.**

Without it, main keeps going right after `start()` and prints `counter` while the threads are still
counting, so it prints some random in-between value. That's not the race condition, just printing too early.

```java
counter1.start();
counter2.start();
counter1.join();     // main sleeps until counter1 is done
counter2.join();     // then until counter2 is done
System.out.println(counter);   // both finished, so this is the final value
```

- `join()` also guarantees **visibility**: everything the finished thread wrote is visible to main after
  `join()` returns, so `counter` doesn't need `volatile` just for printing.
- `join()` can throw `InterruptedException`, so `main` needs `throws InterruptedException` (or a try/catch).
- It's Java's version of `pthread_join` in C.

### Tried
2 threads × 1,000,000 increments:

| Version | Result |
|---|---|
| `counter++` | ~1,200,000 ❌ |
| `volatile` + `counter++` | ~1,200,000 ❌ |
| `synchronized increment()` | 2,000,000 ✅ |

With only 1,000 increments the bug hid: each thread finished before the other started.

## Remember
| | `volatile` | `synchronized` |
|---|---|---|
| Everyone sees the latest value | ✅ | ✅ |
| Only one thread at a time | ❌ | ✅ |
| Use for | A flag one thread sets and others read | Read-then-write steps like `counter++` |
