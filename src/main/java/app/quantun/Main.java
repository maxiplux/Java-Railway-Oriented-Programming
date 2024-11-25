package app.quantun;

import app.quantun.rop.v1.Operation;
import app.quantun.rop.v1.OperationBuilder;
import app.quantun.rop.v1.Result;
import app.quantun.rop.v2.RailwayBuilder;
import app.quantun.rop.v2.RailwayPattern;
import app.quantun.rop.v3.RailwayHandler;
import app.quantun.v4.contract.UserDTO;
import app.quantun.v4.services.UserService;

import java.util.function.Function;


public class Main {
    static Function<Integer, RailwayHandler<Integer>> validateNumber = (number) -> {
        if (number > 0) {
            return RailwayHandler.success(number);
        } else {
            return RailwayHandler.failure(new IllegalArgumentException("Number must be positive"));
        }
    };
    public static void main(String[] args) {
        UserService userService = new UserService();

        RailwayHandler<UserDTO> result = userService.createUser(new UserDTO ("John Doe", "demo@demo.com"));
        if (result.isSuccess()) {
            System.out.println("Success: " + result.getValue());
        } else {
            System.err.println("Failure: " + result.getError().getMessage());
        }


        //demo4();

    }

    private static void demo4() {
        // Define operations


        // Define operations that transform or operate on the input
        Function<Integer, RailwayHandler<Integer>> validateNumber = (number) -> {
            if (number > 0) {
                return RailwayHandler.success(number);
            } else {
                return RailwayHandler.failure(new IllegalArgumentException("Number must be positive"));
            }
        };

        Function<Integer, RailwayHandler<Double>> multiplyByTwo = (number) ->
                RailwayHandler.success(number * 2.5);


        Function<Double, String> convertToString = (number) -> "The final result is: " + number;

        // Use RailwayHandler to chain these operations together
        RailwayHandler<String> result = RailwayHandler
                .success(10)  // Start with initial value of type Integer
                .flatMap(validateNumber)  // Validation still returns RailwayHandler<Integer>
                .flatMap(multiplyByTwo)    // Changes to RailwayHandler<Double>
                .map(convertToString);     // Final mapping to a String representation

        if (result.isSuccess()) {
            System.out.println("Success: " + result.getValue());
        } else {
            System.err.println("Failure: " + result.getError().getMessage());
        }
    }

    private static void demo3() {
        Function<Integer, RailwayHandler<Integer>> multiplyByTwo = (number) ->
                RailwayHandler.success(number * 2);

        Function<Integer, RailwayHandler<Integer>> subtractFive = (number) ->
                RailwayHandler.success(number - 5);


        // Use RailwayHandler to chain these operations together
        RailwayHandler<Integer> result = RailwayHandler
                .start(10)  // Start with initial value
                .addStep(validateNumber)
                .addStep(multiplyByTwo)
                .addStep(subtractFive)
                .execute(); // Execute all steps

        if (result.isSuccess()) {
            System.out.println("Success: " + result.getValue());
        } else {
            System.err.println("Failure: " + result.getError().getMessage());
        }
    }

    private static void demo2() {
        Function<Integer, RailwayPattern<Integer>> validateNumber = (number) -> {
            if (number > 0) {
                return RailwayPattern.success(number);
            } else {
                return RailwayPattern.failure(new IllegalArgumentException("Number must be positive"));
            }
        };

        Function<Integer, RailwayPattern<Integer>> multiplyByTwo = (number) ->
                RailwayPattern.success(number * 2);

        Function<Integer, RailwayPattern<Integer>> subtractFive = (number) ->
                RailwayPattern.success(number - 5);

        // Build a railway pipeline using the builder
        RailwayBuilder<Integer> builder = new RailwayBuilder<>();

        RailwayPattern<Integer> result = builder
                .addStep(validateNumber)
                .addStep(multiplyByTwo)
                .addStep(subtractFive)
                .execute(0);  // Starting with value 10

        if (result.isSuccess()) {
            System.out.println("Success: " + result.getValue());
        } else {
            System.err.println("Failure: " + result.getError().getMessage());
        }
    }

    private static void demoCaseResult() {
        // Define operations
        Operation<String, String, String> trimOperation = input -> {
            if (input == null) {
                return new Result.Failure<>("Input is null");
            }
            return new Result.Success<>(input.trim());
        };

        Operation<String, String, String> toUpperCaseOperation = input -> {
            if (input.isEmpty()) {
                return new Result.Failure<>("Input is empty");
            }
            return new Result.Success<>(input.toUpperCase());
        };

        OperationBuilder<String, String> builder = new OperationBuilder<>("  hello world  ");

        Result<String, String> result = builder
                .addOperation(trimOperation)
                .addOperation(toUpperCaseOperation)
                .execute();

        if (result instanceof Result.Success) {
            System.out.println("Success: " + ((Result.Success<String, String>) result).getValue());
        } else {
            System.out.println("Failure: " + ((Result.Failure<String, String>) result).getError());
        }
    }

}

