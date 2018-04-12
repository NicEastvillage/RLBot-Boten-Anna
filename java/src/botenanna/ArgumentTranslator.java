package botenanna;

import java.util.function.Function;

/** The ArgumentTranslators purpose is to translate the node arguments into Function objects, that allow quick
 * conversion to into the right variable. By using a Function we can avoid comparing Strings every time a node runs. */
public class ArgumentTranslator {
    public static Function<AgentInput, Object> get(String key) throws UnknownBTKeyException {
        switch (key) {
            case "my_index": return (AgentInput a) -> a.myPlayerIndex;          // index (int)
            case "enemy_index": return (AgentInput a) -> a.enemyPlayerIndex;    // index (int)

            case "my_pos": return (AgentInput a) -> a.myCar.position;               // Vector3
            case "my_vel": return (AgentInput a) -> a.myCar.velocity;               // Vector3
            case "my_rot": return (AgentInput a) -> a.myCar.rotation;               // Vector3
            case "enemy_pos": return (AgentInput a) -> a.enemyCar.position;         // Vector3
            case "enemy_vel": return (AgentInput a) -> a.enemyCar.velocity;         // Vector3
            case "enemy_rot": return (AgentInput a) -> a.enemyCar.rotation;         // Vector3

            case "ball_pos": return (AgentInput a) -> a.ball.getPosition();          // Vector3
            case "ball_vel": return (AgentInput a) -> a.ball.getVelocity();          // Vector3
            case "my_goal_box": return (AgentInput a) -> a.getGoalBox(a.myPlayerIndex); // Vector3
            case "best_boost": return (AgentInput a) -> a.getBestBoostPad();         // Vector 3
            case "enemy_goal_box": return (AgentInput a) -> a.getGoalBox(a.enemyPlayerIndex); // Vector3
            case "my_corner_plus": return (AgentInput a) ->  a.getMyCorner(1);       // Vector3
            case "my_corner_minus": return (AgentInput a) ->  a.getMyCorner(-1);     // Vector3

            case "enemy_goal": return (AgentInput a) -> a.getEnemyBoxArea(a.myPlayerIndex);     // Box

            case "ball_land_time": return (AgentInput a) -> a.ballLandingTime;       // time (double)
            case "ball_land_pos": return (AgentInput a) -> a.ballLandingPosition;    // Vector3

            case "ang_ball": return (AgentInput a) -> a.myCar.angleToBall;           // angle (double)
            case "enemy_ang_ball": return (AgentInput a) -> a.enemyCar.angleToBall;  // angle (double)

            default: throw new UnknownBTKeyException(key);
        }
    }
}
