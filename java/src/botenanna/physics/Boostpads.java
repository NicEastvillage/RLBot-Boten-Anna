package botenanna.physics;

import botenanna.math.Vector2;
import botenanna.math.Vector3;
import rlbot.api.GameData;

public class Boostpads {
    public Vector2[] bigBoostPad = {
            new Vector2(-3070, 4100),
            new Vector2(3070, 4100),
            new Vector2(-3580,0),
            new Vector2(3580,0),
            new Vector2(-3070,-4100),
            new Vector2(3070,-4100)
    };

    public Vector2 collectNearestBoost(Vector2 point){



        return bigBoostPad[0];
    }
}
