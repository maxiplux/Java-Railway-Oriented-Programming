# Java Railway Oriented Programming

## Overview

This project demonstrates a comprehensive implementation of the **Railway Oriented Programming (ROP)** pattern in Java. Railway Oriented Programming is a functional programming approach to error handling that makes your code more composable, predictable, and easier to reason about.

### What is Railway Oriented Programming?

Railway Oriented Programming is a design pattern that treats your program flow like a railway track with two rails:
- **Success Track**: Operations continue flowing when everything works correctly
- **Failure Track**: When an error occurs, the flow automatically switches to the failure track and bypasses remaining operations

This pattern eliminates the need for nested try-catch blocks and makes error handling explicit and compositional.

### Why Use Railway Oriented Programming?

✅ **Eliminates Pyramid of Doom**: No more deeply nested if-else or try-catch blocks  
✅ **Explicit Error Handling**: Errors are part of the type system, not hidden exceptions  
✅ **Composable Operations**: Chain operations together in a clean, functional style  
✅ **Fail-Fast Behavior**: Automatically short-circuits on first error  
✅ **Type Safety**: Compile-time guarantees about success and failure cases  
✅ **Easier Testing**: Each operation can be tested independently  

### When to Use ROP?

- **Data validation pipelines**: Validate, transform, and process data through multiple steps
- **Business workflows**: Execute sequential business rules where any step can fail
- **API request handling**: Validate input → Process → Transform → Respond
- **Database operations**: Validate → Map → Save → Return result
- **ETL processes**: Extract → Transform → Load with proper error handling

## Project Structure

```
src/main/java/app/quantun/
├── Main.java                          # Demo entry point with examples
├── rop/
│   ├── v1/                           # Version 1: Basic Result Pattern
│   │   ├── Result.java              # Either-style Success/Failure types
│   │   ├── Operation.java           # Operation interface
│   │   └── OperationBuilder.java    # Builder for chaining operations
│   ├── v2/                           # Version 2: Railway Builder Pattern
│   │   ├── RailwayPattern.java      # Enhanced result type
│   │   └── RailwayBuilder.java      # Fluent builder for pipelines
│   └── v3/                           # Version 3: Functional Railway Handler
│       └── RailwayHandler.java      # Full-featured monadic implementation
└── v4/                               # Version 4: Real-world Example
    ├── contract/
    │   └── UserDTO.java             # Data transfer object
    ├── model/
    │   └── User.java                # Domain entity
    └── services/
        ├── UserRepository.java      # Persistence interface
        └── UserService.java         # Complete ROP implementation
```

## Implementation Evolution

This project demonstrates the evolution of Railway Oriented Programming implementation through four versions:

### Version 1: Basic Result Pattern
A foundational implementation using the Either pattern with `Success` and `Failure` types. Operations can be chained using an `OperationBuilder`.

**Key Features:**
- Simple `Result<T, E>` type with Success and Failure subclasses
- Basic operation chaining
- Good starting point for understanding the pattern

### Version 2: Railway Builder Pattern
Enhanced version with a dedicated builder pattern for constructing railway pipelines.

**Key Features:**
- `RailwayBuilder` for fluent pipeline construction
- Improved step composition
- Clearer separation of pipeline definition and execution

### Version 3: Functional Railway Handler
A mature, monadic implementation with full functional programming capabilities.

**Key Features:**
- `flatMap()` for chaining operations that return `RailwayHandler`
- `map()` for simple transformations
- Type transformations (e.g., `RailwayHandler<Integer>` → `RailwayHandler<String>`)
- Automatic exception handling
- Both builder-style and functional-style APIs

### Version 4: Real-World Application
Complete example demonstrating ROP in a realistic user management scenario.

**Key Features:**
- Full user creation pipeline: Validation → Mapping → Persistence → Response
- Multiple validation steps
- Error handling at each stage
- Integration with repository pattern

## Technologies Used

- **Java** (Modern Java with functional programming features)
- **Maven** (Build and dependency management)

## How to Run

1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd Java-Railway-Oriented-Programming
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the main class to see demos:
    ```sh
    mvn exec:java -Dexec.mainClass="app.quantun.Main"
    ```

## Examples

### Example 1: Basic Number Validation Pipeline

```java
// Define validation and transformation operations
Function<Integer, RailwayHandler<Integer>> validateNumber = (number) -> {
    if (number > 0) {
        return RailwayHandler.success(number);
    } else {
        return RailwayHandler.failure(new IllegalArgumentException("Number must be positive"));
    }
};

Function<Integer, RailwayHandler<Double>> multiplyByTwo = (number) ->
    RailwayHandler.success(number * 2.5);

Function<Double, String> convertToString = (number) -> 
    "The final result is: " + number;

// Chain operations together
RailwayHandler<String> result = RailwayHandler
    .success(10)
    .flatMap(validateNumber)    // Validation
    .flatMap(multiplyByTwo)      // Transformation
    .map(convertToString);       // Final mapping

// Handle result
if (result.isSuccess()) {
    System.out.println("Success: " + result.getValue());
} else {
    System.err.println("Failure: " + result.getError().getMessage());
}
```

**Output:** `Success: The final result is: 25.0`

If you change the input to a negative number (e.g., `-5`), it automatically fails:  
**Output:** `Failure: Number must be positive`

### Example 2: User Creation Pipeline

The `UserService` class demonstrates a complete real-world scenario:

```java
public RailwayHandler<UserDTO> createUser(UserDTO userDTO) {
    return RailwayHandler.success(userDTO)
        .flatMap(this::validateDTO)      // Step 1: Validate input
        .flatMap(this::mapToEntity)      // Step 2: Convert DTO to Entity
        .flatMap(this::saveEntity)       // Step 3: Persist to database
        .map(this::mapToDTO);            // Step 4: Convert back to DTO
}
```

**Usage:**

```java
UserService userService = new UserService();
RailwayHandler<UserDTO> result = userService.createUser(
    new UserDTO("John Doe", "demo@demo.com")
);

if (result.isSuccess()) {
    System.out.println("Success: " + result.getValue());
} else {
    System.err.println("Failure: " + result.getError().getMessage());
}
```

**What happens:**
1. If the name is empty → fails with "Name cannot be null or empty"
2. If email is invalid → fails with "Invalid email address"
3. If mapping fails → fails with exception details
4. If database save fails → fails with exception details
5. If all succeed → returns the saved user DTO

## Key Concepts

### flatMap vs map

- **`flatMap()`**: Use when your operation returns a `RailwayHandler` (for operations that can fail)
  ```java
  .flatMap(this::validateDTO)  // validateDTO returns RailwayHandler<UserDTO>
  ```

- **`map()`**: Use for simple transformations that always succeed
  ```java
  .map(this::mapToDTO)  // mapToDTO returns UserDTO directly
  ```

### Error Propagation

Once any operation in the pipeline fails, all subsequent operations are automatically skipped:

```java
RailwayHandler.success(10)
    .flatMap(validatePositive)   // ❌ Fails here
    .flatMap(multiplyByTwo)      // ⏭️ Skipped
    .flatMap(subtractFive)       // ⏭️ Skipped
    .map(convertToString);       // ⏭️ Skipped
// Result: Failure with validation error
```

### Type Transformations

Railway pattern supports type transformations throughout the pipeline:

```java
RailwayHandler<Integer> → flatMap → RailwayHandler<Double> → map → RailwayHandler<String>
```

## Benefits Demonstrated in This Project

1. **No Exception Handling Boilerplate**: No try-catch blocks in business logic
2. **Clear Data Flow**: Easy to see the sequence of operations
3. **Testable**: Each step can be unit tested independently
4. **Maintainable**: Easy to add, remove, or reorder operations
5. **Type Safe**: Compiler ensures correct types at each stage

## Contributing

Contributions are welcome! Feel free to:
- Add new versions or variations of the pattern
- Improve documentation
- Add more real-world examples
- Optimize implementations

## License

This project is available for educational purposes. Feel free to use it as a reference for implementing Railway Oriented Programming in your own Java projects.

## Further Reading

- [Railway Oriented Programming (Original Article)](https://fsharpforfunandprofit.com/rop/) by Scott Wlaschin
- [Functional Error Handling in Java](https://www.baeldung.com/java-either)
- Java Optional and functional programming patterns
