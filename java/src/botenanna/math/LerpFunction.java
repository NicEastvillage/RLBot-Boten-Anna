package botenanna.math;

/** A LerpFuncion is a function that interpolates between two objects {@code a} and {@code b} with a {@code t}, such
 * that {@code t = 0} will return {@code a} and {@code t = 1} will return {@code b}. */
@FunctionalInterface
public interface LerpFunction<T> {
    T lerp(T a, T b, double t);
}
