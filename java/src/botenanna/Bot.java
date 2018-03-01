package botenanna;

import botenanna.behaviortree.BehaviorTree;
import botenanna.behaviortree.tasks.TaskGoTowardsPoint;
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
    private BehaviorTree behaviorTree;


    public Bot(int playerIndex, int teamIndex) {
        this.playerIndex = playerIndex;
        team = (teamIndex == 0 ? Team.BLUE : Team.ORANGE);
        behaviorTree = buildBehaviourTree();
    }

    /** Hardcoded building of a BehaviourTree */
    public BehaviorTree buildBehaviourTree() {
        BehaviorTree bhtree = new BehaviorTree();
        bhtree.addChild(new TaskGoTowardsPoint(new String[] {"ball_pos"}));

        return bhtree;
    }

    /**
     * Let the bot process the information from the data packet
     *
     * @param packet the game tick packet from the game
     * @return an AgentOutput of what the agent want to do
     */
    public AgentOutput process(AgentInput packet) {

        return behaviorTree.evaluate(packet);
    }

    /**
     * @param packet the game tick packet from the game
     * @param point  the location the agent should go towards
     * @return an AgentOutput of what the agent needs to do to get to the point
     */
    private AgentOutput goTowardsPoint(GameData.GameTickPacket packet, Vector3 point) {

        // TODO For now we always to full throttle forwards, though that not be the shortest route. Maybe we should slide in some cases?
        // TODO Also, the bot will overshoot. In some cases we want the bot to stop, or get to point at a specific time (e.g. when ball lands)

        // Get the needed positions and rotations
        GameData.PlayerInfo me = packet.getPlayers(playerIndex);
        Vector3 myRotation = Vector3.convert(me.getRotation());
        // Converts vectors to 2d.
        Vector2 my2dPos = Vector3.convert(me.getLocation()).asVector2();
        Vector2 point2d = point.asVector2();


        // Get the angle and distance between point and car.
        double ang = RLMath.carsAngleToPoint(my2dPos, myRotation.yaw, point2d);
        Vector2 distance = my2dPos.minus(point2d);

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        //Currently stops close to the point it is trying to reach.
        if (distance.getMagnitude() <= 90 && point.z > 95) {
            return new AgentOutput().withDeceleration(1).withSteer((steering));
        }
        // Boosting towards the ball if the angle and distances are within the parameters.
        if (ang >= -0.2 && ang <= 0.2 && distance.getMagnitude() > 1000) {
            return new AgentOutput().withAcceleration(1).withBoost().withSteer(steering);
        }
        // Slides as a sharp turn
        if (ang > 1.5 || ang < -1.5) {
            return new AgentOutput().withAcceleration(1).withSteer(steering).withSlide();
        }
        return new AgentOutput().withAcceleration(1).withSteer(steering);
    }
    /**
     * Chooses between 3 Defensive points based on the balls position and the bots team.
     * The defensive points are defined as midair to avoid make sure the bot stops at them.
     * @param ballLandingPos The landing position of the ball
     * @return returns one of tree points in front of the goal depending on if the ball is on the top, bottom or in the middle of the field.
     */
    private Vector3 getDefencePoint(Vector3 ballLandingPos){
        if (posInField(ballLandingPos)==1){ return new Vector3(280, 5140 * teamsDirectionToGoal(team),400);}
        else if (posInField(ballLandingPos)==-1){ return new Vector3(-280, 5140* teamsDirectionToGoal(team),400);}
        else return new Vector3(0, 5120 * teamsDirectionToGoal(team),300);
    }

    /**
     * @param team The team identifier, either blue or  orange.
     * @return returns 1 if the team is blue otherwise -1.
     */
    private static int teamsDirectionToGoal(Team team) {
        return team == Team.BLUE ? -1 : 1;
    }

    /**
     * @param ballPoss The position of the ball
     * @param team  the team direction to goal int, either 1 or -1.
     * @return  returns true if the ball is on the teams half.
     */
    private static boolean isBallInTeamHalf(Vector3 ballPoss, int team){
        return ballPoss.y*team>0;
    }

    /**
     * Returns an int depending on the current x-position of the ball in the field.
     * @param ballPos The position of the ball
     * @return  returns 1 if the ball is in the top part of the field, -1 if its the bottom part, and 0 if its in the middle.
     */
    private static int posInField(Vector3 ballPos){
        if (ballPos.x>325){ return 1;}
        else if (ballPos.x<-325) {return -1;}
        else return 0;
    }

    /**
     * Boolean that returns true if the ball is behind the player.
     */
    private static boolean isBallBehind(Vector3 ball, Vector3 Player, int team) {
        return ball.y*team > Player.y*team;
    }

}