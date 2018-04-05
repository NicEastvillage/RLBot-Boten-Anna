package botenanna.fitness;

import botenanna.AgentInput;

public interface fitnessInterface {

    /** Used to calculate the fitness value. */
    double calculateFitness(AgentInput situation, double timeSpent);

    /** Help method for calculateFitness. Calculates the fitness with a formula. */
    double calculateFormula(double timeSpent, double angScaled, double distScaled);

    /** Checks if the deviations are fulfilled. */
    boolean isDeviationFulfilled(AgentInput situation, double timeSpent);
}
