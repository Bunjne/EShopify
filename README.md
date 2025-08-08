# EShopify

A modern Android e-commerce application built using Jetpack Compose and Clean Architecture principles.

## Features

- Store details display with opening hours and ratings
- Product catalog browsing
- Product selection and checkout
- Successful order confirmation

## Tech Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Koin
- **Networking**: Retrofit + OkHttp
- **Serialization**: Kotlinx Serialization
- **Image Loading**: Coil
- **Date/Time**: Kotlinx DateTime
- **Navigation**: Jetpack Navigation Compose

## Project Structure

- **domain**: Contains business models, repositories interfaces, and use cases
- **data**: Implements repositories, includes remote DTOs and mappers
- **presentation**: Contains UI components, view models, and screens
- **di**: Koin dependency injection modules

## Code Style

This project follows Android development best practices:
- Separation of concerns with Clean Architecture
- Single responsibility principle with dedicated use cases
- Testable components with dependency injection
- Modern Kotlin features including coroutines and flows
