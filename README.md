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

Base URL

```text
https://raw.githubusercontent.com/VivekShah138/Brewkery/main/Endpoints
```

## Endpoints

```text
GET /data.json
GET /api/items/{id}.json
```
## Architecture

```com.AB.brewkery/
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
```

## Testing

Unit tests are included for the pricing logic in PriceCalculatorTest.
The checkout calculation follows:
```
Subtotal + 8% Tax + $2.50 Delivery = Total
```
Example:
```
$9.40 + $0.75 + $2.50 = $12.65
```
## AI Assistance

AI tools were used during development as a supporting resource for planning, debugging, implementation guidance, and documentation.

### Tools Used
    Claude
    Gemini in Android Studio
    How AI Was Used

### AI assistance was mainly used to:

- Identify the tools, libraries, and technologies required for the project.
- Break the assignment into manageable development phases.
- Understand requirements and API-related details.
- Explain Android/Kotlin/Jetpack Compose errors.
- Analyse build errors, runtime errors, and crash logs.
- Suggest possible approaches when troubleshooting issues.
- Assist with project documentation and README preparation.

## Actual Prompts Used
```"I applied for an Android Developer role and have been tasked with building this application. Here is the assignment guide. Help me understand the requirements and identify the tools and libraries I will need for the project."```

```"Now that I understand the requirements, let's divide the implementation into phases so that each phase can be tested and debugged before moving to the next one, and make the final unit testing easier."```

```"Here is the Android error/crash log. Help me understand what is causing the issue and where I should investigate in the project."```
## What AI Got Right

  AI was useful for breaking the project into smaller development phases and identifying relevant issues from build errors, runtime errors, and logs. This made it easier to work through the implementation and debugging process step by step.

## What AI Got Wrong

Some suggested approaches did not directly resolve the underlying issue. During one runtime issue, the application still failed after following the suggested approach.

## How I Fixed It

I investigated the error using the available logs and the relevant project code, identified the underlying cause, implemented the required fix, and re-tested the application to verify that the issue was resolved.

AI suggestions were treated as development assistance, while the final implementation, debugging, testing, and verification were performed during the development process.
