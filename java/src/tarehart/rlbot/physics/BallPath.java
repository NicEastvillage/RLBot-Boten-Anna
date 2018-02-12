package tarehart.rlbot.physics;

import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.math.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BallPath {

    ArrayList<SpaceTimeVelocity> path = new ArrayList<>();

    public BallPath(SpaceTimeVelocity start) {
        path.add(start);
    }

    public void addSlice(SpaceTimeVelocity spaceTime) {
        path.add(spaceTime);
    }

    public List<SpaceTimeVelocity> getSlices() {
        return path;
    }

    public Optional<SpaceTimeVelocity> getMotionAt(LocalDateTime time) {
        if (time.isBefore(path.get(0).getTime()) || time.isAfter(path.get(path.size() - 1).getTime())) {
            return Optional.empty();
        }

        for (int i = 0; i < path.size() - 1; i++) {
            SpaceTimeVelocity current = path.get(i);
            SpaceTimeVelocity next = path.get(i + 1);
            if (next.getTime().isAfter(time)) {

                long simulationStepMillis = Duration.between(current.getTime(), next.getTime()).toMillis();
                double tweenPoint = Duration.between(current.getTime(), time).toMillis() * 1.0 / simulationStepMillis;
                Vector3 toNext = next.getSpace().minus(current.getSpace());
                Vector3 toTween = toNext.scaled(tweenPoint);
                Vector3 space = current.getSpace().plus(toTween);
                Vector3 velocity = averageVectors(current.getVelocity(), next.getVelocity(), 1 - tweenPoint);
                return Optional.of(new SpaceTimeVelocity(new SpaceTime(space, time), velocity));
            }
        }

        return Optional.of(getEndpoint());
    }

    private Vector3 averageVectors(Vector3 a, Vector3 b, double weightOfA) {
        return a.scaled(weightOfA).plus(b.scaled(1 - weightOfA));
    }

    /**
     * Bounce counting starts at 1.
     *
     * 0 is not a valid input.
     */
    public Optional<SpaceTimeVelocity> getMotionAfterWallBounce(int targetBounce) {

        assert targetBounce > 0;

        Vector3 previousVelocity = null;
        int numBounces = 0;

        for (int i = 1; i < path.size(); i++) {
            SpaceTimeVelocity spt = path.get(i);
            SpaceTimeVelocity previous = path.get(i - 1);

            if (isWallBounce(previous.getVelocity(), spt.getVelocity())) {
                numBounces++;
            }

            if (numBounces == targetBounce) {
                if (path.size() == i + 1) {
                    return Optional.empty();
                }
                return Optional.of(spt.copy());
            }
        }

        return Optional.empty();
    }

    private boolean isWallBounce(Vector3 previousVelocity, Vector3 currentVelocity) {
        if (currentVelocity.magnitudeSquared() < .01) {
            return false;
        }
        Vector2 prev = previousVelocity.flatten();
        Vector2 curr = currentVelocity.flatten();

        return Vector2.angle(prev, curr) > Math.PI / 6;
    }

    private boolean isFloorBounce(Vector3 previousVelocity, Vector3 currentVelocity) {
        return previousVelocity.z < 0 && currentVelocity.z > 0;
    }

    public SpaceTimeVelocity getStartPoint() {
        return path.get(0).copy();
    }

    public SpaceTimeVelocity getEndpoint() {
        return path.get(path.size() - 1).copy();
    }

    public Optional<SpaceTimeVelocity> getLanding(LocalDateTime startOfSearch) {

        for (int i = 1; i < path.size(); i++) {
            SpaceTimeVelocity spt = path.get(i);

            if (spt.getTime().isBefore(startOfSearch)) {
                continue;
            }

            SpaceTimeVelocity previous = path.get(i - 1);


            if (isFloorBounce(previous.getVelocity(), spt.getVelocity())) {
                if (path.size() == i + 1) {
                    return Optional.empty();
                }

                double floorGapOfPrev = previous.getSpace().z - ArenaModel.BALL_RADIUS;
                double floorGapOfCurrent = spt.getSpace().z - ArenaModel.BALL_RADIUS;

                SpaceTimeVelocity bouncePosition = new SpaceTimeVelocity(new Vector3(spt.getSpace().x, spt.getSpace().y, ArenaModel.BALL_RADIUS), spt.getTime(), spt.getVelocity());
                if (floorGapOfPrev < floorGapOfCurrent) {
                    // TODO: consider interpolating instead of just picking the more accurate.
                    bouncePosition = new SpaceTimeVelocity(
                            new Vector3(previous.getSpace().x, previous.getSpace().y, ArenaModel.BALL_RADIUS),
                            previous.getTime(),
                            spt.getVelocity());
                }

                return Optional.of(bouncePosition);
            }
        }

        return Optional.empty();
    }

    public Optional<SpaceTimeVelocity> getPlaneBreak(LocalDateTime searchStart, Plane plane, boolean directionSensitive) {
        for (int i = 1; i < path.size(); i++) {
            SpaceTimeVelocity spt = path.get(i);

            if (spt.getTime().isBefore(searchStart)) {
                continue;
            }

            SpaceTimeVelocity previous = path.get(i - 1);

            if (directionSensitive && spt.getSpace().minus(previous.getSpace()).dotProduct(plane.normal) > 0) {
                // Moving the same direction as the plane normal. If we're direction sensitive, then we don't care about plane breaks in this direction.
                continue;
            }

            Optional<Vector3> planeBreak = getPlaneBreak(previous.getSpace(), spt.getSpace(), plane);

            if (planeBreak.isPresent()) {

                Vector3 breakPosition = planeBreak.get();

                double stepSeconds = TimeUtil.secondsBetween(previous.getTime(), spt.getTime());
                double tweenPoint = previous.getSpace().distance(breakPosition) / previous.getSpace().distance(spt.getSpace());
                LocalDateTime moment = previous.getTime().plus(TimeUtil.toDuration(stepSeconds * tweenPoint));
                Vector3 velocity = averageVectors(previous.getVelocity(), spt.getVelocity(), 1 - tweenPoint);
                return Optional.of(new SpaceTimeVelocity(breakPosition, moment, velocity));
            }
        }

        return Optional.empty();
    }

    private Optional<Vector3> getPlaneBreak(Vector3 start, Vector3 end, Plane plane) {
        return VectorUtil.getPlaneIntersection(plane, start, end.minus(start));
    }

    public Optional<SpaceTimeVelocity> findSlice(Predicate<SpaceTimeVelocity> decider) {
        for (int i = 1; i < path.size(); i++) {
            if (decider.test(path.get(i))) {
                return Optional.of(path.get(i));
            }
        }
        return Optional.empty();
    }

    public Optional<SpaceTimeVelocity> findSlice(Predicate<SpaceTimeVelocity> decider, LocalDateTime timeLimit) {
        for (int i = 1; i < path.size(); i++) {
            SpaceTimeVelocity slice = path.get(i);
            if (slice.getTime().isAfter(timeLimit)) {
                return Optional.empty();
            }
            if (decider.test(slice)) {
                return Optional.of(slice);
            }
        }
        return Optional.empty();
    }
}
