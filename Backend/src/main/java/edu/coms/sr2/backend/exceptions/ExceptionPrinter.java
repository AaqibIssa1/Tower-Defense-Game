package edu.coms.sr2.backend.exceptions;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Class to handle exception-stacktrace printing. Functionality includes reduced console clutter
 * via filtering out repeat exceptions
 * @author Nathan
 *
 */

public class ExceptionPrinter {
	
	private static HashMap<String, Integer> repeatCounter = new HashMap<>();
	
	private static Timer timer = new Timer();
	
	public static final int printLimit = 6;

	/**
	 * Accepts and prints an exception (if applicable)
	 * @param e The exception to print
	 */
	public static void accept(Exception e) {
		String message = e.getMessage();
		Integer count = repeatCounter.computeIfAbsent(message, key->0);
		if(count == 0)
		{
			e.printStackTrace();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					repeatCounter.put(message, 0);
				}
				
			}, 3000l);
		} else {
			if(count < printLimit)
				System.out.println("Skipping repeat exception: " + e.getClass().getSimpleName());
		}
		repeatCounter.put(message, count + 1);
	}
}
