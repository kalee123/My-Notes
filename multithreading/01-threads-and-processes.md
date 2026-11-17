# Threads and processes

*2026-11-17*

## What is a thread?

A thread is a worker inside a program that runs code step by step, one task at a time.
A program can have many threads, like a kitchen with many cooks.

All code runs on some thread. Each running program is a process. For example, the JVM is a process
that contains the main thread, daemon (background) threads, GC threads etc. In a web app, each request
is handled by its own thread.

Consider a Mac with the 11-core M3 Pro chip: each core runs one thread at a time, so it can run 11 threads
simultaneously. Other threads wait for their turn. Many Intel chips have hyper-threading: one core can
run 2 threads, so an 8-core chip can run 16 threads at a time. The 2 threads share the same core, so it
is faster but not double the speed. Apple chips don't have hyper-threading.

## How does it work?

At the OS level, most threads are in a waiting state: they are sleeping until they have work, and a
sleeping thread uses no CPU. The threads that are ready to run take turns on the cores: the OS scheduler
lets a thread run for a short time slice (a few milliseconds), then pauses it and runs the next one, and
so on. When it pauses a thread, the OS saves exactly where it was, so it can continue later from the same
point. The switch itself takes only microseconds. It happens so fast that as a user we don't notice it:
everything looks like it runs at the same time.

Each thread has its own stack for its local variables and method calls. Objects on the heap are shared
by all threads of the process, which is why threads can step on each other's data.

- **Process** = a running program with its own memory (AlgoDesk's JVM, Chrome...).
- **Thread** = a worker inside a process. Threads of one process **share** its memory.
- **Core** = the hardware that runs a thread. My M3 Pro has 11 cores (5 performance + 6 efficiency),
  so 11 threads run at the exact same moment. The rest take turns: the OS scheduler switches them every few ms.


## Tried
`jps` + `jstack <pid>` on AlgoDesk showed ~50 threads:
- Tomcat's `http-nio-8080-exec-1..10`: a pool of threads that handle requests, reused from one request to the next
- `scheduling-1`: from my `@EnableScheduling`
- `pool-2-thread-1..4`: my own `DashBoardService` pool, born when I opened the Dashboard
- GC threads: 9 of them, sized by the JVM from my 11 cores

Almost all were `waiting`: a sleeping thread uses no CPU.
