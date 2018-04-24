/**
 * 
 */
package plannerMain;

import java.text.*;
import java.util.*;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

/**
 * @author Kaveh Vossoughi
 *
 */
public final class UserInterface extends Application {

	Entry entry = null;
	static int dayOffset = 0;
	static boolean isHistoryShown = false;
	static final String FILEPATH = "test.txt";
	Timeline timeline;

	Button btnStart = new Button("Start");
	Button btnStop = new Button("Stop");
	TextField tf = new TextField("");
	TextArea ta = new TextArea();
	Button btnPrevious = new Button("Previous");
	Button btnShowHide = new Button("Show/Hide");
	Button btnNext = new Button("Next");
	
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setResizable(false);
		ta.setEditable(false);
		PlannerReaderWriter reader = new PlannerReaderWriter(FILEPATH);

		btnStart.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				entry = new Entry(tf.getText());
				reader.writeStart(entry);
				setDisplayHistoryDisabled(true);
				ta.setText("Recording...");
				isHistoryShown = false;
				tf.setEditable(false);
				
				Date a = new Date();
				long start = a.getTime();
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				timeline = new Timeline(
				    new KeyFrame(
				        Duration.millis(500),
				        innerEvent -> {
				            final long diff = System.currentTimeMillis() - start;
				                // System.out.println(System.currentTimeMillis() - start );
				                ta.setText("Recording...\n" + timeFormat.format(diff));
				        }
				    )
				);
				timeline.setCycleCount(Animation.INDEFINITE);
				timeline.play();
			}
		});

		btnStop.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				reader.writeEnd(entry);
				tf.setText("");
				tf.setEditable(true);
				setDisplayHistoryDisabled(false);
				ta.setText("");
				timeline.stop();
			}
		});
		
		btnPrevious.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isHistoryShown) { ta.setText(reader.getRecords(--dayOffset)); }
			}
		});
		
		btnShowHide.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				ta.setText(isHistoryShown ? "" : reader.getRecords(dayOffset));
				isHistoryShown = !isHistoryShown;
			}
		});

		btnNext.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isHistoryShown) { ta.setText(reader.getRecords(++dayOffset)); }
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
	
	private void setDisplayHistoryDisabled(boolean setOn) {
		btnPrevious.setDisable(setOn);
		btnNext.setDisable(setOn);
		btnShowHide.setDisable(setOn);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}