package botenanna.physics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** <p>The SteppedTimeLine is able to associate items of type T with a specific point in time. The SteppedTimeLine is then able to return an
 * item based on time passed. The return item will be the item associated with the last passed time step.</p>
 *
 * <p>The SteppedTimeLine can be reset to start over.</p>*/
public class SteppedTimeLine<T> implements TimeLine<T> {

    /** The TimeStep is a nested class for the SteppedTimeLine. It is used to associate a time with the item of T of the SteppedTimeLine */
    public class TimeStep {
        public final double time;
        public final T item;

        public TimeStep(double time, T item) {
            this.time = time;
            this.item = item;
        }
    }

    private TimeTracker timeTracker = new TimeTracker();
    private LinkedList<TimeStep> timeSteps = new LinkedList<>();

    /** Reset the timeline. */
    public void reset() {
        timeTracker.startTimer();
    }

    /** Add a time step which consists of an item and an associated point in time.
     * @param time point in time in seconds.
     * @param item the item which will be returned at this point in time. */
    @Override
    public void addTimeStep(double time, T item) throws IllegalArgumentException {

        TimeStep stamp = new TimeStep(time, item);

        // If no timeSteps are present, just insert this one
        if (timeSteps.size() == 0) {
            timeSteps.add(stamp);
            return;
        }

        // Find position to insert time step.
        // We assume it is placed at the end, because you usually add TimeSteps in order
        for (int i = timeSteps.size() - 1; i >= 0; i--) {
            TimeStep other = timeSteps.get(i);
            if (other.time < time) {
                timeSteps.add(i + 1, stamp);
                return;
            } else if (i == 0) {
                timeSteps.add(0, stamp);
                return;
            }
        }
    }

    /** <p>Evaluate the item associated with the elapsed time since the SteppedTimeLine was reset using the internal TimeTracker.
     * This method will round down to nearest defined item. </p>
     * <p>Before and after the defined item's times, the first and last item will be returned. </p>
     * @return the item associated with the current time. */
    public T evaluate() {
        return evaluate(timeTracker.getElapsedSecondsTimer());
    }

    /** <p>Evaluate the item associated with the elapsed time since the SteppedTimeLine was reset using the internal TimeTracker.
     * This method will round up to nearest defined item. </p>
     * @return the item associated with the current time. */
    public T evaluateUp() {
        return evaluateUp(timeTracker.getElapsedSecondsTimer());
    }

    /** <p>Evaluate the TimeStep associated with a given time. This method will round
     * down to nearest defined TimeStep. </p>
     * <p>Before and after the defined item's times, the first and last item will be returned. </p>
     * <p>To get the TimeStep after a specific time, see {@code evaluateUpTrue()}.</p>
     * @param time the elapsed time in seconds.
     * @return the TimeStep associated with the given time. */
    public TimeStep evaluateTrue(double time) throws IllegalArgumentException {

        // Make sure it possible to evaluate
        if (timeSteps.size() == 0) {
            throw new NullPointerException("SteppedTimeLine is empty.");
        }

        // Find TimeStep at time rounded down to nearest TimeStep time
        TimeStep active = timeSteps.get(0);
        for (int i = 0; i < timeSteps.size(); i++) {
            if (timeSteps.get(i).time <= time) {
                active = timeSteps.get(i);
            } else {
                // Break when reaching unreached TimeSteps
                break;
            }
        }

        return active;
    }

    /** <p>Evaluate the item associated with given time without using the internal TimeTracker. This method will round
     * down to nearest defined item. </p>
     * <p>Before and after the defined item's times, the first and last item will be returned. </p>
     * <p>To get the element after a specific time, see {@code evaluateUp()}.</p>
     * @param time the elapsed time in seconds.
     * @return the item associated with the given time. */
    @Override
    public T evaluate(double time) throws IllegalArgumentException {
        return evaluateTrue(time).item;
    }

    /** <p>Evaluate the TimeStep associated with given time. This method will round up to nearest defined TimeStep.
     * When given a time that matches a defined TimeStep, it will return the following TimeStep, if possible. </p>
     * <p>To get the TimeStep before a specific time, see {@code evaluateTrue()}.</p>
     * @param time the elapsed time in seconds.
     * @return the TimeStep associated with the given time. */
    public TimeStep evaluateUpTrue(double time) {

        // Make sure it possible to evaluate
        if (timeSteps.size() == 0) {
            throw new NullPointerException("SteppedTimeLine is empty.");
        }

        // Find TimeStep at time rounded up to nearest TimeStep time
        for (int i = 0; i < timeSteps.size(); i++) {
            if (timeSteps.get(i).time > time) {
                // This is the first TimeStep with a greater time
                return timeSteps.get(i);
            }
        }

        return timeSteps.getLast();
    }

    /** <p>Evaluate the item associated with given time without using the internal TimeTracker. This method will round
     * up to nearest defined item. When given a time that matches a defined item, it will return the following item,
     * if possible. </p>
     * <p>To get the element before a specific time, see {@code evaluate()}.</p>
     * @param time the elapsed time in seconds.
     * @return the item associated with the given time. */
    public T evaluateUp(double time) throws IllegalArgumentException {
        return evaluateUpTrue(time).item;
    }

    /** Retrieve a List of all step times in chronological order. */
    @Override
    public List<Double> getTimes() {
        ArrayList<Double> list = new ArrayList<>(timeSteps.size());
        for (TimeStep timeStep : timeSteps) {
            list.add(timeStep.time);
        }
        return list;
    }

    /** Retrieve a List of all items in chronological order. */
    @Override
    public List<T> getItems() {
        ArrayList<T> list = new ArrayList<>(timeSteps.size());
        for (TimeStep timeStep : timeSteps) {
            list.add(timeStep.item);
        }
        return list;
    }

    /** Returns the first point in time of which an element is defined. */
    @Override
    public double getFirstTime() {
        return timeSteps.getFirst().time;
    }

    /** Returns the item returned at the first step in the SteppedTimeLine. This is also the element return at infinity. */
    @Override
    public T getFirstItem() {
        return timeSteps.getFirst().item;
    }

    /** Returns the last point in time of which an element is defined. This is equal to the SteppedTimeLines length */
    @Override
    public double getLastTime() {
        return timeSteps.getLast().time;
    }

    /** Returns the item returned at the last step in the SteppedTimeLine. This is also the item return at infinity. */
    @Override
    public T getLastItem() {
        return timeSteps.getLast().item;
    }

    @Override
    public boolean isEmpty() {
        return timeSteps.isEmpty();
    }

    void analyze() {
        for (TimeStep timeStep : timeSteps) {
            System.out.println("t: " + timeStep.time + ", i: " + timeStep.item);
        }
    }

    LinkedList<TimeStep> getTimeSteps() {
        return timeSteps;
    }
}
