package app.quantun.rop.v1;

import java.util.LinkedList;
import java.util.List;


public class OperationBuilder<T, E> {
    private T input;
    private final List<Operation<T, T, E>> operations = new LinkedList<>();

    public OperationBuilder(T input) {
        this.input = input;
    }

    public OperationBuilder<T, E> addOperation(Operation<T, T, E> operation) {
        operations.add(operation);
        return this;
    }




    public Result<T, E> execute() {
        T current = input;
        for (Operation<T, T, E> operation : operations) {
            Result<T, E> result = operation.apply(current);
            if (result instanceof Result.Failure) {
                return result;
            }
            current = ((Result.Success<T, E>) result).getValue();
        }
        return new Result.Success<>(current);
    }


}
