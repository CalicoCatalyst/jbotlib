/**
 * (#)TimeUtils.java - Version 1.0
 * 
 */
package net.insxnity.jbotlib.util;

/**
 * TimeUtils is a class that eases checking of dates while using reddit bots
 * 
 * @author Insxnity
 */
public class TimeUtils {
	
	public static final long SECOND = 1000;
	
	public static final long MINUTE = SECOND * 60;
	
	public static final long HOUR = MINUTE * 60;
	
	public static final long DAY = HOUR * 24;
	
	public static final long WEEK = DAY * 7;
	
	public static final long FORTNIGHT = WEEK * 2;
	
	/**
	 * Approximated to 30 days
	 */
	public static final long MONTH = DAY * 30;
	
	/**
	 * Approximated to 365 days
	 */
	public static final long YEAR = DAY * 365;
	
	/**
	 * Adds a day for leap year.
	 */
	public static final long FOURYEAR = (YEAR * 4) + DAY;
	
	/**
	 * Check if <code>timeToCheck</code> (in millis) was in the last <code>period</code>
	 * 
	 * @param period - Unit of time before now 
	 * @param timeToCheck - Time of the thing we're checking
	 * @return true if <code>timeToCheck</code> occured within a <code>period</code> ago
	 */
	public static boolean inLastDay(long period, long timeToCheck) {
	    return timeToCheck > System.currentTimeMillis()-period;
	}
	
	
}
