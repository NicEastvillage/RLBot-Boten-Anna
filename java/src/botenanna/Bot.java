package botenanna;

import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import rlbot.api.GameData;

import javax.vecmath.Vector2d;

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

        //Player info
        GameData.PlayerInfo me = packet.getPlayers(playerIndex);
        Vector2 myPos = Vector3.convert(me.getLocation()).asVector2();

        // Where will ball the land?
        Rigidbody ballBody = new Ball(packet.getBall());
        Vector2 ballLandingPos = ballBody.getPosition().asVector2(); // this is default, if ball is not "landing" anywhere
        double landingTime = ballBody.predictArrivalAtHeight(Ball.RADIUS);
        if (!Double.isNaN(landingTime)) {
            // Calculate landing position
            ballLandingPos = ballBody.stepped(landingTime).getPosition().asVector2();
        }

        // If the ball is behind the player and in the players half, it will go towards one of tree designated defence points based on the balls position
        if (isBallInTeamHalf(ballLandingPos,teamsDirectionToGoal(team))  && isBallBehind(ballLandingPos, myPos,teamsDirectionToGoal(team))){
            return goTowardsPoint(packet, getDefencePoint(ballLandingPos));
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
        Vector3 myRotation = Vector3.convert(me.getRotation());
        Vector2 my2dPos = Vector3.convert(me.getLocation()).asVector2();

        // Get the angle and distance between point and car.
        double ang = RLMath.carsAngleToPoint(my2dPos, myRotation.yaw, point);
        Vector2 distance = my2dPos.minus(point);


        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        //Currently stops close to the point it is trying to reach.
        if (distance.getMagnitude()<=90){
            return new AgentOutput().withDeceleration(1).withSteer((steering));
        }
        // Boosting towards the ball if the angle and distances are within the parameters.
        if (ang>=-0.2 && ang<=0.2 && distance.getMagnitude()>300){return new AgentOutput().withAcceleration(1).withBoost().withSteer(steering);
        }

        return new AgentOutput().withAcceleration(1).withSteer(steering);
    }
    /**
     * Chooses between 3 Defensive points based on the balls position and the bots team.
     * @param ballLandingPos The landing position of the ball
     * @return returns one of tree points in front of the goal depending on if the ball is on the top, bottom or in the middle of the field.
     */
    private Vector2 getDefencePoint(Vector2 ballLandingPos){
        if (posInField(ballLandingPos)==1){ return new Vector2(280, 5100 * teamsDirectionToGoal(team));}
        else if (posInField(ballLandingPos)==-1){ return new Vector2(-280, 5100* teamsDirectionToGoal(team));}
        else return new Vector2(0, 5120 * teamsDirectionToGoal(team));
    }

    private static int teamsDirectionToGoal(Team team) {
        return team == Team.BLUE ? -1 : 1;
    }
    private static boolean isBallInTeamHalf(Vector2 ballPoss, int team){
        return ballPoss.y*team>0;
    }

    /**
     * Returns an int depending on the current x-position of the ball in the field.
     * @param ballPos The position of the ball
     * @return  returns 1 if the ball is in the top part of the field, -1 if its the bottom part, and 0 if its in the middle.
     */
    private static int posInField(Vector2 ballPos){
        if (ballPos.x>325){ return 1;}
        else if (ballPos.x<-325) {return -1;}
        else return 0;
    }

    /**
     * Boolean that returns true if the ball is behind the player.
     */
    private static boolean isBallBehind(Vector2 ball, Vector2 Player, int team) {
        return ball.y*team > Player.y*team;
    }

}


