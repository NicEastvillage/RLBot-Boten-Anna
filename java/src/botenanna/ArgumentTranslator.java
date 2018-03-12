package botenanna;

import java.util.function.Function;

/** The ArgumentTranslators purpose is to translate the node arguments into Function objects, that allow quick
 * conversion to into the right variable. By using a Function we can avoid comparing Strings every time a node runs. */
public class ArgumentTranslator {
    public static Function<AgentInput, Object> get(String key) throws UnknownBTKeyException {
        switch (key) {
            case "my_index": return (AgentInput a) -> a.myPlayerIndex;          // index (int)
            case "enemy_index": return (AgentInput a) -> a.enemyPlayerIndex;    // index (int)

            case "my_pos": return (AgentInput a) -> a.myLocation;               // Vector3
            case "my_vel": return (AgentInput a) -> a.myVelocity;               // Vector3
            case "my_rot": return (AgentInput a) -> a.myRotation;               // Vector3
            case "enemy_pos": return (AgentInput a) -> a.enemyLocation;         // Vector3
            case "enemy_vel": return (AgentInput a) -> a.enemyVelocity;         // Vector3
            case "enemy_rot": return (AgentInput a) -> a.enemyRotation;         // Vector3

            case "ball_pos": return (AgentInput a) -> a.ballLocation;           // Vector3
            case "ball_vel": return (AgentInput a) -> a.ballVelocity;           // Vector3
            case "my_goal_box": return (AgentInput a) -> a.getGoalBox(a.myPlayerIndex); // Vector3
            case "enemy_goal_box": return (AgentInput a) -> a.getGoalBox(a.enemyPlayerIndex); // Vector3

            case "ball_land_time": return (AgentInput a) -> a.ballLandingTime;  // time (double)
            case "ball_land_pos": return (AgentInput a) -> a.ballLandingPosition; // Vector3

            case "ang_ball": return (AgentInput a) -> a.angleToBall;            // angle (double)

            default: throw new UnknownBTKeyException(key);
        }
    }
}
