package app.quantun.rop.v1;

@FunctionalInterface
public interface Operation<T, R, E> {
    Result<R, E> apply(T input);

}




