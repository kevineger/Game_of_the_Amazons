import java.util.Timer;
import java.util.Date;
/**
 * Game timer should be created per game for the entire duration of a game
 * it holds a timer object and number of time violations per game
 * @author TCulos
 *
 */
public class GameTimer {
	//static variable and object so one could reference them in TimeOutTask.Run()
	static int violations = 0;
	static Timer clock = new Timer();
	
		/**
		 * starts a timer for 30 seconds and runs TimeOutTask when done should
		 * be called whenever its our turn defaulted to 30 second timer
		 */
	public void StartClock(){
		TimeOutTask task = new TimeOutTask();
		Date date = new Date();
		
		//Used deprecated data type because Timer.schedule requires a date object not a calendar
		date.setSeconds(date.getSeconds() +25);
		clock.schedule(task, date);
	}
	
}