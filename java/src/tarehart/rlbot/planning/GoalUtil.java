package tarehart.rlbot.planning;

import tarehart.rlbot.Bot;
import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.physics.BallPath;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class GoalUtil {

    public static final Goal BLUE_GOAL = new Goal(true);
    public static final Goal ORANGE_GOAL = new Goal(false);

    public static Goal getOwnGoal(Bot.Team team) {
        return team == Bot.Team.BLUE ? BLUE_GOAL : ORANGE_GOAL;
    }

    public static Goal getEnemyGoal(Bot.Team team) {
        return team == Bot.Team.BLUE ? ORANGE_GOAL : BLUE_GOAL;
    }

    public static Optional<SpaceTimeVelocity> predictGoalEvent(Goal goal, BallPath ballPath) {
        return ballPath.getPlaneBreak(ballPath.getStartPoint().time, goal.getScorePlane(), true);
    }

    public static boolean ballLingersInBox(Goal goal, BallPath ballPath) {
        Optional<SpaceTimeVelocity> firstSlice = ballPath.findSlice(slice -> goal.isInBox(slice.getSpace()));
        Optional<SpaceTimeVelocity> secondSlice = firstSlice.flatMap(stv -> ballPath.getMotionAt(stv.getTime().plusSeconds(2)));
        return secondSlice.isPresent() && goal.isInBox(secondSlice.get().getSpace());
    }
}
