package botenanna;

import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.overlayWindow.StatusWindow;
import botenanna.physics.Rigidbody;
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

    /**
     * Let the bot process the information from the data packet
     *
     * @param packet the game tick packet from the game
     * @return an AgentOutput of what the agent want to do
     */
    public AgentOutput process(GameData.GameTickPacket packet) {

        // TODO Go towards where the ball will land!

        // Where will ball the land?
        Rigidbody ballBody = new Ball(packet.getBall());
        Vector2 ballLandingPos = ballBody.getPosition().asVector2(); // this is default, if ball is not "landing" anywhere
        double landingTime = ballBody.predictArrivalAtHeight(Ball.RADIUS);
        if (!Double.isNaN(landingTime)) {
            // Calculate landing position
            ballLandingPos = ballBody.stepped(landingTime).getPosition().asVector2();
        }

        return goTowardsPoint(packet, ballLandingPos);
    }

    /**
     * @param packet the game tick packet from the game
     * @param point  the location the agent should go towards
     * @return an AgentOutput of what the agent needs to do to get to the point
     */
    private AgentOutput goTowardsPoint(GameData.GameTickPacket packet, Vector2 point) {

        // TODO For now we always to full throttle forwards, though that not be the shortest route. Maybe we should slide in some cases?
        // TODO Also, the bot will overshoot. In some cases we want the bot to stop, or get to point at a specific time (e.g. when ball lands)

        // Get the needed positions and rotations
        GameData.PlayerInfo me = packet.getPlayers(playerIndex);
        Vector3 myPos = Vector3.convert(me.getLocation());
        Vector3 myRotation = Vector3.convert(me.getRotation());

        double ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, point);

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        if (me.getIsMidair()) {
            double smoothrotation = RLMath.steeringSmooth(-myRotation.pitch / 2);
            if (myRotation.pitch < 0) {
                if (myRotation.roll < 0) {
                    System.out.println("Rotating upwards and rolling +1.");
                    return new AgentOutput().withPitch(smoothrotation).withRoll(smoothrotation);
                }else {
                    return new AgentOutput().withPitch(smoothrotation);
                }
            } else if (myRotation.pitch > 0) {
                if (myRotation.roll > 0) {
                    System.out.println("Rotating upwards and rolling -1.");
                    return new AgentOutput().withPitch(smoothrotation).withRoll(smoothrotation);
                } else {
                    return new AgentOutput().withPitch(smoothrotation);
                }
            }
        }

        /*return new AgentOutput().withAcceleration(1).withBoost();*/
        if (ang > 1.5) {
            return new AgentOutput().withAcceleration(1).withSteer(steering).withSlide();
        } else if (ang < -1.5) {
            return new AgentOutput().withAcceleration(1).withSteer(steering).withSlide();
        } else {
            return new AgentOutput().withAcceleration(1).withSteer(steering);
        }
    }
}
