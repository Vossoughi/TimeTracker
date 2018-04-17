/**
 * 
 */
package plannerMain;

import java.util.*;

/**
 * @author Kaveh Vossoughi
 *
 */
public final class CustomDate extends Date {
	
	Calendar cal = Calendar.getInstance();
	
	public CustomDate() {
		super();
		cal.setTime(this);
	}
	
	public CustomDate(long milliseconds) {
		super(milliseconds);
	}
	
	public int getYear() {	
		return cal.get(Calendar.YEAR);
	}
	
	public int getMonth() {	
		return cal.get(Calendar.MONTH);
	}
	
	public int getDayOfMonth() {		
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int dayDifference(Date date, Date otherDate) {
		Calendar otherCal2 = Calendar.getInstance();
		otherCal2.setTime(otherDate);
		Calendar otherCal = Calendar.getInstance();
		otherCal.setTime(date);
		return otherCal.get(Calendar.DAY_OF_MONTH) - otherCal2.get(Calendar.DAY_OF_MONTH);				
	}

}
