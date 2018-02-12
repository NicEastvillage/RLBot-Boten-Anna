package tarehart.rlbot.planning;

import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.math.TimeUtil;
import tarehart.rlbot.steps.BlindStep;
import tarehart.rlbot.steps.TapStep;
import tarehart.rlbot.steps.landing.LandGracefullyStep;
import tarehart.rlbot.steps.landing.LandMindlesslyStep;
import tarehart.rlbot.steps.strikes.MidairStrikeStep;

import java.time.Duration;

public class SetPieces {

    public static Plan frontFlip() {

        return new Plan()
                .unstoppable()
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withPitch(-1)
                                .withJump(true)
                                .withAcceleration(1)))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withPitch(-1)
                                .withAcceleration(1)
                ))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1)
                                .withPitch(-1)))
                .withStep(new BlindStep(
                        new AgentOutput()
                                .withAcceleration(1)
                                .withPitch(-1),
                        Duration.ofMillis(900)
                ))
                .withStep(new LandGracefullyStep());
    }

    public static Plan performAerial() {
        Duration tiltBackDuration = Duration.ofMillis(390);

        return new Plan()
                .withStep(new BlindStep(
                        new AgentOutput()
                            .withJump(true)
                            .withPitch(1),
                        tiltBackDuration
                ))
                .withStep(new MidairStrikeStep(tiltBackDuration))
                .withStep(new LandGracefullyStep(LandGracefullyStep.FACE_BALL));
    }

    public static Plan performJumpHit(double strikeHeight) {

        long totalRiseMillis = Math.min(500, (long) (strikeHeight * 80));
        long pitchBackPortion = Math.min(360, totalRiseMillis);
        long driftUpPortion = totalRiseMillis - pitchBackPortion;

        Plan plan = new Plan()
                .withStep(new BlindStep(
                        new AgentOutput()
                                .withJump(true)
                                .withPitch(1),
                        Duration.ofMillis(pitchBackPortion)
                ));

        if (driftUpPortion > 0) {
            plan.withStep(new BlindStep(
                    new AgentOutput()
                            .withJump(true),
                    Duration.ofMillis(driftUpPortion)
            ));
        }


        return plan
                .withStep(new TapStep(1,
                        new AgentOutput()
                                .withPitch(-1)
                                .withJump(false)
                                .withAcceleration(1)))
                .withStep(new TapStep(5,
                        new AgentOutput()
                                .withPitch(-1)
                                .withJump(true)
                                .withAcceleration(1)))
                .withStep(new BlindStep(
                        new AgentOutput()
                                .withAcceleration(1)
                                .withPitch(-1),
                        Duration.ofMillis(800)
                ))
                .withStep(new LandGracefullyStep(LandGracefullyStep.FACE_BALL));
    }

    public static Plan sideFlip(boolean flipLeft) {
        return new Plan()
                .unstoppable()
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1)))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withAcceleration(1)
                ))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1)
                                .withSteer(flipLeft ? -1 : 1)))
                .withStep(new LandMindlesslyStep());
    }

    public static Plan jumpSideFlip(boolean flipLeft, double jumpTime) {

        jumpTime = Math.max(jumpTime, 0);

        return new Plan()
                .unstoppable()
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1)))
                .withStep(new BlindStep(
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1), TimeUtil.toDuration(jumpTime)))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withAcceleration(1)
                ))
                .withStep(new TapStep(2,
                        new AgentOutput()
                                .withJump(true)
                                .withAcceleration(1)
                                .withSteer(flipLeft ? -1 : 1)))
                .withStep(new LandGracefullyStep(LandGracefullyStep.FACE_BALL));
    }
}
