package tarehart.rlbot.input;

/**
 * All values are in radians per second.
 */
public class CarSpin {
    /**
     * Positive means tilting upward
     */
    public double pitchRate;

    /**
     * Positive means turning to the right
     */
    public double yawRate;

    /**
     * Positive means rolling to the right
     */
    public double rollRate;

    public CarSpin(double pitchRate, double yawRate, double rollRate) {
        this.pitchRate = pitchRate;
        this.yawRate = yawRate;
        this.rollRate = rollRate;
    }
}
