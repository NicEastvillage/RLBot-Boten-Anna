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

    public Vector2 collectNearestBoost(Vector2 playerPos){
        int i;
        int index = 0;
        double mainDistance = 99999;
        double secondaryDistance = 0;
        Vector2 temp;

        for(i = 0; i <= 5; i++) {
            temp = playerPos.minus(bigBoostPad[i]);
            secondaryDistance = temp.getMagnitude();
            if(secondaryDistance < mainDistance) {
                mainDistance = secondaryDistance;
                index = i;
            }
        }
        return bigBoostPad[index];
    }
}
