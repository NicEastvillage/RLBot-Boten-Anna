package botenanna.physics;

import java.util.LinkedList;

/** <p>The TimeLine is able to associate items of type T with a specific time stamp. The TimeLine is then able to return an
 * item based on time passed. The return item will be the item associated with the last passed time stamp.</p>
 *
 * <p>The TimeLine can be reset to start over.</p>
 *
 * <p>If no item is defined at time = 0, the first defined time's item will be returned until the second item is reached.</p>*/
public class TimeLine<T> {

    /** The TimeStamp is a nested class for the TimeLine. It is used to associate a time with the item of T of the TimeLine */
    private class TimeStamp {
        public final double time;
        public final T item;

        public TimeStamp(double time, T item) {
            this.time = time;
            this.item = item;
        }
    }

    private TimeTracker timeTracker = new TimeTracker();
    private LinkedList<TimeStamp> timeStamps = new LinkedList<>();

    /** Reset the timeline. */
    public void reset() {
        timeTracker.startTimer();
    }

    /** Add a time stamp which consists of an item and an associated point in time.
     * @param time point in time in seconds. Must be zero or greater.
     * @param item the item which will be returned at this point in time. */
    public void addTimeStamp(double time, T item) throws IllegalArgumentException {
        // Check arguments
        if (time < 0) {
            throw new IllegalArgumentException("Time must be zero or greater.");
        }

        TimeStamp stamp = new TimeStamp(time, item);

        // If no timeStamps are present, just insert this one
        if (timeStamps.size() == 0) {
            timeStamps.add(stamp);
            return;
        }

        // Find position to insert time stamp.
        // We assume it is placed at the end, because you usually add TimeStamps in order
        for (int i = timeStamps.size() - 1; i >= 0; i++) {
            TimeStamp other = timeStamps.get(i);
            if (other.time < time) {
                timeStamps.add(i + 1, stamp);
                return;
            } else if (i == 0) {
                timeStamps.add(0, stamp);
                return;
            }
        }
    }

    /** <p>Evaluate the item associated with the elapsed time since the TimeLine was reset using the internal TimeTracker.
     * This method will round down to nearest defined item. </p>
     * <p>If no item is defined at time = 0, the first defined time's item will be returned until the second item is reached.</p>
     * @return the item associated with the current time. */
    public T evaluate() {
        return evaluate(timeTracker.getElapsedSecondsTimer());
    }

    /** <p>Evaluate the item associated with the elapsed time since the TimeLine was reset using the internal TimeTracker.
     * This method will round up to nearest defined item. </p>
     * @return the item associated with the current time. */
    public T evaluateUp() {
        return evaluateUp(timeTracker.getElapsedSecondsTimer());
    }

    /** <p>Evaluate the item associated with given time without using the internal TimeTracker. This method will round
     * down to nearest defined item. </p>
     * <p>If no item is defined at time = 0, the first defined time's item will be returned until the second item is reached.</p>
     * <p>To get the element after a specific time, see {@code evaluateUp()}.</p>
     * @param seconds the elapsed time in seconds. Must be zero or greater.
     * @return the item associated with the given time. */
    public T evaluate(double seconds) throws IllegalArgumentException {
        if (seconds < 0) throw new IllegalArgumentException("Seconds must be zero or greater.");

        // Make sure it possible to evaluate
        if (timeStamps.size() == 0) {
            throw new NullPointerException("TimeLine is empty.");
        }

        // Find TimeStamp at time rounded down to nearest TimeStamp time
        TimeStamp active = timeStamps.get(0);
        for (int i = 0; i < timeStamps.size(); i++) {
            if (timeStamps.get(i).time <= seconds) {
                active = timeStamps.get(i);
            } else {
                // Break when reaching unreached TimeStamps
                break;
            }
        }

        return active.item;
    }

    /** <p>Evaluate the item associated with given time without using the internal TimeTracker. This method will round
     * up to nearest defined item. When given a time that matches a defined item, it will return the following item,
     * if possible. </p>
     * <p>To get the element before a specific time, see {@code evaluate()}.</p>
     * @param seconds the elapsed time in seconds. Must be zero or greater.
     * @return the item associated with the given time. */
    public T evaluateUp(double seconds) throws IllegalArgumentException {
        if (seconds < 0) throw new IllegalArgumentException("Seconds must be zero or greater.");

        // Make sure it possible to evaluate
        if (timeStamps.size() == 0) {
            throw new NullPointerException("TimeLine is empty.");
        }

        // Find TimeStamp at time rounded up to nearest TimeStamp time
        for (int i = 0; i < timeStamps.size(); i++) {
            if (timeStamps.get(i).time > seconds) {
                // This is the first TimeStamp with a greater time
                return timeStamps.get(i).item;
            }
        }

        return timeStamps.getLast().item;
    }
}
