package botenanna;

import botenanna.physics.TimeTracker;
import rlbot.api.GameData;

//TODO: is car on ground

/** This manages the input (packet).
 *  It handles calculations and other measures. */
public class AgentInput {

    private GameData.GameTickPacket packet;
    private TimeTracker timeTracker;

    /** The constructor.
     * @param packet the GameTickPacket.
     * @param timeTracker the class that tracks and handles time. */
    public AgentInput(GameData.GameTickPacket packet, TimeTracker timeTracker){
        this.packet = packet;
        this.timeTracker = timeTracker;
    }

    /** Used when GameTickPacket is accessed. */
    public GameData.GameTickPacket getPacket() {
        return packet;
    }

    /** Used when timeTracker is accessed. */
    public TimeTracker getTimeTracker() {
        return timeTracker;
    }

    /** Returns true if the car is on the ground.
     * @param playerIndex the index of the player.
     * @return true if the car is on the ground. */
    public boolean isCarOnGround(int playerIndex){
        float carZ = this.packet.getPlayers(playerIndex).getLocation().getZ();

        if(carZ < 20)
            return true;
        else
            return false;
    }
}
