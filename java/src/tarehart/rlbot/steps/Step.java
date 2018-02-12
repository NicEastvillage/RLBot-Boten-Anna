package tarehart.rlbot.steps;

import tarehart.rlbot.AgentInput;
import tarehart.rlbot.AgentOutput;

import java.util.Optional;

public interface Step {

    /**
     * Return the output you want to pass to the bot.
     * If you pass Optional.empty(), you are declaring yourself to be complete.
     */
    Optional<AgentOutput> getOutput(AgentInput input);

    /**
     * Return true if you know that you're complete without even looking at the input.
     */
    boolean isBlindlyComplete();

    void begin();

    // Describes very briefly what's going on, for UI display
    String getSituation();

    boolean canInterrupt();
}
