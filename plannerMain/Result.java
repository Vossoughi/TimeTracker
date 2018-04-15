package plannerMain;

import java.io.*;
import java.util.*;

public class Result {

	public static void main(String[] args) {
		
		Calendar cal = Calendar.getInstance();
		// Map map = new HashMap<Calendar, ArrayList<Entry>>();
		// TODO 
		File file = new File("text.txt");
		try(Scanner scan = new Scanner(file)) {
			scan.nextLine();
			
			while (scan.hasNextLine()) {
				scan.next();
				long startDate = scan.nextLong();
				long endDate = scan.nextLong();
				
				int duration = (int) ((endDate - startDate) / 1000l);
				int remainder = duration / 60;
				int numberOfSeconds= duration % 60;
				int numberOfHours = remainder / 60;
				int numberOfMinutes = remainder % 60;
				System.out.println("Hours: " + numberOfHours + " - " + "Minutes: " + numberOfMinutes + " - " + "Seconds: " + numberOfSeconds);
				
				Date date = new Date(startDate);
				cal.setTime(date);
				int month = getMonth(cal);
				System.out.println("Month: " + month);
				
				int dayOfMonth = getDayOfMonth(cal);
				System.out.println("Day of Month: " + dayOfMonth);
			}
			
			
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		
		
	}
	
	private static int getMonth(Calendar cal) {
		return cal.get(Calendar.MONTH);
	}
	
	private static int getDayOfMonth(Calendar cal) {
		return cal.get(Calendar.DAY_OF_MONTH);
	}
}
