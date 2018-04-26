package botenanna.behaviortree.builder;

import botenanna.behaviortree.Node;
import botenanna.behaviortree.guards.*;
import botenanna.behaviortree.intentions.IntentionCollectBoost;
import botenanna.behaviortree.intentions.IntentionDefendGoal;
import botenanna.behaviortree.intentions.IntentionShootTowardsGoal;
import botenanna.behaviortree.intentions.IntentionTouchBall;
import botenanna.behaviortree.tasks.*;
import botenanna.behaviortree.composites.*;
import botenanna.behaviortree.decorators.*;

public class NodeLibrary {
    public static Node nodeFromString(String nodeName, String[] arguments) {
        if (nodeName.length() >= 4 && nodeName.substring(0, 4).equals("Task")) {
            // Tasks
            switch (nodeName) {
                case "TaskGoForwards":
                    return new TaskGoForwards(arguments);
                case "TaskGoTowardsPoint":
                    return new TaskGoTowardsPoint(arguments);
                case "TaskDashForward":
                    return new TaskDashForward(arguments);
                case "TaskAdjustAirRotation":
                    return new TaskAdjustAirRotation(arguments);
                case "TaskHitTowardsPoint":
                    return new TaskHitTowardsPoint(arguments);
                case "TaskShootAtGoal":
                    return new TaskShootAtGoal(arguments);
            }
        } else if (nodeName.length() >= 9 && nodeName.substring(0, 9).equals("Intention")) {
            // Intentions
            switch (nodeName) {
                case "IntentionDefendGoal": return new IntentionDefendGoal(arguments);
                case "IntentionCollectBoost": return new IntentionCollectBoost(arguments);
                case "IntentionTouchBall": return new IntentionTouchBall(arguments);
                case "IntentionShootTowardsGoal": return new IntentionShootTowardsGoal(arguments);
            }
        } else if (nodeName.length() >= 5 && nodeName.substring(0, 5).equals("Guard")) {
            // Guards
            switch (nodeName) {
                case "GuardHasBoost": return new GuardHasBoost(arguments);
                case "GuardIsBallOnMyHalf": return new GuardIsBallOnMyHalf(arguments);
                case "GuardIsDistanceLessThan": return new GuardIsDistanceLessThan(arguments);
                case "GuardIsDoubleLessThan": return new GuardIsDoubleLessThan(arguments);
                case "GuardIsKickoff": return new GuardIsKickoff(arguments);
                case "GuardIsMidAir": return new GuardIsMidAir(arguments);
                case "GuardIntercept": return new GuardIntercept(arguments);
                case "GuardCloserThan": return  new GuardCloserThan(arguments);
                case "GuardHasGoalOpportunity": return new GuardHasGoalOpportunity(arguments);
                case "GuardIsPointBehind": return new GuardIsPointBehind(arguments);
                case "GuardHasBallPossession": return new GuardHasBallPossession(arguments);
                case "GuardIsDistanceLessThanDistance": return new GuardIsDistanceLessThanDistance(arguments);
                case "GuardWillBallHitGoal": return new GuardWillBallHitGoal(arguments);
                case "GuardIsPointInsideBox": return new GuardIsPointInsideBox(arguments);
                case "GuardIsBallNearWall": return new GuardIsBallNearWall(arguments);
                case "GuardBallMidAir": return new GuardBallMidAir(arguments);
            }
        } else {
            // Other nodes
            switch (nodeName) {
                case "Selector": return new Selector();
                case "Sequencer": return new Sequencer();
                case "Inverter": return new Inverter();
            }
        }
        throw new BehaviourTreeUnknownNodeException("Could not recognize the node \"" + nodeName + "\".");
    }
}
