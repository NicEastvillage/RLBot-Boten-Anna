package tarehart.rlbot.tuning;

import tarehart.rlbot.Bot;

public class BotLog {

    private static StringBuilder blueLog = new StringBuilder();
    private static StringBuilder orangeLog = new StringBuilder();

    public static void println(String message, Bot.Team team) {
        getLog(team).append(message).append("\n");
        System.out.println(message);
    }

    private static StringBuilder getLog(Bot.Team team) {
        return team == Bot.Team.BLUE ? blueLog : orangeLog;
    }

    public static String collect(Bot.Team team) {
        StringBuilder log = getLog(team);
        String contents = log.toString();
        log.setLength(0);
        return contents;
    }
}
