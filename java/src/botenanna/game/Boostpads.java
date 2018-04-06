package botenanna.game;

import botenanna.math.Vector2;
import botenanna.math.Vector3;
import javafx.util.Pair;
import rlbot.api.GameData;

import java.util.ArrayList;
import java.util.List;

public class Boostpads {

    public static final int NUM_PADS = 34;
    public static final int NUM_BIGBOOST = 6;
    public ArrayList<Pair<Vector3,Boolean>> boostPadList;

    public Boostpads(GameData.GameTickPacket packet){

        List<GameData.BoostInfo> boostInfolist = packet.getBoostPadsList();
        boostPadList = new ArrayList<>();

        for (int i = 0; i>NUM_PADS; i++){
            Vector3 padLocation = new Vector3(boostInfolist.get(i).getLocation().getX(),boostInfolist.get(i).getLocation().getY(),boostInfolist.get(i).getLocation().getZ());
            boostPadList.add(i,BoostPadPairing(padLocation, boostInfolist.get(i).getIsActive()));
        }
    }


    public static Pair<Vector3, Boolean> BoostPadPairing( Vector3 vector, Boolean bool){
        return  new Pair<>(vector,bool);
    }

    public Boostpads(ArrayList<Pair<Vector3,Boolean>> list){
        boostPadList = list;
    }

    public static Vector2[] bigBoostPad = {
            new Vector2(-3070, 4100), //Index 7
            new Vector2(3070,-4100), //Index 8
                new Vector2(-3070,-4100),new Vector2(-3580,0), new Vector2(3580,0), new Vector2(3070, 4100)
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

    public Pair<Vector3, Boolean> get(int i) {
        return boostPadList.get(i);
    }
}
