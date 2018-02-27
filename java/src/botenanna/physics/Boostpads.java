package botenanna.physics;

import botenanna.math.Vector2;
import rlbot.api.GameData;

public class Boostpads {
    public Vector2[] bigBoostPad = {
            new Vector2(-3070, 4100), //Index 7
            new Vector2(3070,-4100), //Index 8
            new Vector2(-3070,-4100), //Index 9
            new Vector2(-3580,0), //Index 10
            new Vector2(3580,0), // Index 11
            new Vector2(3070, 4100) //Index 12
    };

    public Vector2 collectNearestBoost(GameData.GameTickPacket packet, Vector2 playerPos){
        int i;
        int index = 0;
        double mainDistance = 99999;
        double secondaryDistance;
        Vector2 temp;

        for(i = 0; i <= 5; i++) {
            GameData.BoostInfo isActive = packet.getBoostPads(7+i);
            if(isActive.getIsActive()) {
                temp = playerPos.minus(bigBoostPad[i]);
                secondaryDistance = temp.getMagnitude();
                if (secondaryDistance < mainDistance) {
                    mainDistance = secondaryDistance;
                    index = i;
                }
            }
        }
        return bigBoostPad[index];
    }
}
