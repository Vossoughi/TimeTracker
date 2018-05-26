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
	boolean isPaused = false;
	boolean isCategorized = false;
	String timeSpent = "00:00:00";
	TimeZone tz = TimeZone.getTimeZone("GMT");
	Timeline timeline;

	Button btnStart = new Button("Start");
	Button btnStop = new Button("Stop");
	Button btnPause = new Button(" ❚❚ ");
	TextField tf = new TextField("");
	TextArea ta = new TextArea();
	Button btnPrevious = new Button("Previous");
	Button btnShowHide = new Button("Show/Hide");
	Button btnChooseFile = new Button("Choose file");
	Button btnCategorized = new Button("Categorized");
	Button btnNext = new Button("Next");
	Label statusLabel = new Label();
	Label errorLabel;
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setResizable(false);
		ta.setEditable(false);
		
		btnStop.setDisable(true);
		btnPause.setDisable(true);
		btnStart.setDefaultButton(true);
		PlannerReaderWriter reader = new PlannerReaderWriter();

		btnStart.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (tf.getText().replaceAll("\\s","").equals("")) { return; }
				entry = new Entry(tf.getText().replaceAll("\\s",""));
				reader.writeDate(entry, true, false);
				setUItoRecording(true);
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
				reader.writeDate(entry, false, true);
				
				tf.setText("");
				tf.setEditable(true);
				setUItoRecording(false);
				pauseRecording();
				isRecording = false;
				statusLabel.setText("Stopped\n" + timeSpent);
				timeline.stop();
			}
		});
		
		btnPause.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				// pauseRecording();
				if (isPaused) {
					btnPause.setText(" ❚❚ ");
					timeline.play();
				} else {
					btnPause.setText("Play");
					timeline.pause();
				}
				isPaused = !isPaused;
				reader.writeDate(entry, false, false);
			}
		});
		
		btnPrevious.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (isHistoryShown) { ta.setText(reader.getRecords(--dayOffset, isCategorized)); }
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
				ta.setText(isHistoryShown ? "" : reader.getRecords(dayOffset, isCategorized));
				isHistoryShown = !isHistoryShown;
				}
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
		
		btnCategorized.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (isCategorized) { 
					btnCategorized.setText("Categorized");
				} else {
					btnCategorized.setText("Uncategorized");
				}
				isCategorized = !isCategorized;
				ta.setText(isHistoryShown ? reader.getRecords(dayOffset, isCategorized) : "");
			}
		});
		
		btnNext.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isHistoryShown) { ta.setText(reader.getRecords(++dayOffset, isCategorized)); }
			}
		});

		HBox topPane = new HBox(10, btnStart, tf, btnStop, btnPause, statusLabel);
		topPane.setMinHeight(30);
		HBox bottomPane = new HBox(10, btnPrevious, btnChooseFile, btnShowHide, btnCategorized, btnNext);
		VBox pane = new VBox(10, topPane, ta, bottomPane);
		
		Scene scene = new Scene(pane, 450, 250);
		tf.requestFocus();
		
		primaryStage.setTitle("Planner");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event) {
				if (entry != null && isRecording) { reader.writeDate(entry, false, true); }
			}
		});
		System.out.println(reader.checkFile());
	}
	
	private void pauseRecording() {
	}
	
	private void setUItoRecording(boolean setOn) {
		btnStart.setDisable(setOn);
		btnStop.setDisable(!setOn);
		btnStart.setDefaultButton(!setOn);
		btnPause.setDefaultButton(setOn);
		btnPrevious.setDisable(setOn);
		btnNext.setDisable(setOn);
		btnChooseFile.setDisable(setOn);
		btnShowHide.setDisable(setOn);
		btnPause.setDisable(!setOn);
	}
	
	private void popUpWindow(String title, String message, boolean fatal) {
		errorLabel = new Label(message);
		Button btnClose = new Button("Close");

		VBox topPane = new VBox(10, errorLabel, new Label(), btnClose);
		topPane.setAlignment(Pos.BASELINE_CENTER);

		Scene secondScene = new Scene(topPane, 280, 100);

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