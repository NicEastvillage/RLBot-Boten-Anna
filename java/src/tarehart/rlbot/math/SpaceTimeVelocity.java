package tarehart.rlbot.math;

import tarehart.rlbot.math.vector.Vector3;

import java.time.LocalDateTime;

public class SpaceTimeVelocity {
    public Vector3 space;
    public LocalDateTime time;
    public Vector3 velocity;

    public SpaceTimeVelocity(Vector3 space, LocalDateTime time, Vector3 velocity) {
        this.space = space;
        this.time = time;
        this.velocity = velocity;
    }

    public SpaceTimeVelocity(SpaceTime spaceTime, Vector3 velocity) {
        this(spaceTime.space, spaceTime.time, velocity);
    }

    public Vector3 getSpace() {
        return space;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public SpaceTimeVelocity copy() {
        return new SpaceTimeVelocity(this.getSpace(), this.getTime(), this.getVelocity());
    }

    public SpaceTime toSpaceTime() {
        return new SpaceTime(space, time);
    }
}
