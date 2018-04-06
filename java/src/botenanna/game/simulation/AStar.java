package botenanna.game.simulation;

import botenanna.game.ActionSet;
import botenanna.game.Situation;

import java.util.LinkedList;
import java.util.List;

public class AStar {

    /** This method will generate all valid ActionSet that sensibly follow a given ActionSet in a Situation.
     * @param situation the Situation.
     * @param current the ActionSet prior to the ones generated. */
    public List<ActionSet> mutateAction(Situation situation, ActionSet current) {
        /* Variabler:
        ///////// Optimizations taken
        [-1, 1] variabler behøver ikke reale tal
        [-1, 1] behøver kun være de nærmeste heltal
        jump is false, if jumping has no effect
        boost is false, if throttle is 0 or -1
        boost is false, if car has no boost
        pitch and roll is 0, if car is grounded
        slide is false, when boost is true
        slide is false, when steer is 0
        */

        List<ActionSet> following = new LinkedList<>();
        following.add(current);

        double[] newThrottles = mutateDirection(current.getThrottle());
        double[] newSteerings = mutateDirection(current.getSteer());
        // pitch and roll is 0, if car is grounded
        double[] newPitches = situation.myCar.isMidAir ? mutateDirection(current.getPitch()) : new double[]{0};
        double[] newRolls = situation.myCar.isMidAir ? mutateDirection(current.getRoll()) : new double[]{0};
        // jump is false, if jumping has no effect // FIXME With current implementation, second jump will always be one step long
        boolean[] newJumps = !situation.myCar.hasDoubleJumped ? new boolean[]{true, false} : new boolean[]{false};
        // boost is false, if car has no boost
        boolean[] newBoosts = situation.myCar.boost > 0 ? new boolean[]{true, false} : new boolean[]{false};
        boolean[] newSlides = new boolean[]{true, false};

        for (double throttle : newThrottles) {
            for (double steer : newSteerings) {
                for (double pitch : newPitches) {
                    for (double roll : newRolls) {
                        for (boolean jump : newJumps) {
                            for (boolean boost : newBoosts) {
                                // boost is false, if throttle is 0 or -1
                                if (boost && throttle != 1) continue;

                                for (boolean slide : newSlides) {
                                    // slide is false, when boost is true, or when steer == 0
                                    if (slide && (boost || steer == 0)) continue;

                                    following.add(new ActionSet()
                                            .withThrottle(throttle)
                                            .withSteer(steer)
                                            .withPitch(pitch)
                                            .withRoll(roll)
                                            .withJump(jump)
                                            .withBoost(boost)
                                            .withSlide(slide));
                                }
                            }
                        }
                    }
                }
            }
        }

        return following;
    }

    /** A helper method that returns an array of directions that are close to a given direction. */
    private double[] mutateDirection(double value) {
        if (value == 1) return new double[]{1, 0};
        if (value == -1) return new double[]{0, -1};
        if (value == 0) return new double[]{1, 0, -1};
        return new double[0];
    }
}
