package botenanna.physics;

import botenanna.math.RLMath;

import java.util.List;

public class InterpolatedTimeLine<T> implements TimeLine<T> {

    private LerpFunction<T> lerpFunction;
    private SteppedTimeLine<T> steps;

    /** <p>The InterpolatedTimeLine is able interpolate between a sequence of items of type T. Each item myst be
     * associated with a specific point in time. The InterpolatedTimeLine is then able to return an item based on the
     * two nearest defined items using interpolation (rounding up and down to nearest items' times). An interpolation
     * function must be provided on creation.</p>
     *
     * <p>When requesting an item, if the given time is before the first item's time, the first item will be returned.
     * Similarly, after the last defined item's time, the last item will be returned.</p> */
    public InterpolatedTimeLine(LerpFunction<T> lerpFunction) {
        this.lerpFunction = lerpFunction;
        this.steps = new SteppedTimeLine<>();
    }

    /** Add a time step which consists of an item and an associated point in time.
     * @param time point in time in seconds.
     * @param item the item which will be returned at this point in time. */
    @Override
    public void addTimeStep(double time, T item) {
        steps.addTimeStep(time, item);
    }

    /** <p>Evaluate the InterpolatedTimeLine at {@code time} which will find nearest defined TimeStep before and after
     * {@code time} and Interpolate between them with the LerpFunction provided at creation.</p>
     * <p>When requesting an item, if the given time is before the first item's time, the first item will be returned.
     * Similarly, after the last defined item's time, the last item will be returned. </p>*/
    @Override
    public T evaluate(double time) throws NullPointerException {
        if (isEmpty()) throw new NullPointerException("TimeLine is empty.");

        // Find steps before and after given time
        SteppedTimeLine<T>.TimeStep first = steps.evaluateTrue(time);
        SteppedTimeLine<T>.TimeStep second = steps.evaluateUpTrue(time);

        // Calculate the relative time between first and second step. Then lerp with that time
        double relativeTime = RLMath.invLerp(first.time, second.time, time);
        return lerpFunction.lerp(first.item, second.item, relativeTime);
    }

    @Override
    public List<Double> getTimes() {
        return steps.getTimes();
    }

    @Override
    public List<T> getItems() {
        return steps.getItems();
    }

    @Override
    public double getFirstTime() {
        return steps.getFirstTime();
    }

    @Override
    public double getLastTime() {
        return steps.getLastTime();
    }

    @Override
    public T getFirstItem() {
        return steps.getFirstItem();
    }

    @Override
    public T getLastItem() {
        return steps.getLastItem();
    }

    @Override
    public boolean isEmpty() {
        return steps.isEmpty();
    }
}
