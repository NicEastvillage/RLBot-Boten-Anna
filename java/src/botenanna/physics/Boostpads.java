package botenanna.physics;

import botenanna.math.Vector2;
import botenanna.math.Vector3;
import rlbot.api.GameData;

import java.util.List;

public class Boostpads {
    public Vector2[] bigBoostPad = {
            new Vector2(-3070, 4100),
            new Vector2(3070, 4100),
            new Vector2(-3580,0),
            new Vector2(3580,0),
            new Vector2(-3070,-4100),
            new Vector2(3070,-4100)
    };

    public Vector2 collectNearestBoost(GameData.GameTickPacket packet, Vector2 playerPos){
        int i;
        int index = 0;
        double mainDistance = 99999;
        double secondaryDistance = 0;
        Vector2 temp;

        GameData.BoostInfo yo = packet.getBoostPads(1);
        System.out.println(yo.getLocation());

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
