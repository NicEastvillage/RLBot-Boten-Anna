package botenanna.game;

import botenanna.math.Vector2;
import botenanna.math.Vector3;

public class Boostpad {

    public static final int PAD_RADIUS = 165;
    public static final int COUNT_BIG_PADS = 6;
    public static final int COUNT_SMALL_PADS = 28;
    public static final int COUNT_TOTAL_PADS = COUNT_BIG_PADS + COUNT_SMALL_PADS;
    public static final int AMOUNT_IN_SMALL = 12;
    public static final int AMOUNT_IN_BIG = 100;
    public static final int RESPAWN_TIME_BIG = 10;
    public static final int RESPAWN_TIME_SMALL = 3;
    public static final Vector3[] BIG_BOOST_PADS_POSITIONS = {
            new Vector3(-3070, 4100),
            new Vector3(3070,-4100),
            new Vector3(-3070,-4100),
            new Vector3(-3580,0),
            new Vector3(3580,0),
            new Vector3(3070, 4100)
    };

    private Vector3 position;
    private double respawnTimeLeft;
    private boolean isBigBoostPad;

    public Boostpad(double x, double y, double respawnTimeLeft) {
        this.position = new Vector3(x, y);
        setRespawnTimeLeft(respawnTimeLeft);
        determineIfBig();
    }

    public Boostpad(Boostpad from) {
        this.position = new Vector3(from.position.x, from.position.y);
        setRespawnTimeLeft(respawnTimeLeft);
        isBigBoostPad = from.isBigBoostPad;
    }

    private void determineIfBig() {
        double allowedDiff = 20;
        for (Vector3 bigBoostPadsPosition : BIG_BOOST_PADS_POSITIONS) {
            if (position.getDistanceTo(bigBoostPadsPosition) <= allowedDiff) {
                isBigBoostPad = true;
                return;
            }
        }
        isBigBoostPad = false;
    }

    @Override
    public String toString() {
        return "Boostpad(x: " + position.x + ", y: " + position.y +
                ". t: " + respawnTimeLeft + ", big: " + isBigBoostPad + ")";
    }

    public Vector3 getPosition() {
        return position;
    }

    public Boolean isActive() {
        return respawnTimeLeft <= 0;
    }

    public double getRespawnTimeLeft() {
        return respawnTimeLeft;
    }

    public void setRespawnTimeLeft(double respawnTimeLeft) {
        this.respawnTimeLeft = Math.max(0, respawnTimeLeft);
    }

    /** @return whether the Boostpad is active after the reduction. */
    public boolean reduceRespawnTimeLeft(double amount) {
        setRespawnTimeLeft(respawnTimeLeft - amount);
        return isActive();
    }

    public void refreshRespawnTimer() {
        respawnTimeLeft = isBigBoostPad ? RESPAWN_TIME_BIG : RESPAWN_TIME_SMALL;
    }

    public void setActive() {
        respawnTimeLeft = 0;
    }

    public int getBoostAmount() {
        return isBigBoostPad ? AMOUNT_IN_BIG : AMOUNT_IN_SMALL;
    }
}
