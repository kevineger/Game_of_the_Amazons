import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

/**
 * Game timer should be created per game for the entire duration of a game
 * it holds a timer object and number of time violations per game
 * @author TCulos
 *
 */
public class GameTimer extends  Timer{
	//static variable and object so one could reference them in TimeOutTask.Run()
	static int violations = 0;
    private Long startTime;
    private TimeOutTask task = new TimeOutTask();
	
		/**
		 * starts a timer for 30 seconds and runs TimeOutTask when done should
		 * be called whenever its our turn defaulted to 30 second timer
		 */
	public void startClock(){
        this.startViolationTimer();
        startTime = System.currentTimeMillis();
	}

    public boolean validTime(){
        long currentTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
        if (currentTime > 25){
            return true;
        }
        return false;
    }

    public void startViolationTimer() {
        Date date = new Date();

        //Used deprecated data type because Timer.schedule requires a date object not a calendar
        date.setSeconds(date.getSeconds() + 30);
        this.schedule(task, date);
    }
	
}