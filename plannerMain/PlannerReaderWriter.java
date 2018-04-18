/**
 * 
 */
package plannerMain;

import java.io.*;
import java.util.*;

/**
 * @author Kaveh Vossoughi
 *
 */
public final class PlannerReaderWriter {

	private String filePath;
	// Map map;

	public PlannerReaderWriter(String filePath) {
		this.filePath = filePath;
		// map = new HashMap<Calendar, ArrayList<Entry>>();
	}
	
	public String getRecords(int dayOffset) {
		String stringToDisplay = "";
		int totalHours = 0;
        CustomDate today = new CustomDate();
		File file = new File(filePath);
		
		try(Scanner scan = new Scanner(file)) {
			scan.nextLine();
			while (scan.hasNextLine()) {
				String subject = scan.next();
				long startDateSeconds = scan.nextLong();
				long endDateSeconds = scan.nextLong();
				
				CustomDate thisDay = new CustomDate(startDateSeconds);
				//System.out.println(thisDay.dayDifference(today));
                if (CustomDate.dayDifference(thisDay, today) == dayOffset) {
                	int duration = (int) ((endDateSeconds - startDateSeconds) / 1000l);
    				totalHours += duration;
    				stringToDisplay += subject + " " + secondsToDate(duration) + "\n";
                }
							
			}	
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		stringToDisplay += "TOTAL --> " + secondsToDate(totalHours);
		
		return stringToDisplay;
	}
	
	public void writeStart(Entry entry) {
		try(FileWriter writer = new FileWriter(filePath, true)) {
			String entryString = "\n" + entry.getSubject() + " " + entry.getStartDate() + " ";
			writer.write(entryString);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public void writeEnd(Entry entry) {
		try(FileWriter writer = new FileWriter(filePath, true)) {
			String entryString = entry.getEndDate() + "";
			writer.write(entryString);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private String secondsToDate(int duration) {
		int remainder = duration / 60;
		int numberOfSeconds= duration % 60;
		int numberOfHours = remainder / 60;
		int numberOfMinutes = remainder % 60;
		return numberOfHours + ":" + numberOfMinutes + ":" + numberOfSeconds;
	}
}
