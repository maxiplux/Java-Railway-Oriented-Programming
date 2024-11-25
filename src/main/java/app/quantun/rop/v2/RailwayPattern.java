package app.quantun.rop.v2;

import java.util.function.Function;

public class RailwayPattern<T> {
    private final T value;
    private final Throwable error;

    private RailwayPattern(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    public static <T> RailwayPattern<T> success(T value) {
        return new RailwayPattern<>(value, null);
    }

    public static <T> RailwayPattern<T> failure(Throwable error) {
        return new RailwayPattern<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getValue() {
        return value;
    }

    public Throwable getError() {
        return error;
    }

    // FlatMap for composability
    public <R> RailwayPattern<R> flatMap(Function<T, RailwayPattern<R>> mapper) {
        if (isSuccess())
        {
            try {
                return mapper.apply(value);
            } catch (Throwable e) {
                return failure(e);
            }
        }
        return failure(error);

    }
}

