# Spring Boot Reactive Web Application with Resilience4j

This is a Spring Boot application that demonstrates how to implement Circuit Breaker and Retry
patterns using Resilience4j. The application fetches customer data from an external Express API and
utilizes reactive programming principles.

## Features

- Fetches customer data from an external Express Based API.
- Implements Circuit Breaker and Retry patterns to handle failures gracefully.
- Uses reactive programming with Project Reactor.
- Exposes health and status endpoints through Spring Boot Actuator.

## Technologies Used

- **Spring Boot**: 3.3.4
- **Spring WebFlux**: 3.3.4
- **Resilience4j**: 1.7.1
- **Project Reactor**: 3.4.16
- **Maven**: 3.8.6

## Setup

1. **Clone the external service repository**:

   ```bash
   git clone https://github.com/priyodas12/ExpressJSV2.git

