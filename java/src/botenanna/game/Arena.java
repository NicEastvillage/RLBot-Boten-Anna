package botenanna.game;

import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;

/** The Arena object contains constants and method to help getting the right constants. */
public class Arena {

    public static final double LENGTH = 10280;
    public static final double WIDTH = 8240;
    public static final double HEIGHT = 4060;

    public static final double WALL_X = WIDTH/2;
    public static final double WALL_Y = LENGTH/2;

    public static final double GOAL_POST_X_OFFSET = 720;
    public static final double GOAL_HEIGHT = 700;
    public static final double GOAL_DEPHT = 800;

    public static final Box FIELD = new Box(new Vector3(WALL_X, WALL_Y, HEIGHT), new Vector3(-WALL_X, -WALL_Y, HEIGHT));
    public static final Box ORANGE_GOAL_BOX_AREA = new Box(new Vector3(-GOAL_POST_X_OFFSET, WALL_Y, 0), new Vector3(GOAL_POST_X_OFFSET, (WALL_Y - 1000), 1500));
    public static final Box BLUE_GOAL_BOX_AREA = new Box(new Vector3(-GOAL_POST_X_OFFSET, -WALL_Y, 0), new Vector3(GOAL_POST_X_OFFSET, -(WALL_Y - 1000), 1500));
    public static final Box MIDFIELD_ACROSS = new Box(new Vector3(-WALL_X, -2080, HEIGHT), new Vector3(WALL_X, 2080, 0));
    public static final Box BLUE_CORNER_NEGATIVE = new Box(new Vector3(-WALL_X, -WALL_Y, HEIGHT), new Vector3(0, 0, 0));
    public static final Box BLUE_CORNER_POSITIVE = new Box(new Vector3(-WALL_X, WALL_Y, HEIGHT), new Vector3(0, 0, 0));
    public static final Box ORANGE_CORNER_NEGATIVE = new Box(new Vector3(WALL_X, WALL_Y, HEIGHT), new Vector3(0, 0, 0));
    public static final Box ORANGE_CORNER_POSITVE = new Box(new Vector3(WALL_X, -WALL_Y, HEIGHT), new Vector3(0, 0, 0));
    public static final Box ORANGE_GOAL_INSIDE = new Box(new Vector3(-GOAL_POST_X_OFFSET, WALL_Y, 0), new Vector3(GOAL_POST_X_OFFSET, (WALL_Y + GOAL_DEPHT), GOAL_HEIGHT));
    public static final Box BLUE_GOAL_INSIDE = new Box(new Vector3(-GOAL_POST_X_OFFSET, -WALL_Y, 0), new Vector3(GOAL_POST_X_OFFSET, -(WALL_Y + GOAL_DEPHT), GOAL_HEIGHT));

    public static final Vector2 BLUE_GOALPOST_LEFT = new Vector2(-GOAL_POST_X_OFFSET, -WALL_Y);
    public static final Vector2 BLUE_GOALPOST_RIGHT = new Vector2(GOAL_POST_X_OFFSET, -WALL_Y);
    public static final Vector2 RED_GOALPOST_LEFT = new Vector2(-GOAL_POST_X_OFFSET, WALL_Y);
    public static final Vector2 RED_GOALPOST_RIGHT = new Vector2(GOAL_POST_X_OFFSET, WALL_Y);
    public static final Vector3 BLUE_GOAL_POS = Vector3.FORWARD.scale(-5000);
    public static final Vector3 ORANGE_GOAL_POS = Vector3.FORWARD.scale(5000);

    /** @return either +1 or -1, depending on which end of the y-axis this player's goal is. */
    public static int getTeamGoalYDirection(int playerIndex) {
        return playerIndex == 0 ? -1 : 1;
    }

    /** @return a Zone that is equal to the whole field, but all walls are offset.
     * This makes i useful to test if things are close to the walls. Positive offsets inwards. */
    public static Box getFieldWithWallOffset(double offset) {
        double wx = WALL_X - offset;
        double wy = WALL_Y - offset;
        return new Box(new Vector3(wx, wy, HEIGHT), new Vector3(-wx, -wy, 0));
    }

    /** @return the goal box owner by playerIndex */
    public static Box getGoalBoxArea(int playerIndex) {
        return playerIndex == 0 ? BLUE_GOAL_BOX_AREA : ORANGE_GOAL_BOX_AREA;
    }

    /** @return the box inside goal belonging to playerIndex */
    public static Box getGoalInside(int playerIndex) {
        return playerIndex == 0 ? BLUE_GOAL_INSIDE : ORANGE_GOAL_INSIDE;
    }

    /** @return a point in front of the goal belonging to playerIndex */
    public static Vector3 getGoalPos(int playerIndex) {
        return playerIndex == 0 ? BLUE_GOAL_POS : ORANGE_GOAL_POS;
    }
}
