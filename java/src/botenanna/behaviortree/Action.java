package botenanna.behaviortree;

import botenanna.AgentOutput;
import rlbot.api.GameData;

public class Action {
    public final AgentOutput state;
    public final Task creator;

    public Action(AgentOutput output, Task creator) {
        this.state = output;
        this.creator = creator;
    }
}
