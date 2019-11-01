package CONTI.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
	

	  /**
	   * Sleeps for the given amount of time.
	   * 
	   * @param duration
	   *          duration
	   * @param unit
	   *          time unit
	   */
	  public static void sleep(long duration, TimeUnit unit) {
	    try {
	      unit.sleep(duration);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	  }

	  /**
	   * Converts given amount of time into milliseconds.
	   * 
	   * @param duration
	   *          duration amount
	   * @param unit
	   *          time unit
	   * @return result in milliseconds
	   */
	  public static long milliseconds(int duration, TimeUnit unit) {
	    return unit.toMillis(duration);
	  }

}
