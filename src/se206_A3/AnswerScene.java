package se206_A3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AnswerScene {
	
	private Stage _primary;
	private Scene _menu;
	private String[] _catNames;
	private Button _click;
	private String _category;
	private int _lineNum, _counter, _value;
	private final DropShadow shadow = new DropShadow();

	public AnswerScene(Button click, String category, int lineNum, Stage primary,
			String[] catNames, Scene menu) {
		_click = click;
		_category = category;
		_lineNum = lineNum;
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}

	public void startScene() {
		
		shadow.setColor(Color.web("#7f96eb"));
		
		GameScene game = new GameScene(_catNames, _primary, _menu);
		File winFile = new File("./saves/winnings");

		// Get the value
		_value = Integer.parseInt(_click.getText());

		Button btnEnter = new Button("Enter");
		Button dkBtn = new Button("Don't know");
		Button replay = new Button("Replay");
		btnEnter.setEffect(shadow);
		dkBtn.setEffect(shadow);
		replay.setEffect(shadow);

		// Get the question and clue
		String question = getType("question");
		String text = getType("clue");
		runTts(question);

		// Create a slider for tts speed
		Slider slider = new Slider();

		// Set the sliders min, max and val
		slider.setMin(0.5);
		slider.setMax(2);
		slider.setValue(1);

		// Set increment
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(0.25);


		// Start the timer
		Timer timer = new Timer();
		_counter = 45;
		Label timeLeft = new Label("45 seconds left to answer");
		TimerTask task = new TimerTask()
		{
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timeLeft.setText(Integer.toString(_counter) + " seconds left to answer");
						_counter--;
						if(_counter == -1) {
							// Get the answer
							timer.cancel();
							String answer = getType("answer");
							runTts("Answer was " + answer);
							
							Alert a = new Alert(AlertType.CONFIRMATION);
							a.setTitle("Time is up");
							a.setHeaderText("The correct answer was " + answer);
							a.showAndWait();
							
							update(_category, _lineNum);
							game.startScene();
						}
					}
				});
			}
		};
		timer.scheduleAtFixedRate(task, 1000,1000);

		// Allow user to enter their answer
		TextField txtInput = new TextField();
		// Check if entered answer is correct
		btnEnter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// Read money value from file
				BufferedReader win = null;
				int money = 0;
				try {
					win = new BufferedReader(new FileReader(winFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				String moneyPool = null;
				try {
					moneyPool = win.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				money = Integer.parseInt(moneyPool);

				String answer = getType("answer");
				Alert a = new Alert(AlertType.CONFIRMATION);

				// Check if answer has multiple correct answers
				String[] answers = null;
				if (answer.indexOf('/') != -1) {
					answers = answer.split("\\/+");
					// Convert all string to lower case
					for (int i = 0; i < answers.length; i++) {
						answers[i] = answers[i].toLowerCase();
					}
				}
				else {
					answers = new String[1];
					answers[0] = answer.toLowerCase();
				}

				if (Arrays.asList(answers).contains(txtInput.getText().toLowerCase())) {
					a.setTitle("Correct");
					// tts correct
					runTts("Correct");

					a.setHeaderText("Your answer was correct");
					money += _value;
				}
				else {
					a.setTitle("Incorrect");
					// tts the answer
					runTts("Answer was " + answer);
					
					a.setHeaderText("The correct answer was " + answer);
				}

				// Write new money value to file
				BufferedWriter out = null;
				try {
					out = new BufferedWriter(new FileWriter(winFile));
					out.write("" + money);
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				update(_category, _lineNum);
				timer.cancel();
				a.showAndWait();
				
				game.startScene();
			}	
		});

		dkBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String answer = getType("answer");
				runTts("Answer was " + answer);
				
				Alert a = new Alert(AlertType.CONFIRMATION);
				a.setTitle("Answer");
				a.setHeaderText("The correct answer was " + answer);
				// tts the answer

				update(_category, _lineNum);
				timer.cancel();
				a.showAndWait();
				
				game.startScene();
			}
		});

		// Replay the question
		replay.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent arg0) {
				// Get the slider value
				double speed = slider.getValue();
				
				// Returns the question portion
				String question = getType("question");
				
				// tts the question and speed
				HelperThread ttsQ = new HelperThread(question, speed);
				ttsQ.start();
			}
		});

		// Layout of the answer scene where user gets to input answer to question
		VBox layout = new VBox(30);
		layout.setAlignment(Pos.BASELINE_CENTER);
		layout.setPadding(new Insets(80));
		Label clue = new Label("Clue: " + text + "...");
		clue.setFont(new Font(15));
		clue.setMinWidth(Region.USE_PREF_SIZE);
		Label info = new Label("Adjust question speed (default is 1)");

		TilePane tileBtns = new TilePane(Orientation.HORIZONTAL);
		tileBtns.getChildren().addAll(btnEnter, dkBtn, replay);


		layout.getChildren().addAll(clue, txtInput, tileBtns, slider, info, timeLeft);
		
		Scene answer = new Scene(layout, 500, 450);
		_primary.setScene(answer);
		_primary.show();
	}
	
	/*
	 * Method updates the save files so that questions which have been answered are removed
	 */
	private void update(String cateFile, int lineRemove) {
		UpdateCategory cate = new UpdateCategory(cateFile, lineRemove);
		cate.update();
	}
	
	/*
	 * Method to run the tts
	 */
	private void runTts(String type) {
		HelperThread tts = new HelperThread(type);
		tts.start();
	}
	
	/*
	 * This method returns the question,answer or clue portion
	 */
	private String getType(String type) {
		String line = null;
		String msg = null;
		try {
			line = Files.readAllLines(Paths.get("./saves/"+_category)).get(_lineNum);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg = line.substring(line.indexOf("|") + 1);
		
		if (type.equals("answer")) {
			msg = msg.substring(msg.indexOf("|") + 1);
			msg = msg.substring(msg.indexOf("|") + 1);
		}
		else if( type.equals("clue")) {
			msg = msg.substring(msg.indexOf("|") + 1);
			msg = msg.substring(0, msg.indexOf("|"));
		}
		else if(type.equals("question")) {
			msg = msg.substring(0, msg.indexOf("|"));
		}
		else {
			msg = "Error has occurred";
		}
		
		return msg;
	}
}
