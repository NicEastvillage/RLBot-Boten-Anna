package tarehart.rlbot.planning;

import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.math.SpaceTime;

import java.time.LocalDateTime;

public class Intercept {
    private Vector3 space;
    private LocalDateTime time;
    private double airBoost;
    private StrikeProfile strikeProfile;

    public Intercept(Vector3 space, LocalDateTime time, double airBoost, StrikeProfile strikeProfile) {
        this.space = space;
        this.time = time;
        this.airBoost = airBoost;
        this.strikeProfile = strikeProfile;
    }

    public Intercept(SpaceTime spaceTime, StrikeProfile strikeProfile) {
        this(spaceTime.space, spaceTime.time, 0, strikeProfile);
    }

    public double getAirBoost() {
        return airBoost;
    }

    public Vector3 getSpace() {
        return space;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public SpaceTime toSpaceTime() {
        return new SpaceTime(space, time);
    }

    public StrikeProfile getStrikeProfile() {
        return strikeProfile;
    }
}
