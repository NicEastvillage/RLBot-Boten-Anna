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
     * <p>If no item is defined at time = 0, the first defined time's item will be returned until the first item is
     * reached. Similarly, after the last defined item's time, the last item will be returned. </p>*/
    public InterpolatedTimeLine(LerpFunction<T> lerpFunction) {
        this.lerpFunction = lerpFunction;
        this.steps = new SteppedTimeLine<>();
    }

    @Override
    public void addTimeStep(double time, T item) {
        steps.addTimeStep(time, item);
    }

    @Override
    public T evaluate(double time) throws IllegalArgumentException, NullPointerException {
        if (isEmpty()) throw new NullPointerException("TimeLine is empty.");
        if (time < 0) throw new IllegalArgumentException("The argument time must be 0 or greater.");

        SteppedTimeLine<T>.TimeStep first = steps.evaluateTrue(time);
        SteppedTimeLine<T>.TimeStep second = steps.evaluateUpTrue(time);

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
