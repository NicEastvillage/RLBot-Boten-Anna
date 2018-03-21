package botenanna.physics;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//TODO make counter start at gameStart

/** This is used to track time (Under development). */
public class TimeTracker {

    private LocalDateTime gameTime; //Used to track time since GrpcService was started.
    private LocalDateTime stopWatch; //Used to track time for stopwatch.

    public TimeTracker() {
        this.gameTime = LocalDateTime.now();
        stopWatch = null;
    }

    /** DO NOT USE. Not formatted and therefor not really useful.
     *  Use this to get the time since the GrpcService was started.
     *  //TODO: Could be optimized, does kinda the same as getElapsedSecondsTimer()
     *  @return the time since the GrpcService was started. (Formatted with LocalDateTime)
     */
    private LocalDateTime getGameTime() {
        return gameTime;
    }

    public double getSecondsSinceServerStart(){
        LocalDateTime currentTime = LocalDateTime.now();
        long seconds = 0;

        try{
            seconds = gameTime.until(currentTime, ChronoUnit.MILLIS);
        } catch (NullPointerException e){
            System.out.println("Null Pointer ex"); //TODO EDIT
            return 0;
        }

        return seconds * 0.001;
    }

    /** Gets the time between now and when the method setTime() was called last.
     *  @return Seconds since last call of setTime(). (3 decimals 0.xxx) */
    public double getElapsedSecondsTimer(){
        LocalDateTime currentTime = LocalDateTime.now();
        long seconds = 0;

        try{
            seconds = stopWatch.until(currentTime, ChronoUnit.MILLIS);
        } catch (NullPointerException e){
            System.out.println("Null Pointer ex"); //TODO EDIT
            return 0;
        }

        return seconds * 0.001;
    }

    /** This starts the timer. */
    public void startTimer(){
        this.stopWatch = LocalDateTime.now();
    }
}
