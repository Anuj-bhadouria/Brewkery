# Brewkery

Brewkery is a native Android coffee and bakery ordering application built as part of an Android Developer technical assignment.

## Features

- API-driven menu
- Category filtering
- Product details and ingredients
- Dynamic product customization and pricing
- Cart management and quantity controls
- $2.50 delivery charge and 8% tax calculation
- Simulated order placement and `PREPARING` status
- Active order tracking
- Loading and error states
- Unit testing for pricing logic

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Retrofit
- Kotlin Coroutines
- ViewModel
- StateFlow
- Coil
- JUnit

## API

**Base URL**

```text
https://raw.githubusercontent.com/VivekShah138/Brewkery/main/Endpoints
```

GET /data.json
GET /api/items/{id}.json
Architecture
UI → ViewModel → Repository → Retrofit API

The project is organized into:

com.AB.brewkery/
├── data/
│   ├── api/
│   ├── model/
│   └── repository/
├── domain/
│   ├── CartItem
│   └── PriceCalculator
├── UI/
│   ├── cart/
│   ├── detail/
│   ├── menu/
│   └── status/
└── MainActivity.kt
## Testing

Unit tests are included for the pricing logic in PriceCalculatorTest.

The checkout calculation follows:

Subtotal + 8% Tax + $2.50 Delivery = Total

Example:

$9.40 + $0.75 + $2.50 = $12.65



## AI Assistance

AI tools were used as development assistance throughout the project. They were mainly used for breaking the assignment into manageable phases, understanding requirements, interpreting errors and crashes, reviewing implementation approaches, and helping with documentation.

### Tools Used

- Claude
- Gemini in Android Studio

### How AI Was Used

AI assistance was used for:

- Breaking the assignment into development phases and planning the implementation.
- Understanding the provided API structure and requirements.
- Explaining Android, Kotlin, and Jetpack Compose errors.
- Analysing crash messages and helping identify their likely causes.
- Reviewing implementation approaches and suggesting possible solutions.
- Assisting with documentation and README preparation.

### What AI Got Right

AI was particularly useful during debugging. By providing the relevant error messages and logs, I was able to understand what was causing certain build and runtime issues much faster.

It was also useful for breaking down the assignment into smaller implementation phases, which helped keep the development process organised within the given time limit.

### What AI Got Wrong

AI did not always provide the correct fix. For example, during one debugging issue, Claude suggested restarting/re-running the application as a possible solution. Restarting did not resolve the underlying problem.

### How I Fixed It

I used the error information and suggestions from AI as a starting point, then investigated the relevant code and project configuration myself. I identified the actual cause, implemented the fix, and rebuilt and tested the application to verify that the issue was resolved.

AI was therefore used primarily for assistance and error recognition; the final implementation, fixes, and verification were done by me.
