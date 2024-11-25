package app.quantun.rop.v3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class RailwayHandler<T> {
    // Internal data structure to hold the value or error
    private final T value;
    private final Throwable error;

    // List of steps to build the pipeline
    private List<Function<T, RailwayHandler<T>>> steps = new LinkedList<>();

    // Private constructor for internal success/failure creation
    private RailwayHandler(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    // Example of how to create a new builder that can be initialized
    public static <T> RailwayHandler<T> start(T value) {
        return RailwayHandler.success(value);
    }
    // Static methods for success and failure creation
    public static <T> RailwayHandler<T> success(T value) {
        return new RailwayHandler<>(value, null);
    }

    public static <T> RailwayHandler<T> failure(Throwable error) {
        return new RailwayHandler<>(null, error);
    }

    // Check whether the operation was successful
    public boolean isSuccess() {
        return error == null;
    }

    public T getValue() {
        return value;
    }

    public Throwable getError() {
        return error;
    }

    // Add step to the pipeline
    public RailwayHandler<T> addStep(Function<T, RailwayHandler<T>> step) {
        this.steps.add(step);
        return this;
    }

    // Execute all steps in the pipeline
    public RailwayHandler<T> execute() {
        RailwayHandler<T> current = this;
        for (Function<T, RailwayHandler<T>> step : steps) {
            if (current.isSuccess()) {
                try {
                    current = step.apply(current.getValue());
                } catch (Throwable e) {
                    return failure(e);
                }
            } else {
                return current; // Short-circuit on error
            }
        }
        return current;
    }


    // Function to add more transformations in a composable manner (remains of type A)
    public <R> RailwayHandler<R> flatMap(Function<T, RailwayHandler<R>> mapper) {
        if (isSuccess()) {
            try {
                return mapper.apply(value);
            } catch (Throwable e) {
                return RailwayHandler.failure(e);
            }
        } else {
            return RailwayHandler.failure(error);
        }
    }

    // Function for mapping the type A to a different type B without affecting the railway flow
    public <B> RailwayHandler<B> map(Function<T, B> mapper) {
        if (isSuccess()) {
            try {
                return RailwayHandler.success(mapper.apply(value));
            } catch (Throwable e) {
                return RailwayHandler.failure(e);
            }
        } else {
            return RailwayHandler.failure(error);
        }
    }


}

