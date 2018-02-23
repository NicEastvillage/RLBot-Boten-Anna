package botenanna;

import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
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

    /** Let the bot process the information from the data packet
     * @param packet the game tick packet from the game
     * @return an AgentOutput of what the agent want to do */
    public AgentOutput process(GameData.GameTickPacket packet) {

        // TODO Go towards where the ball will land!
        GameData.BallInfo ball = packet.getBall();
        Vector3 ballPos = Vector3.convert(ball.getLocation());
        Vector3 ballVel = Vector3.convert(ball.getVelocity());
        Vector2 ballBuff = new Vector2(0,400);

        // Where will ball the land?
        Vector2 ballLandingPos = ballPos.asVector2(); // this is default, if ball is not "landing" anywhere
        Rigidbody ballBody = new Rigidbody();
        ballBody.setPosition(ballPos);
        ballBody.setVelocity(ballVel);
        ballBody.setAffectedByGravity(true);
        double landingTime = ballBody.predictArrivalAtHeight(92); // TODO: BALL RADIUS = 92 uu
        boolean home;

        if (team == Team.BLUE) { //If the player is blue then they will  return "home" if they are on the wrong side of it
            home = goingHome(ballLandingPos,ballBuff, Vector3.convert(packet.getPlayers(playerIndex).getLocation()).asVector2(),true);
            if (home) {
                return goTowardsPoint(packet, new Vector2(0, -5120));

            }
            if (!Double.isNaN(landingTime)) {
                ballBody.step(landingTime);
                ballLandingPos = ballBody.getPosition().asVector2();
            }
            return goTowardsPoint(packet, ballLandingPos);

        }  else {
            home = goingHome(ballLandingPos,ballBuff,Vector3.convert(packet.getPlayers(playerIndex).getLocation()).asVector2(),false);
            if (home) {
                return goTowardsPoint(packet, new Vector2(0, 5120));

            }
            if (!Double.isNaN(landingTime)) {
                    ballBody.step(landingTime);
                    ballLandingPos = ballBody.getPosition().asVector2();
            }
            return goTowardsPoint(packet, ballLandingPos);

        }

}
    /**
     * @param packet the game tick packet from the game
     * @param point the location the agent should go towards
     * @return an AgentOutput of what the agent needs to do to get to the point */
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

        return new AgentOutput().withAcceleration(1).withSteer(steering);

    }
    private boolean goingHome (Vector2 ball, Vector2 player, Vector2 ballBuff,  boolean color ){
        if(color){
        if (ball.y-ballBuff.y < player.y){
            return true;
        }else if (ball.y-ballBuff.y > player.y && (ball.y-ballBuff.y) > ballBuff.y){
            return false;
        }
        else return false;

    }   else
            if (ball.y+ballBuff.y > player.y){
                return true;
            }else if (ball.y+ballBuff.y < player.y && (ball.y+ballBuff.y) < ballBuff.y){
                return false;
            }
            else return false;

    }
}


