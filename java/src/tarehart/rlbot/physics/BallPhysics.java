package tarehart.rlbot.physics;

import tarehart.rlbot.math.SpaceTimeVelocity;

public class BallPhysics {
    public static double getGroundBounceEnergy(SpaceTimeVelocity stv) {
        double potentialEnergy = (stv.getSpace().z - ArenaModel.BALL_RADIUS) * ArenaModel.GRAVITY;
        double verticalKineticEnergy = 0.5 * stv.getVelocity().z * stv.getVelocity().z;
        return potentialEnergy + verticalKineticEnergy;
    }
}
