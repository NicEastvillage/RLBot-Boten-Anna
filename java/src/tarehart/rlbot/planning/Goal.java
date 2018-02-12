package tarehart.rlbot.planning;

import tarehart.rlbot.math.Plane;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.physics.ArenaModel;


public class Goal {

    private static final double GOAL_DISTANCE = 102;
    public static final double GOAL_HEIGHT = 12;

    public static final double EXTENT = 17.8555;
    private Vector3 center;
    private Plane threatPlane;
    private Plane scorePlane;

    public Goal(boolean negativeSide) {

        center = new Vector3(0, GOAL_DISTANCE * (negativeSide ? -1 : 1), 0);

        threatPlane = new Plane(new Vector3(0, negativeSide ? 1 : -1, 0), new Vector3(0, (GOAL_DISTANCE - 1) * (negativeSide ? -1 : 1), 0));
        scorePlane = new Plane(new Vector3(0, negativeSide ? 1 : -1, 0), new Vector3(0, (GOAL_DISTANCE + 2) * (negativeSide ? -1 : 1), 0));
    }


    public Vector3 getNearestEntrance(Vector3 ballPosition, double padding) {

        double adjustedExtent = EXTENT - ArenaModel.BALL_RADIUS - padding;
        double adjustedHeight = GOAL_HEIGHT - ArenaModel.BALL_RADIUS - padding;
        double x = Math.min(adjustedExtent, Math.max(-adjustedExtent, ballPosition.x));
        double z = Math.min(adjustedHeight, Math.max(ArenaModel.BALL_RADIUS, ballPosition.z));
        return new Vector3(x, center.y, z);
    }

    public Plane getThreatPlane() {
        return threatPlane;
    }

    public Plane getScorePlane() {
        return scorePlane;
    }

    public Vector3 getCenter() {
        return center;
    }

    /**
     * From shooter's perspective
     */
    public Vector3 getLeftPost() {
        return getLeftPost(0);
    }

    /**
     * From shooter's perspective
     */
    public Vector3 getLeftPost(double padding) {
        return new Vector3(center.x - (EXTENT - padding) * Math.signum(center.y), center.y, center.z);
    }

    /**
     * From shooter's perspective
     */
    public Vector3 getRightPost() {
        return getRightPost(0);
    }

    /**
     * From shooter's perspective
     */
    public Vector3 getRightPost(double padding) {
        return new Vector3(center.x + (EXTENT - padding) * Math.signum(center.y), center.y, center.z);
    }

    public boolean isInBox(Vector3 position) {
        if (getCenter().y * position.y < 0) {
            return false; // Wrong side of field
        }

        if (Math.abs(position.y) < 70) {
            return false; // Too much toward center
        }

        if (Math.abs(position.x) > 50) {
            return false; // Too much to the side
        }

        return true;
    }
}
