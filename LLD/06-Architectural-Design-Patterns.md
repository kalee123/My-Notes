# Architectural Design Patterns

## Intro

Architectural patterns deal with **how the whole application is organised**. The other three categories shape a few classes; these shape the entire app, especially apps with a user interface.

All three patterns here share one goal: **separate the data and business logic from the UI**, so each can be changed and tested on its own. They all have a Model and a View; they differ in the third part that connects the two.

- **Model**: the data and business logic.
- **View**: what the user sees and interacts with.

## Patterns

| Pattern | Parts | How it works | Use it when |
|---------|-------|--------------|-------------|
| **MVC** | Model, View, Controller | The Controller receives user input and updates the Model; the View displays the Model. | Classic web apps and server-side frameworks. |
| **MVP** | Model, View, Presenter | The Presenter sits between the two: it takes input from the View, updates the Model, then tells the View what to show. View and Model never talk directly. | You want a very thin View and easily testable UI logic. |
| **MVVM** | Model, View, ViewModel | The View binds to the ViewModel; data binding updates the View automatically when the ViewModel changes. | The framework supports data binding (e.g. modern frontend and mobile frameworks). |

## How to tell them apart

- **MVC vs MVP**: in MVC the View can read the Model directly; in MVP everything goes through the Presenter.
- **MVP vs MVVM**: the Presenter holds a reference to the View and updates it by hand; the ViewModel does not know the View, and data binding does the updating.
- **Coupling between View and logic**: tightest in MVC, looser in MVP, loosest in MVVM.
