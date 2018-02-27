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
            new Vector2(3070,-4100) //Index 6
    };

    public Vector2 collectNearestBoost(GameData.GameTickPacket packet, Vector2 playerPos){
        int i;
        int index = 0;
        double mainDistance = 99999;
        double secondaryDistance = 0;
        Vector2 temp;

        GameData.BoostInfo yo7 = packet.getBoostPads(7);
        GameData.BoostInfo yo8 = packet.getBoostPads(8);
        GameData.BoostInfo yo9 = packet.getBoostPads(9);
        GameData.BoostInfo yo10 = packet.getBoostPads(10);
        GameData.BoostInfo yo11 = packet.getBoostPads(11);
        GameData.BoostInfo yo12 = packet.getBoostPads(12);
        GameData.BoostInfo yo13 = packet.getBoostPads(13);
        GameData.BoostInfo yo14 = packet.getBoostPads(14);
        GameData.BoostInfo yo15 = packet.getBoostPads(15);
        GameData.BoostInfo yo16 = packet.getBoostPads(16);
        GameData.BoostInfo yo17 = packet.getBoostPads(17);
        GameData.BoostInfo yo18 = packet.getBoostPads(18);
        GameData.BoostInfo yo19 = packet.getBoostPads(19);
        GameData.BoostInfo yo20 = packet.getBoostPads(20);
        GameData.BoostInfo yo21 = packet.getBoostPads(21);
        GameData.BoostInfo yo22 = packet.getBoostPads(22);
        GameData.BoostInfo yo23 = packet.getBoostPads(23);
        GameData.BoostInfo yo24 = packet.getBoostPads(24);
        GameData.BoostInfo yo25 = packet.getBoostPads(25);
        GameData.BoostInfo yo26 = packet.getBoostPads(26);
        GameData.BoostInfo yo27 = packet.getBoostPads(27);
        GameData.BoostInfo yo28 = packet.getBoostPads(28);
        GameData.BoostInfo yo29 = packet.getBoostPads(29);
        GameData.BoostInfo yo30 = packet.getBoostPads(30);
        GameData.BoostInfo yo31 = packet.getBoostPads(31);
        GameData.BoostInfo yo32 = packet.getBoostPads(32);
        GameData.BoostInfo yo33 = packet.getBoostPads(33);
        GameData.BoostInfo yo34 = packet.getBoostPads(34);

        System.out.println("START 7 Location test" + Vector3.convert(yo7.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo8.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo9.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo10.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo11.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo12.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo13.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo14.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo15.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo16.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo17.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo18.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo19.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo20.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo21.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo22.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo23.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo24.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo25.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo26.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo27.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo28.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo29.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo30.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo31.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo32.getLocation()));
        System.out.println("Location test" + Vector3.convert(yo33.getLocation()));
        System.out.println("END 34 Location test" + Vector3.convert(yo34.getLocation()));

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
