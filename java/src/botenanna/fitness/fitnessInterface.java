package botenanna.fitness;

import botenanna.game.Situation;

public interface fitnessInterface {

    /** Used to calculate the fitness value. */
    double calculateFitness(Situation situation, double timeSpent);

    /** Checks if the deviations are fulfilled. */
    boolean isDeviationFulfilled(Situation situation, double timeSpent);
}
