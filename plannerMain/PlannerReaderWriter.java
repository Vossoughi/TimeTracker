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

	final private String DEFAULT_FILE_PATH = "planner.txt";
	final private String CONFIG_FILE = ".config";
	String filePath;

	public PlannerReaderWriter(String filePath) {
		this.filePath = filePath;
	}
	
	public PlannerReaderWriter() {
		filePath = this.getFilePath();
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
		File file = new File(CONFIG_FILE);

		if (!file.exists()) {			
			try(FileWriter writer = new FileWriter(CONFIG_FILE, false)) {
				writer.write(DEFAULT_FILE_PATH);
			} catch (IOException e) {
				System.err.println(e.getMessage());	
			}
			return DEFAULT_FILE_PATH;
		}
		
		try (Scanner scan = new Scanner(new File(CONFIG_FILE))) {
			return scan.nextLine();	
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return DEFAULT_FILE_PATH;
		}
	}
	
	public String getRecords(int dayOffset, boolean isCategorized) {
		String stringToDisplay = "";
		String subject = "";
		int dayTotal = 0;		
		HashMap<String, Integer> entriesMap = new HashMap<>();
        CustomDate today = new CustomDate();
		
		try (Scanner scan = new Scanner(new File(filePath))) {
			scan.nextLine();			
			while (scan.hasNextLine()) {
				ArrayList<Long> timeEntries = new ArrayList<>();
				int entryTotal = 0;
				try(Scanner scanLine = new Scanner(scan.nextLine())) {
					subject = scanLine.next();
					while (scanLine.hasNextLong()) {
						timeEntries.add(scanLine.nextLong());
					}
				}
				if (timeEntries.size() % 2 == 1) { timeEntries.remove(timeEntries.size() - 1); }
				
				CustomDate thisDay = new CustomDate(timeEntries.get(0));		
                if (CustomDate.dayDifference(thisDay, today) == dayOffset) {             	
    				for (int i = timeEntries.size() - 1; i >= 0; i -= 2) {
    					entryTotal += (timeEntries.get(i) - timeEntries.get(i - 1));
    				}
                	int duration = (int) (entryTotal / 1000l);
                	
                	// New
                	if (entriesMap.containsKey(subject)) {
    					entriesMap.replace(subject, entriesMap.get(subject) + duration);
    				} else {
    					entriesMap.put(subject, duration);
    				}
                	
                	if (!isCategorized) {
                		dayTotal += duration;
        				stringToDisplay += subject + " " + secondsToDate(duration) + "\n";
                	}
                	// *********
                }						
			}	
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		// New
		if (isCategorized) {
			for (Map.Entry<String, Integer> entry : entriesMap.entrySet()) {
				stringToDisplay += entry.getKey() + " " + secondsToDate(entry.getValue()) + "\n";
			    dayTotal += entry.getValue();		    
			}
		}
		// *********
		stringToDisplay += "TOTAL --> " + secondsToDate(dayTotal);
		
		return stringToDisplay;
	}
	
	public void writeDate(Entry entry, boolean isStart, boolean isEnd) {
		try(FileWriter writer = new FileWriter(filePath, true)) {
			String entryString = "";
			if (isStart) {
				entryString = "\n" + entry.getSubject() + " " + entry.getDate() + " ";
			} else if (isEnd) {
				entryString = entry.getDate() + "";
			} else {
				entryString = entry.getDate() + " ";
			}			
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
				try(Scanner scanLine = new Scanner(scan.nextLine())) {
					scanLine.next();
					while (scanLine.hasNext() && fileOK) {
						String timeEntry = scanLine.next();
						try {
							Long.parseLong(timeEntry);
						} catch (NumberFormatException e) {
							System.out.println(timeEntry);
							fileOK = false;
						}
					}
				}
			}	
		} catch (IOException e) {
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
