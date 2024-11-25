package app.quantun.rop.v2;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class RailwayBuilder<T> {
    private List<Function<T, RailwayPattern<T>>> steps;

    public RailwayBuilder() {
        this.steps = new LinkedList<>();
    }

    // Add a step to the builder
    public RailwayBuilder<T> addStep(Function<T, RailwayPattern<T>> step) {
        this.steps.add(step);
        return this;
    }

    // Build and execute the pipeline
    public RailwayPattern<T> execute(T input) {
        RailwayPattern<T> result = RailwayPattern.success(input);

        for (Function<T, RailwayPattern<T>> step : steps)
        {
            if (result.isSuccess())
            {
                result = result.flatMap(step);
            }
            else {
                return result;  // Short-circuit on error
            }
        }
        return result;
    }
}

