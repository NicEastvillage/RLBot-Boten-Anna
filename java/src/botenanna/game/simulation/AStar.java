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
        slide er altid = 0, hvis steering = 0
        jump er altid = false, hvis bilen ikke kan hoppe på nuværende tidspunkt
        boost er altid = false, hvis bilen ingen boost har
        boost er altid = false, hvis acceleration er 0 eller -1
        pitch og roll er altid = 0, hvis bilen er grounded
        slide er altid = false, når boost = true
        */

        List<ActionSet> following = new LinkedList<>();
        following.add(current);

        double[] newThrottles = mutateDouble(current.getThrottle());
        double[] newSteering = mutateDouble(current.getSteer());
        double[] newPitch = mutateDouble(current.getPitch());
        double[] newRoll = mutateDouble(current.getRoll());
        boolean[] newJump = mutateBoolean();
        boolean[] newBoost = mutateBoolean();
        boolean[] newSlide = mutateBoolean();

        for (double throttle : newThrottles) {
            for (double steer : newSteering) {
                for (double pitch : newPitch) {
                    for (double roll : newRoll) {
                        for (boolean jump : newJump) {
                            for (boolean boost : newBoost) {
                                for (boolean slide : newSlide) {
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
