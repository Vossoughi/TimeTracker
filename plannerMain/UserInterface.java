/**
 * 
 */
package plannerMain;

import java.io.File;
import java.text.*;
import java.util.*;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
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
	int dayOffset = 0;
	boolean isHistoryShown = false;
	boolean isRecording = false;
	String timeSpent = "00:00:00";
	TimeZone tz = TimeZone.getTimeZone("GMT");
	Timeline timeline;

	Button btnStart = new Button("Start");
	Button btnStop = new Button("Stop");
	TextField tf = new TextField("");
	TextArea ta = new TextArea();
	Button btnPrevious = new Button("Previous");
	Button btnShowHide = new Button("Show/Hide");
	Button btnChooseFile = new Button("Choose file");
	Button btnNext = new Button("Next");
	Label statusLabel = new Label();
	Label errorLabel;
	
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setResizable(false);
		ta.setEditable(false);
		PlannerReaderWriter reader = new PlannerReaderWriter();

		btnStart.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (tf.getText().replaceAll("\\s","").equals("")) { return; }
				entry = new Entry(tf.getText().replaceAll("\\s",""));
				reader.writeStart(entry);
				setDisplayHistoryDisabled(true);
				isHistoryShown = false;
				isRecording = true;
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
				            statusLabel.setText("Recording...\n" + timeSpent);
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
				isRecording = false;
				statusLabel.setText("Stopped\n" + timeSpent);
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
					popUpWindow("File Error", "The file format is incorrect", false);
				} else {
				ta.setText(isHistoryShown ? "" : reader.getRecords(dayOffset));
				isHistoryShown = !isHistoryShown;
				}
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
		
		btnChooseFile.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					reader.setFilePath(file.getAbsolutePath(), true);
				}		
			}
		});

		HBox topPane = new HBox(10, btnStart, tf, btnStop, statusLabel);
		topPane.setMinHeight(30);
		HBox bottomPane = new HBox(30, btnPrevious, btnChooseFile, btnShowHide, btnNext);
		VBox pane = new VBox(10, topPane, ta, bottomPane);

		Scene scene = new Scene(pane, 380, 250);

		primaryStage.setTitle("Planner");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event) {
				if (entry != null && isRecording) { reader.writeEnd(entry); }
			}
		});
	}
	
	private void setDisplayHistoryDisabled(boolean setOn) {
		btnPrevious.setDisable(setOn);
		btnNext.setDisable(setOn);
		btnChooseFile.setDisable(setOn);
		btnShowHide.setDisable(setOn);
	}
	
	private void popUpWindow(String title, String message, boolean fatal) {
		errorLabel = new Label(message);
		Button btnClose = new Button("Close");

		VBox topPane = new VBox(10, errorLabel, btnClose);
		topPane.setAlignment(Pos.BASELINE_CENTER);

		Scene secondScene = new Scene(topPane, 230, 100);

		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("title");
		newWindow.setScene(secondScene);
		
		btnClose.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (fatal) { 
					System.exit(1); 
				} else {
					newWindow.close();
				};
			}
		});

		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event) {
				if (fatal) { 
					System.exit(1); 
				} else {
					newWindow.close();
				}
			}
		});
		newWindow.showAndWait();
		
		// Set position of second window, related to primary window.
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		newWindow.setX((primScreenBounds.getWidth() - newWindow.getWidth()) / 2); 
		newWindow.setY((primScreenBounds.getHeight() - newWindow.getHeight()) / 4);

		// Another approach
		// newWindow.setX(stage.getX() + 200);
		// newWindow.setY(stage.getY() + 100);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
	
}