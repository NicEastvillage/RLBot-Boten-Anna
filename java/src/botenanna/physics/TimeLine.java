package botenanna.physics;

import java.util.List;

/** <p>A TimeLine is able to associate items of type T with a specific point in time. Like a Collection it can return
 * an item, but to do that, you have to specify a double representing a point in time. TimeLines work great in
 * conjunctions with {@link TimeTracker}. See {@link SteppedTimeLine} and {@link InterpolatedTimeLine}. */
public interface TimeLine<T> {

    /** Adds an item to a time. */
    void addTimeStep(double time, T item);

    /** Returns the item associated with a specific time. */
    T evaluate(double time) throws NullPointerException;

    /** Returns a List of all defined times. */
    List<Double> getTimes();
    /** Return a List of all defined items. */
    List<T> getItems();

    /** Return the first defined time. */
    double getFirstTime();

    /** Return the last defined time. This is also the length of the TimeLine. */
    double getLastTime();

    /** Return the first defined item. */
    T getFirstItem();

    /** Return the last defined item. */
    T getLastItem();

    /** Returns whether any items are defined. */
    boolean isEmpty();
}
