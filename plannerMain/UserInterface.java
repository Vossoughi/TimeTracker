/**
 * 
 */
package plannerMain;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * @author Kaveh Vossoughi
 *
 */
public final class UserInterface extends Application {

	Entry entry = null;
	static int dayOffset = 0;
	static boolean isShown = false;
	static final String FILEPATH = "test.txt";

	Button btnStart = new Button("Start");
	Button btnStop = new Button("Stop");
	TextField tf = new TextField("");
	TextArea ta = new TextArea();
	Button btnPrevious = new Button("Previous");
	Button btnShowHide = new Button("Show/Hide");
	Button btnNext = new Button("Next");
	
	
	@Override
	public void start(Stage primaryStage) {
		
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
			}
		});

		btnStop.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				reader.writeEnd(entry);
				tf.setText("");
				setDisplayHistoryDisabled(false);
			}
		});
		
		btnPrevious.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isShown) { ta.setText(reader.getRecords(--dayOffset)); }
			}
		});
		
		btnShowHide.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				ta.setText(isShown ? "" : reader.getRecords(dayOffset));
				isShown = !isShown;
			}
		});

		btnNext.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				if (isShown) { ta.setText(reader.getRecords(++dayOffset)); }
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
