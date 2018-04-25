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

	final String DEFAULT_FILE_PATH = "test.txt";
	final String CONFIG_FILE = ".config";
	private String filePath;

	public PlannerReaderWriter(String filePath) {
		this.filePath = filePath;
	}
	
	public PlannerReaderWriter() {
		filePath = this.getFilePath();
		if (filePath == null) {
			filePath = DEFAULT_FILE_PATH;
		}
	}
	
	public void setFilePath(String filePath, boolean isPermanent) {
		this.filePath = filePath;
		if (isPermanent) {
			try(FileWriter writer = new FileWriter(this.filePath, true)) {
				writer.write(filePath);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public String getFilePath() {
		try (Scanner scan = new Scanner(new File(CONFIG_FILE))) {
			return scan.nextLine();	
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public String getRecords(int dayOffset) {
		String stringToDisplay = "";
		int totalHours = 0;
        CustomDate today = new CustomDate();
		
		try (Scanner scan = new Scanner(new File(filePath))) {
			scan.nextLine();
			while (scan.hasNextLine()) {
				String subject = scan.next();
				long startDateSeconds = scan.nextLong();
				long endDateSeconds = scan.nextLong();
				
				CustomDate thisDay = new CustomDate(startDateSeconds);		
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
	
	public boolean checkFile() {
		boolean fileOK = true;
		File file = new File(filePath);
		try(Scanner scan = new Scanner(file)) {
			scan.nextLine();
			while (scan.hasNextLine() && fileOK) {
				scan.next();
				String startTime = scan.next();
				String endTime = scan.next();
				try {
					Long.parseLong(startTime);
					Long.parseLong(endTime);
				} catch (NumberFormatException e) {
					fileOK = false;
				}
			}	
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		return fileOK;
	}
	
	private String secondsToDate(int duration) {
		int remainder = duration / 60;
		int numberOfSeconds= duration % 60;
		int numberOfHours = remainder / 60;
		int numberOfMinutes = remainder % 60;
		return numberOfHours + ":" + numberOfMinutes + ":" + numberOfSeconds;
	}
}
