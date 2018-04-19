package botenanna.game;

import botenanna.math.Vector2;
import rlbot.api.GameData;

import java.util.ArrayList;
import java.util.List;

public class Boostpads {

    public static final int PAD_RADIUS = 165; //Tested
    public static final int COUNT_BIG_PADS = 6; //Tested
    public static final int COUNT_SMALL_PADS = 28; //Tested
    public static final int COUNT_TOTAL_PADS = 34; //Tested

    private ArrayList<Boostpad> boostpadList;
    private Boolean isInitialized = false;

    /** Formats the boostInfo packet object to be used in this call.
     *  Removes boostpad element number 7 (6 if counting with 0) because its invalid.
     *  @param boostInfo The boostInfo object from the packet.
     *  @return Formatted array of boostpads. */
    private ArrayList<Boostpad> createListOfBoostpads(List<GameData.BoostInfo> boostInfo){

        ArrayList<Boostpad> boostpadList = new ArrayList<>();

        for(int i = 0; i < boostInfo.size(); i++){

            if(i != 6) //element 6 is invalid
                boostpadList.add(new Boostpad(boostInfo.get(i).getLocation().getX(), boostInfo.get(i).getLocation().getY(), boostInfo.get(i).getIsActive(), boostInfo.get(i).getTimer()));
        }

        return boostpadList;
    }

    /** Updates or initializes the boostpad list.
     *  @param boostInfo the object boostInfo from the game packet. */
    public void updateBoostpadList(List<GameData.BoostInfo> boostInfo){

        ArrayList<Boostpad> inputArray = createListOfBoostpads(boostInfo);

        if(!isInitialized){
            boostpadList = inputArray;
            isInitialized = true;
        }else{
            if(boostInfo.size() != inputArray.size())
                System.out.println("SOMETHING WENT WRONG! Boostpad2 class. Contact -> MIKKEL!"); //TODO keep? Should never get here!

            for(int i = 0; i < boostInfo.size(); i++){
                boostpadList.get(i).setLocation(inputArray.get(i).getLocation());
                boostpadList.get(i).setActive(inputArray.get(i).isActive());
                boostpadList.get(i).setTimer(inputArray.get(i).getTimer());
            }
        }
    }

    public Boostpad getBoostpad(int index){
        if(isInitialized)
            return boostpadList.get(index);
        else
            return null;
    }

    /** Prints out the information in the boostpad list. */
    public void printBoostpadList(){
        if(isInitialized){
            for(int i = 0; i < boostpadList.size(); i++){
                System.out.println(i + ": " + boostpadList.get(i).toString());
            }
        }else
            System.out.println("The boostpad list has not yet been updated.");
    }

    /** @return the ArrayList of boostpads. Returns null if the list is not initialized. */
    public ArrayList<Boostpad> getBoostpadList() {
        if(isInitialized)
            return boostpadList;
        else
            return null;
    }

    /** Replaces the current boostpadList with a given one.
     *  @param boostpadList the boostpadList to be copyed. */
    public void setBoostpadList(ArrayList<Boostpad> boostpadList){
        this.boostpadList = new ArrayList<>();

        for(int i = 0; i < boostpadList.size(); i++)
            this.boostpadList.add(boostpadList.get(i));
    }

    /** The boostpad as a object. */
    public class Boostpad{

        private Vector2 location;
        private Boolean isActive;
        private int timer;

        public Boostpad(float x, float y, Boolean isActive, int timer) {
            this.location = new Vector2(x, y);
            this.isActive = isActive;
            this.timer = timer;
        }

        @Override
        public String toString() {
            return "x: " + location.x + ". y: " + location.y +
                    ". active: " + isActive + ". timer: " + timer;
        }

        public Vector2 getLocation() {
            return location;
        }

        public Boolean isActive() {
            return isActive;
        }

        public int getTimer() {
            return timer;
        }

        public void setLocation(Vector2 location) {
            this.location = location;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public void setTimer(int timer) {
            this.timer = timer;
        }
    }
}
