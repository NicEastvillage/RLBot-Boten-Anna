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
        GameData.BoostInfo yo2 = packet.getBoostPads(2);
        GameData.BoostInfo yo3 = packet.getBoostPads(3);
        GameData.BoostInfo yo4 = packet.getBoostPads(4);
        GameData.BoostInfo yo5 = packet.getBoostPads(5);
        GameData.BoostInfo yo6 = packet.getBoostPads(6);

        System.out.println("1. Location test" + Vector3.convert(yo.getLocation()));
        System.out.println("2. Location test" + Vector3.convert(yo2.getLocation()));
        System.out.println("3. Location test" + Vector3.convert(yo3.getLocation()));
        System.out.println("4. Location test" + Vector3.convert(yo4.getLocation()));
        System.out.println("5. Location test" + Vector3.convert(yo5.getLocation()));
        System.out.println("6. Location test" + Vector3.convert(yo6.getLocation()));

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
