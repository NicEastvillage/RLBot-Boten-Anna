package tarehart.rlbot;

import rlbot.api.GameData;

/**
 * A data class describing the outputs of an agent. This class can be translated into a ControllerState.
 */
public class AgentOutput {

    // 0 is straight, -1 is hard left, 1 is hard right.
    private double steeringTilt = 0;

    // -1 for front flip, 1 for back flip
    private double pitchTilt = 0;

    // 0 is none, 1 is full
    private double acceleration = 0;
    private double deceleration = 0;

    private boolean jumpDepressed = false;
    private boolean boostDepressed = false;
    private boolean slideDepressed = false;

    public AgentOutput() {
    }

    /** Set steering/turning. 0 is straight, -1 is hard left, 1 is hard right. Clamped between -1 and 1. Default it 0. */
    public AgentOutput withSteer(double steeringTilt) {
        this.steeringTilt = Math.max(-1, Math.min(1, steeringTilt));
        return this;
    }

    /** Set pitch. -1 for front flip, 1 for back flip. Clamped between -1 and 1. Default is 0. */
    public AgentOutput withPitch(double pitchTilt) {
        this.pitchTilt = Math.max(-1, Math.min(1, pitchTilt));
        return this;
    }

    /** Set acceleration. 0 is none, 1 is full. Clamped between 0 and 1. Default is 0. */
    public AgentOutput withAcceleration(double acceleration) {
        this.acceleration = Math.max(0, Math.min(1, acceleration));
        return this;
    }

    /** Set deceleration. 0 is none, 1 is full. Clamped between 0 and 1. Default is 0. */
    public AgentOutput withDeceleration(double deceleration) {
        this.deceleration = Math.max(0, Math.min(1, deceleration));
        return this;
    }

    /** Set jump pressed state. Default is false. */
    public AgentOutput withJump(boolean jumpDepressed) {
        this.jumpDepressed = jumpDepressed;
        return this;
    }

    /** Set boost pressed state. Default is false. */
    public AgentOutput withBoost(boolean boostDepressed) {
        this.boostDepressed = boostDepressed;
        return this;
    }

    /** Set slide pressed state. Default is false. */
    public AgentOutput withSlide(boolean slideDepressed) {
        this.slideDepressed = slideDepressed;
        return this;
    }

    /** Set jump pressed state to true. */
    public AgentOutput withJump() {
        this.jumpDepressed = true;
        return this;
    }

    /** Set boost pressed state to true. */
    public AgentOutput withBoost() {
        this.boostDepressed = true;
        return this;
    }

    /** Set slide pressed state to true. */
    public AgentOutput withSlide() {
        this.slideDepressed = true;
        return this;
    }


    /**
     * Compare to AgentOutputs.
     * @param o the other AgentOutput.
     * @return whether the AgentOutputs are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentOutput that = (AgentOutput) o;

        if (Double.compare(that.steeringTilt, steeringTilt) != 0) return false;
        if (Double.compare(that.pitchTilt, pitchTilt) != 0) return false;
        if (Double.compare(that.acceleration, acceleration) != 0) return false;
        if (Double.compare(that.deceleration, deceleration) != 0) return false;
        if (jumpDepressed != that.jumpDepressed) return false;
        if (boostDepressed != that.boostDepressed) return false;
        return slideDepressed == that.slideDepressed;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(steeringTilt);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pitchTilt);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(acceleration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(deceleration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (jumpDepressed ? 1 : 0);
        result = 31 * result + (boostDepressed ? 1 : 0);
        result = 31 * result + (slideDepressed ? 1 : 0);
        return result;
    }

    /**
     * @return the amount of steering. Between -1 (left) and 1 (right).
     */
    public double getSteer() {
        return steeringTilt;
    }

    /**
     * @return the amount of pitch. Between -1 (forwards) and 1 (backwards).
     */
    public double getPitch() {
        return pitchTilt;
    }


    /**
     * @return this AgentOutput as a ControllerState.
     */
    GameData.ControllerState toControllerState() {
        return GameData.ControllerState.newBuilder()
                .setThrottle((float) (acceleration - deceleration))
                .setSteer((float) steeringTilt)
                .setYaw(slideDepressed ? 0 : (float) steeringTilt)
                .setRoll(slideDepressed ? (float) steeringTilt : 0)
                .setPitch((float) pitchTilt)
                .setBoost(boostDepressed)
                .setHandbrake(slideDepressed)
                .setJump(jumpDepressed)
                .build();
    }
}
