package botenanna.fitness;

import botenanna.AgentInput;

public interface FitnessInterface {

    /** Used to calculate the fitness value. */
    double calculateFitness(AgentInput situation, double timeSpent);

    /** Checks if the deviations are fulfilled. */
    boolean isDeviationFulfilled(AgentInput situation, double timeSpent);
}
