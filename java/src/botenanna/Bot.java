package botenanna;

import botenanna.math.Vector2;
import botenanna.math.Vector3;
import rlbot.api.GameData;

public class Bot {

    public enum Team {
        BLUE, ORANGE
    }

    private final Team team;
    private final int playerIndex;

    public Bot(int playerIndex, int teamIndex) {
        this.playerIndex = playerIndex;
        team = (teamIndex == 0 ? Team.BLUE : Team.ORANGE);
    }

    /** Let the bot process the information from the data packet
     * @param packet the game tick packet from the game
     * @return an AgentOutput of what the agent want to do */
    public AgentOutput process(GameData.GameTickPacket packet) {

        // TODO Go towards the ball for now
        return goTowardsPoint(packet, Vector3.convert(packet.getBall().getLocation()).asVector2());
    }

    /**
     * @param packet the game tick packet from the game
     * @param point the location the agent should go towards
     * @return an AgentOutput of what the agent needs to do to get to the point */
    private AgentOutput goTowardsPoint(GameData.GameTickPacket packet, Vector2 point) {

        // TODO For now we always to full throttle forwards, though that not be the shortest route. Maybe we should slide in some cases?
        // TODO Also, the bot will overshoot. In some cases we want the bot to stop, or get to point at a specific time (e.g. when ball lands)

        // Get the need positions and rotations
        GameData.PlayerInfo me = packet.getPlayers(playerIndex);
        Vector3 myPos = Vector3.convert(me.getLocation());
        Vector3 myRotation = Vector3.convert(me.getRotation());

        // Find the difference between the agents location and the point
        Vector2 diff = point.minus(myPos.asVector2());

        // Calculate the angle between the cars front and direction to the point
        double atan = Math.atan2(diff.y, diff.x);
        double angDiff = atan - myRotation.yaw;
        if (angDiff > Math.PI) angDiff -= 2 * Math.PI; // Fix ang between -PI and +PI

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = steeringSmooth(angDiff);

        return new AgentOutput().withAcceleration(1).withSteer(steering);
    }

    /** Translate an angular difference to a smooth steering to avoid wobbly driving
     * @param ang the angle of which the car should adjust
     * @return the amount of steering needed to turn to the right angle */
    private double steeringSmooth(double ang) {
        return 2.0 / (1 + Math.pow(2.71828182845 , -5 * ang)) - 1; // 2/(1+e^(-5x)) - 1
    }
}
