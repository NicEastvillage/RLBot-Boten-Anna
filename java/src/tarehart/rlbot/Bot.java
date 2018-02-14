package tarehart.rlbot;

public class Bot {

    public enum Team {
        BLUE, ORANGE
    }

    // private final Team team;
    private final int playerIndex;

    public Bot(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
