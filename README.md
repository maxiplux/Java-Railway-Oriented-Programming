# Quantum Project

## Overview

This project demonstrates the implementation of the Railway Oriented Programming (ROP) pattern in Java using various examples. The project includes a user service that validates, maps, and saves user data using a railway pipeline.

## Project Structure

- `src/main/java/app/quantun/Main.java`: Contains the main class with various demo methods showcasing the ROP pattern.
- `src/main/java/app/quantun/rop/v1`: Contains the implementation of the ROP pattern version 1.
- `src/main/java/app/quantun/rop/v2`: Contains the implementation of the ROP pattern version 2.
- `src/main/java/app/quantun/rop/v3`: Contains the implementation of the ROP pattern version 3.
- `src/main/java/app/quantun/rop/v4`: Contains the implementation of the ROP pattern version 4.
- `src/main/java/app/quantun/v4/contract/UserDTO.java`: Defines the `UserDTO` record.
- `src/main/java/app/quantun/v4/services/UserRepository.java`: Defines the `UserRepository` interface.
- `src/main/java/app/quantun/v4/services/UserService.java`: Implements the `UserService` class with methods to create and validate users.

## Technologies Used

- Java
- Maven

## How to Run

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/quantum-project.git
    cd quantum-project
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the main class:
    ```sh
    mvn exec:java -Dexec.mainClass="app.quantun.Main"
    ```

## Examples

### User Creation

The `UserService` class demonstrates the creation of a user using a railway pipeline:

```java
UserService userService = new UserService();
RailwayHandler<UserDTO> result = userService.createUser(new UserDTO("John Doe", "demo@demo.com"));
if (result.isSuccess()) {
    System.out.println("Success: " + result.getValue());
} else {
    System.err.println("Failure: " + result.getError().getMessage());
}
