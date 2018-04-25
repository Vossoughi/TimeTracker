/**
 * 
 */
package plannerMain;

import java.text.*;
import java.util.*;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Rectangle2D;
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
	static final String FILE_PATH = "test.txt";
	
	Entry entry = null;
	int dayOffset = 0;
	boolean isHistoryShown = false;
	String timeSpent = "00:00:00";
	TimeZone tz = TimeZone.getTimeZone("GMT");
	Timeline timeline;

	Button btnStart = new Button("Start");
	Button btnStop = new Button("Stop");
	TextField tf = new TextField("");
	TextArea ta = new TextArea();
	Button btnPrevious = new Button("Previous");
	Button btnShowHide = new Button("Show/Hide");
	Button btnNext = new Button("Next");
	Label errorLabel;
	
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setResizable(false);
		ta.setEditable(false);
		PlannerReaderWriter reader = new PlannerReaderWriter(FILE_PATH);

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
				
				long start = System.currentTimeMillis();
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				timeFormat.setTimeZone(tz);
				timeline = new Timeline(
				    new KeyFrame(
				        Duration.millis(500.0),
				        innerEvent -> {
				            final long diff = System.currentTimeMillis() - start;
				            timeSpent = timeFormat.format(diff);
				            ta.setText("Recording...\n" + timeSpent);
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
				ta.setText("Stopped\n" + timeSpent);
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
				if(!reader.checkFile()) {
					popUpErrorWindow("The file format is incorrect", primaryStage);
				}
				
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

		Scene scene = new Scene(pane, 300, 250);

		primaryStage.setTitle("Planner");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event) {
				if (entry != null) { reader.writeEnd(entry); }
			}
		});
	}
	
	private void setDisplayHistoryDisabled(boolean setOn) {
		btnPrevious.setDisable(setOn);
		btnNext.setDisable(setOn);
		btnShowHide.setDisable(setOn);
	}
	
	private void popUpErrorWindow(String message, Stage stage) {
		errorLabel = new Label(message);

		StackPane secondaryLayout = new StackPane();
		secondaryLayout.getChildren().add(errorLabel);

		Scene secondScene = new Scene(secondaryLayout, 230, 100);

		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("File Error");
		newWindow.setScene(secondScene);

		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event) {
				System.exit(1);
			}
		});
		newWindow.showAndWait();
		
		// Set position of second window, related to primary window.
				//Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
				//newWindow.setX((primScreenBounds.getWidth() - newWindow.getWidth()) / 2); 
				//newWindow.setY((primScreenBounds.getHeight() - newWindow.getHeight()) / 4);
				//System.out.println(primScreenBounds.getWidth() - newWindow.getWidth() / 2);
				newWindow.setX(stage.getX() + 200);
			    newWindow.setY(stage.getY() + 100);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
	
}