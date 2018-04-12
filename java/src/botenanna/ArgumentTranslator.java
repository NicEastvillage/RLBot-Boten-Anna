package botenanna;

import botenanna.game.Situation;

import java.util.function.Function;

/** The ArgumentTranslators purpose is to translate the node arguments into Function objects, that allow quick
 * conversion to into the right variable. By using a Function we can avoid comparing Strings every time a node runs. */
public class ArgumentTranslator {
    public static Function<Situation, Object> get(String key) throws UnknownBTKeyException {
        switch (key) {
            case "my_index": return (Situation a) -> a.myPlayerIndex;          // index (int)
            case "enemy_index": return (Situation a) -> a.enemyPlayerIndex;    // index (int)

            case "my_pos": return (Situation a) -> a.myCar.position;               // Vector3
            case "my_vel": return (Situation a) -> a.myCar.velocity;               // Vector3
            case "my_rot": return (Situation a) -> a.myCar.rotation;               // Vector3
            case "enemy_pos": return (Situation a) -> a.enemyCar.position;         // Vector3
            case "enemy_vel": return (Situation a) -> a.enemyCar.velocity;         // Vector3
            case "enemy_rot": return (Situation a) -> a.enemyCar.rotation;         // Vector3

            case "ball_pos": return (Situation a) -> a.ball.getPosition();          // Vector3
            case "ball_vel": return (Situation a) -> a.ball.getVelocity();          // Vector3
            case "my_goal_box": return (Situation a) -> a.getGoalBox(a.myPlayerIndex); // Vector3
            case "best_boost": return (Situation a) -> a.getBestBoostPad();         // Vector 3
            case "enemy_goal_box": return (Situation a) -> a.getGoalBox(a.enemyPlayerIndex); // Vector3
            case "my_corner_plus": return (Situation a) ->  a.getMyCorner(1);       // Vector3
            case "my_corner_minus": return (Situation a) ->  a.getMyCorner(-1);     // Vector3

            case "ball_land_time": return (Situation a) -> a.ballLandingTime;       // time (double)
            case "ball_land_pos": return (Situation a) -> a.ballLandingPosition;    // Vector3

            case "ang_ball": return (Situation a) -> a.myCar.angleToBall;           // angle (double)
            case "enemy_ang_ball": return (Situation a) -> a.enemyCar.angleToBall;  // angle (double)

            default: throw new UnknownBTKeyException(key);
        }
    }
}
