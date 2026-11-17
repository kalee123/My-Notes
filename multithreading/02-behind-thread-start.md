# What `new Thread().start()` really does

*2026-11-17*

## How to create a thread in Java

The `Thread` class represents a thread and has the methods to manage it (`start`, `join`, `sleep`, `interrupt`).
I pass it the work to do as a lambda (a `Runnable`):

```java
static int counter = 0;

Thread counter1 = new Thread(() -> {
    for (int i = 0; i < 1_000_000; i++) {
        counter++;
    }
});
counter1.start();     // the loop now runs on a new thread, separately from main
counter1.join();      // main waits for it to finish
System.out.println(counter);
```
`new Thread()` with no lambda creates a thread that has nothing to do.

## start() vs run()

**`start()`**: calls the native method `start0()` through JNI. The JVM's C++ code then calls
`pthread_create`, the OS kernel creates a real thread, and the OS scheduler decides when it runs on a core.
That new thread then calls `run()`. Java asks the OS for a thread, but the OS decides the scheduling.

**`run()`**: just a normal Java method. It runs the lambda on the **current** thread, and no new thread is created.

With `.run()` every task printed `main` in order 0..9. With `.start()` the order shuffled on every run.

## When does a thread end?
A thread ends when `run()` returns. The JVM keeps running while any non-daemon thread is alive.
I saw this: `main` finished, but my program kept running because the worker thread was still looping.

## Calling start() twice throws an error
`start()` checks `threadStatus` (that's why it's `synchronized`), so a second call throws
`IllegalThreadStateException`. One `Thread` object = one OS thread, and it can only start once.

## How to stop a thread
It can't be safely killed from outside (`Thread.stop()` is deprecated). Instead, ask it to stop with a
`volatile` flag or `interrupt()`, and let `run()` return. See [volatile and synchronized](03-volatile-and-synchronized.md).

## Under the hood
```
new Thread(...)   → just a Java object, no OS thread yet
.start()          → start0()  (native method)
                  → JNI → JVM_StartThread (C++)
                  → pthread_create        (same call as in C)
                  → kernel creates the real thread
                  → OS scheduler picks a core → my lambda runs
```
In JDK 17, `start()` is `public synchronized void start()` and calls `private native void start0()`.
`registerNatives()` links `start0` to the C++ function when the Thread class loads.

## JNI: calling C from Java
- `native` = the method body is written in C.
- `javac -h` generates the header with the exact C function name (`Java_com_app_ThreadTester_customThread`).
- Compile the C into a library (`cc -shared ... .dylib`), load it with `System.loadLibrary`,
  run with `-Djava.library.path`.

## Tried
Java called my C code, and my C code created real OS threads with `pthread_create`.
Each new thread got a fresh OS thread id, while the calling Java thread's id stayed the same.
