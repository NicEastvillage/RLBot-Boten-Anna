package botenanna.physics;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

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

    /** Add a time stamp which consists of an item and an associated point in time. */
    public void addTimeStamp(double time, T item) throws IllegalArgumentException {
        // Check arguments
        if (time < 0) {
            throw new IllegalArgumentException();
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

    /** <p>Evaluate the item associated with the elapsed time since the TimeLine was reset.</p>
     * <p>If no item is defined at time = 0, the first defined time's item will be returned until the second item is reached.</p>*/
    public T evaluate() {
        // Make sure it possible to evaluate
        if (timeStamps.size() == 0) {
            throw new NullPointerException();
        }

        // Find TimeStamp with last time
        double elapsedTime = timeTracker.getElapsedSecondsTimer();
        TimeStamp active = timeStamps.get(0);
        for (int i = 0; i < timeStamps.size(); i++) {
            if (timeStamps.get(i).time < elapsedTime) {
                active = timeStamps.get(i);
            } else {
                // Break when reaching unreached TimeStamps
                break;
            }
        }

        return active.item;
    }
}
