package app.quantun.rop.v1;

public abstract class Result<T, E> {
    private Result() {
    }


    public static <T, E> OperationBuilder<T, E> builder(T initialValue) {
        return new OperationBuilder<>(initialValue);
    }

    public static final class Success<T, E> extends Result<T, E> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }

    public static final class Failure<T, E> extends Result<T, E> {
        private final E error;

        public Failure(E error) {
            this.error = error;
        }

        public E getError() {
            return error;
        }
    }
}
