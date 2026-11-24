# Behavioral Design Patterns

## Intro

Behavioral patterns deal with **how objects communicate and share responsibilities**. They are not about what objects are made of, but about who does what and how a request flows between them.

Why this matters: when objects talk to each other directly and know too much about one another, a change in one breaks the others. Behavioral patterns keep that communication loosely coupled.

## Patterns

| Pattern | In one line | Use it when |
|---------|-------------|-------------|
| **Chain of Responsibility** | Passes a request along a chain of handlers until one handles it. | Several objects may handle a request and the handler is not known in advance (e.g. support levels L1 → L2 → L3). |
| **Command** | Wraps a request as an object. | You need undo/redo, queuing, or logging of actions. |
| **Iterator** | Walks through a collection without exposing how it is stored. | Callers should loop over a list, tree, or graph the same way. |
| **Mediator** | Objects talk through a central object instead of directly to each other. | Many objects interact in complex ways (e.g. a chat room, air traffic control). |
| **Observer** | When one object changes, all its subscribers are notified. | Many objects must react to an event (e.g. notifications, UI updates). |
| **Visitor** | Adds new operations to a set of classes without modifying them. | The classes are stable but new operations on them keep coming. |
| **Interpreter** | Defines a grammar and evaluates sentences written in it. | You need to evaluate a simple language (e.g. expressions, rules, search queries). |
| **Template** | A base class fixes the steps of an algorithm; subclasses fill in some steps. | Several classes follow the same overall flow with small differences. |
| **Memento** | Saves an object's state so it can be restored later. | You need snapshots or undo without exposing the object's internals. |
| **State** | An object changes its behaviour when its internal state changes. | Behaviour depends on state and `if/else` on the state is growing (e.g. a vending machine). |
| **Strategy** | A family of interchangeable algorithms, chosen at runtime. | There are several ways to do one task (e.g. pay by card, UPI, or wallet). |

## How to tell them apart

- **Strategy vs State**: same structure. In Strategy the client picks the algorithm; in State the object switches behaviour itself as its state changes.
- **Strategy vs Template**: Strategy swaps the whole algorithm through composition; Template changes only some steps through inheritance.
- **Observer vs Mediator**: Observer is one-to-many broadcasting; Mediator is a central hub coordinating many-to-many communication.
- **Command vs Memento**: both support undo. Command stores the action; Memento stores the state.
- **Chain of Responsibility vs Command**: Chain decides who handles a request; Command packages what the request is.
