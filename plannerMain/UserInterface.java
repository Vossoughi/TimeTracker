package plannerMain;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class UserInterface extends Application {

	Entry entry = null;

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
				entry = new Entry(tf.getText());
				writeStart(entry);
			}
		});
		
		btnShowHide.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				ta.setText(resultToString());
			}
		});

		btnNext.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				writeEnd(entry);
				tf.setText("");
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
		Calendar cal = Calendar.getInstance();
		String str = "";
		int total = 0;
		// Map map = new HashMap<Calendar, ArrayList<Entry>>();
		// TODO 
		File file = new File("text.txt");
		try(Scanner scan = new Scanner(file)) {
			scan.nextLine();
			
			while (scan.hasNextLine()) {
				String subject = scan.next();
				long startDate = scan.nextLong();
				long endDate = scan.nextLong();
				
				int duration = (int) ((endDate - startDate) / 1000l);
				total += duration;
				str += subject + " " + secondsToDate(duration) + "\n";
				//System.out.println("Hours: " + numberOfHours + " - " + "Minutes: " + numberOfMinutes + " - " + "Seconds: " + numberOfSeconds);
				
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
	
	private static int getMonth(Calendar cal) {
		return cal.get(Calendar.MONTH);
	}
	
	private static int getDayOfMonth(Calendar cal) {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
