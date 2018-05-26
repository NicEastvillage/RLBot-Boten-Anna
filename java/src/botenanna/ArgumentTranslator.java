package botenanna;

import botenanna.game.Arena;
import botenanna.game.Situation;
import java.util.function.Function;

/** The ArgumentTranslators purpose is to translate the node arguments into Function objects, that allow quick
 * conversion to into the right variable. By using a Function we can avoid comparing Strings every time a node runs. */
public class ArgumentTranslator {
    public static Function<Situation, Object> get(String key) throws UnknownBTKeyException {
        switch (key) {

            case "my_index": return (Situation a) -> a.myPlayerIndex;                       // index (int)
            case "enemy_index": return (Situation a) -> a.enemyPlayerIndex;                 // index (int)

            case "my_pos": return (Situation a) -> a.getMyCar().getPosition();               // Vector3
            case "my_vel": return (Situation a) -> a.getMyCar().getVelocity();               // Vector3
            case "my_rot": return (Situation a) -> a.getMyCar().getRotation();               // Vector3
            case "enemy_pos": return (Situation a) -> a.getEnemyCar().getPosition();         // Vector3
            case "enemy_vel": return (Situation a) -> a.getEnemyCar().getVelocity();         // Vector3
            case "enemy_rot": return (Situation a) -> a.getEnemyCar().getRotation();         // Vector3

            case "ball_pos": return (Situation a) -> a.getBall().getPosition();              // Vector3
            case "ball_vel": return (Situation a) -> a.getBall().getVelocity();              // Vector3
            case "ball_land_time": return (Situation a) -> a.getBallLandingTime();           // time (double)
            case "ball_land_pos": return (Situation a) -> a.getBallLandingPosition();        // Vector3

            case "best_boost": return (Situation a) -> a.getBestBoostPad();                  // Vector 3
            case "ang_ball": return (Situation a) -> a.getMyCar().getAngleToBall();          // angle (double)
            case "enemy_ang_ball": return (Situation a) -> a.getEnemyCar().getAngleToBall(); // angle (double)

            case "my_goal_pos": return (Situation a) -> Arena.getGoalPos(a.myPlayerIndex);   // Vector3
            case "enemy_goal_pos": return (Situation a) -> Arena.getGoalPos(a.enemyPlayerIndex);      // Vector3

            case "mid_zone": return (Situation a) ->  Arena.MIDFIELD_ACROSS;                          // Box
            case "my_goal_zone": return (Situation a) -> Arena.getGoalBoxArea(a.myPlayerIndex);       // Box
            case "enemy_goal_zone": return (Situation a) -> Arena.getGoalBoxArea(a.myPlayerIndex);    // Box
            case "my_goal_inside": return (Situation a) -> Arena.getGoalInside(a.myPlayerIndex);      // Box
            case "enemy_goal_inside": return (Situation a) -> Arena.getGoalInside(a.myPlayerIndex);   // Box

            default: throw new UnknownBTKeyException(key);
        }
    }
}
