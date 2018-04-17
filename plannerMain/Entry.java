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
	
	Date start;
	Date end;
	String subject;
    String note;

	// Constructors
	
	public Entry(String subject, String note) {
		this.subject = subject;
		this.note = note;
		start = new CustomDate();
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
	
	public long getStartDate() {
		return start.getTime();
	}
	
	public long getEndDate() {
		end = new CustomDate();
		return end.getTime();
	}
	
	public boolean hasDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		return cal.get(Calendar.DAY_OF_MONTH) == day && cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year;
	}

}
