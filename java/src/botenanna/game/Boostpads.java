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
        updateBoostpadList(inputArray);
    }

    /** Updates or initializes the boostpad list.
     *  @param boostList the object boostInfo from the game packet. */
    public void updateBoostpadList(ArrayList<Boostpad> boostList) {
        if(!isInitialized){
            boostpadList = boostList;
            isInitialized = true;
        }else{
            if(boostList.size() != boostList.size())
                System.out.println("SOMETHING WENT WRONG! Boostpad2 class. Contact -> MIKKEL!"); //TODO keep? Should never get here!

            for(int i = 0; i < boostList.size(); i++){
                boostpadList.get(i).setPosition(boostList.get(i).getPosition());
                boostpadList.get(i).setActive(boostList.get(i).isActive());
                boostpadList.get(i).setTimer(boostList.get(i).getTimer());
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
        this.boostpadList = new ArrayList<>(boostpadList);

        isInitialized = true;
    }

    /** The boostpad as a object. */
    public class Boostpad{

        private Vector2 position;
        private Boolean isActive;
        private int timer;

        public Boostpad(float x, float y, Boolean isActive, int timer) {
            this.position = new Vector2(x, y);
            this.isActive = isActive;
            this.timer = timer;
        }

        @Override
        public String toString() {
            return "x: " + position.x + ". y: " + position.y +
                    ". active: " + isActive + ". timer: " + timer;
        }

        public Vector2 getPosition() {
            return position;
        }

        public Boolean isActive() {
            return isActive;
        }

        public int getTimer() {
            return timer;
        }

        public void setPosition(Vector2 position) {
            this.position = position;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public void setTimer(int timer) {
            this.timer = timer;
        }
    }
}
