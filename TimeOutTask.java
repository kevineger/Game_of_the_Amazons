import java.util.TimerTask;
/**
 * class that will run a task when the timer reaches its time
 * @author TCulos
 *
 */
public class TimeOutTask extends TimerTask {
    String msg = "";


    /**
     * whenever player(us) goes over time increments the number of violations
     * as well as whatever other actions are necessary
     */
    public void run() {
        //call static GameTimer objects because could not give run a return value
        GameTimer.violations++;
        System.out.println("Time Violation: "+ GameTimer.violations);
    }
}