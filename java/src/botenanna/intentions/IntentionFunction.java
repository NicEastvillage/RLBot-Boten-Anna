package botenanna.intentions;

import botenanna.game.Situation;

public interface IntentionFunction {

    /** Used to calculate the intention value; the amount of work expected through a situation. */
    double compute(Situation situation, double timeSpent);

    /** Checks if the deviations are fulfilled. */
    boolean isDeviationFulfilled(Situation situation, double timeSpent);
}
