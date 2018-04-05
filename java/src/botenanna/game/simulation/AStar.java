package botenanna.game.simulation;

import botenanna.game.ActionSet;
import botenanna.game.Situation;

import java.util.LinkedList;
import java.util.List;

public class AStar {

    public List<ActionSet> mutateAction(Situation situation, ActionSet current) {
        /* Variabler:
        [-1, 1]
        acceleration (deceleration)
        steeringTilt
        pitchTilt
        rollTilt

        [t/f]
        jumpDepressed
        boostDepressed
        slideDepressed

        ///////// Optimering
        [-1, 1] variabler behøver ikke reale tal
        [-1, 1] behøver kun være de nærmeste heltal
        jump er altid = false, hvis bilen ikke kan hoppe på nuværende tidspunkt TODO
        boost is false, if throttle is 0 or -1
        boost is false, if car has no boost
        pitch and roll is 0, if car is grounded
        slide is false, when boost is true
        slide is false, when steer is 0
        */

        List<ActionSet> following = new LinkedList<>();
        following.add(current);

        double[] newThrottles = mutateDouble(current.getThrottle());
        double[] newSteering = mutateDouble(current.getSteer());
        // pitch and roll is 0, if car is grounded
        double[] newPitch = mutateDouble(current.getPitch());
        double[] newRoll = mutateDouble(current.getRoll());
        boolean[] newJump = mutateBoolean();
        // boost is false, if car has no boost
        boolean[] newBoost = situation.myCar.boost > 0 ? mutateBoolean() : new boolean[]{false};
        boolean[] newSlide = mutateBoolean();

        for (double throttle : newThrottles) {
            for (double steer : newSteering) {
                for (double pitch : newPitch) {
                    for (double roll : newRoll) {
                        for (boolean jump : newJump) {
                            for (boolean boost : newBoost) {
                                // boost is false, if throttle is 0 or -1
                                if (boost && throttle != 1) continue;

                                for (boolean slide : newSlide) {
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

    private double[] mutateDouble(double value) {
        if (value == 1) return new double[]{1, 0};
        if (value == -1) return new double[]{0, -1};
        if (value == 0) return new double[]{1, 0, -1};
        return new double[0];
    }

    private boolean[] mutateBoolean() {
        return new boolean[]{true, false};
    }
}
