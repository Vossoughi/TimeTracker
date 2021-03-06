/**
 * 
 */
package plannerMain;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Kaveh Vossoughi
 *
 */
public class Entry {
	
	Date time;
	String subject;
    String note;

	// Constructors
	public Entry(String subject, String note) {
		this.subject = subject;
		this.note = note;
	}
	
	public Entry(String subject) {
		this(subject, "");
	}
	
	// Accessors and Mutators	
	public String getSubject() {
		return subject;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setSubject(String newSubject) {
		subject = newSubject;
	}
	
	public void setNote(String newNote) {
		note = newNote;
	}
	
	public long getDate() {
		CustomDate now = new CustomDate();
		return now.getTime();
	}
	
	public boolean hasDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return cal.get(Calendar.DAY_OF_MONTH) == day && cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year;
	}

}
