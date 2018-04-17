package plannerMain;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public final class UserInterface extends Application {

	Entry entry = null;
	static int dayOffset = 0;
	static boolean isShown = false;

	@Override
	public void start(Stage primaryStage) {

		Button btnStart = new Button("Start");
		Button btnStop = new Button("Stop");
		TextField tf = new TextField("");
		TextArea ta = new TextArea();
		Button btnPrevious = new Button("Previous");
		Button btnShowHide = new Button("Show/Hide");
		Button btnNext = new Button("Next");
		ta.setEditable(false);

		btnStart.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				entry = new Entry(tf.getText());
				writeStart(entry);
			}
		});

		btnStop.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				writeEnd(entry);
				tf.setText("");
			}
		});
		
		btnPrevious.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isShown) { 
					dayOffset--;
					ta.setText(resultToString()); 
				}
			}
		});
		
		btnShowHide.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				ta.setText(isShown ? "" : resultToString());
				isShown = !isShown;
			}
		});

		btnNext.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isShown) { 
					dayOffset++;
					ta.setText(resultToString()); 
				}
			}
		});

		HBox topPane = new HBox(10, btnStart, tf, btnStop);
		HBox bottomPane = new HBox(48, btnPrevious, btnShowHide, btnNext);
		VBox pane = new VBox(10, topPane, ta, bottomPane);
		// StackPane root = new StackPane();
		// root.getChildren().addAll(btnStart, btnStop);

		Scene scene = new Scene(pane, 300, 250);

		primaryStage.setTitle("Planner");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private static void writeStart(Entry entry) {
		try(FileWriter writer = new FileWriter("text.txt", true)) {
			String entryString = "\n" + entry.getSubject() + " " + entry.getStartDate() + " ";
			writer.write(entryString);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void writeEnd(Entry entry) {
		try(FileWriter writer = new FileWriter("text.txt", true)) {
			String entryString = entry.getEndDate() + "";
			writer.write(entryString);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private static String resultToString() {
		
		String str = "";
		int total = 0;
		// Map map = new HashMap<Calendar, ArrayList<Entry>>();
		// TODO 
		File file = new File("text.txt");
		CustomDate today = new CustomDate();
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
    				total += duration;
    				str += subject + " " + secondsToDate(duration) + "\n";
                }
							
			}
						
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
		str += "TOTAL --> " + secondsToDate(total);
		
		return str;
	}
	
	private static String secondsToDate(int duration) {
		int remainder = duration / 60;
		int numberOfSeconds= duration % 60;
		int numberOfHours = remainder / 60;
		int numberOfMinutes = remainder % 60;
		return numberOfHours + ":" + numberOfMinutes + ":" + numberOfSeconds;
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
