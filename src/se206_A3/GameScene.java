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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameScene {

	private Stage _primary;
	private Scene _menu, _game, answerScene;
	private String[] _catNames;
	private Random _rnd;
	private int _monVal;
	private List<String> categories, lines, questions;
	private Button _valueBtn, _backBtn, btnClicked;

	public GameScene(String[] catNames, Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}

	public void startScene() {

		// Create lists
		categories = new ArrayList<String>();
		questions = new ArrayList<String>();
		lines = new ArrayList<String>();

		// Instantiate a random object
		_rnd = new Random();

		// Creates a directory for save files
		new File("./saves").mkdir();

		File saveDir = new File("./saves");
		File winFile = new File("./saves/winnings");
		String[] saveFiles = saveDir.list();

		// Preparation 
		if ( saveFiles.length < 1) {
			// Selects 5 random unique categories
			while (categories.size() < 5) {
				// Generates a random index number
				int index = _rnd.nextInt(_catNames.length);
				// Check if category has already been added
				if(!categories.contains(_catNames[index])) {
					categories.add(_catNames[index]);
				}
			}
			// Save the selected categories to a save folder
			for (int i = 0; i < categories.size(); i++) {
				// New text file inside saves for the category
				File savefile = new File("./saves/" + categories.get(i));
				// Text file inside categories folder
				File catefile = new File("./categories/" + categories.get(i));

				// Store all the lines from the category into a list
				try (BufferedReader value = new BufferedReader(new FileReader(catefile))) {
					String line;
					while ((line = value.readLine()) != null) {
						lines.add(line);
					}
				}catch (IOException e) {
					e.printStackTrace();
				}

				// Get 5 random questions from that list, write to the new file
				_monVal = 100;
				while (questions.size() < 5) {
					int rndLineIndex = _rnd.nextInt(lines.size());
					String line = lines.get(rndLineIndex);

					if(!questions.contains(line)) {
						// Write the line to the new file
						BufferedWriter out = null;
						try {
							int val = _monVal;
							savefile.createNewFile();
							// Appends the new lines to the file
							out = new BufferedWriter(new FileWriter(savefile, true));
							out.write(val + "|" + line);
							out.newLine();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						_monVal += 100;
						questions.add(line);
					}
				}
				questions.clear();
				lines.clear();
			}

			// Create a winnings file to store money earned
			BufferedWriter writer = null;
			try {
				winFile.createNewFile();
				writer = new BufferedWriter(new FileWriter(winFile));
				writer.write("" + 0);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			// If game is ongoing, add the category names to the list
			for (String category: saveFiles) {
				if(!category.equals("winnings")) {
					categories.add(category);
				}
			}
		}

		// Check if all sections have been completed
		int count = 0;
		for (int i = 0; i < categories.size(); i++) {
			File file = new File("./saves/"+ categories.get(i));
			if (file.length() == 0) {
				count++;
			}
		}
		// If the game has been completed, show an alert box to reset
		if(count == categories.size()) {
			// Start up the reward scene
			RewardScene reward = new RewardScene(_primary, _menu);
			reward.startScene();
			return;
		}

		TabPane tabs= new TabPane();
		// Tabs cannot be closed
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		// Create the tabs for the 5 categories
		for (int i = 0; i < categories.size(); i++) {
			// Create a layout for each category
			VBox cateLayout = new VBox(30);
			cateLayout.setAlignment(Pos.CENTER);
			cateLayout.setPadding(new Insets(30));

			// Create a title
			Text title = new Text("Select " + categories.get(i) + " question?");
			title.setTextAlignment(TextAlignment.CENTER);
			title.setFont(new Font(15));
			// Add title to layout
			cateLayout.getChildren().add(title);

			// New text file inside saves for the category
			File savefile = new File("./saves/" + categories.get(i));

			boolean empty = !savefile.exists() || savefile.length() == 0;

			if(empty) {
				Label comp = new Label("Completed"); 
				comp.setFont(new Font(10));
				cateLayout.getChildren().add(comp);
			}
			else {
				try (BufferedReader value = new BufferedReader(new FileReader(savefile))){
					String line;
					int row = 0;
					String cateNum = categories.get(i);

					while ((line = value.readLine()) != null) {
						int lineNum = row;
						line = line.substring(0, line.indexOf("|"));
						_valueBtn = new Button(line);
						_valueBtn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								btnClicked = (Button) event.getSource();
								// Only the lowest value is able to be clicked
								if(lineNum == 0) {
									answerScene = new Scene(answerLayout(btnClicked, cateNum, lineNum), 450, 450);
									_primary.setScene(answerScene);
								}
							}	
						});
						cateLayout.getChildren().add(_valueBtn);
						row++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Button to go back to menu
			_backBtn = new Button("Back to Menu");
			_backBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					_primary.setScene(_menu);
				}	
			});

			// Add btn to layout
			cateLayout.getChildren().add(_backBtn);
			tabs.getTabs().add(new Tab(categories.get(i), cateLayout));
		}

		// Creates a layout for the whole game module scene
		VBox gameLayout = new VBox(50);
		gameLayout.getChildren().addAll(tabs);
		_game = new Scene(gameLayout, 450, 450);

		// Display the scene
		_primary.setScene(_game);
		_primary.show();
	}

	public VBox answerLayout(Button click, String category, int lineNum) {
		File winFile = new File("./saves/winnings");

		// Get the value
		int value = Integer.parseInt(click.getText());

		Button btnEnter = new Button("Enter");
		Button dkBtn = new Button("Don't know");
		Button replay = new Button("Replay Q");

		// Get the specified line from the category file
		String readLine = null;
		String question = null;
		String text = null;
		try {
			readLine = Files.readAllLines(Paths.get("./saves/"+category)).get(lineNum);
			question = readLine.substring(readLine.indexOf("|")+ 1);
			text = question.substring(question.indexOf("|") + 1);
			text = text.substring(0, text.indexOf("|"));
			question = question.substring(0, question.indexOf("|"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// tts the question
		HelperThread ttsQ = new HelperThread(question);
		ttsQ.start();

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

		// Allow user to enter their answer
		TextField txtInput = new TextField();
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

				// Get the answer
				String readLine = null;
				try {
					readLine = Files.readAllLines(Paths.get("./saves/"+category)).get(lineNum);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String answer = readLine.substring(readLine.indexOf("|") + 1);
				answer = answer.substring(answer.indexOf("|") + 1);
				answer = answer.substring(answer.indexOf("|") + 1);

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
				
				//if (txtInput.getText().equalsIgnoreCase(answer.trim())) {
				if (Arrays.asList(answers).contains(txtInput.getText().toLowerCase())) {
					a.setTitle("Correct");
					// tts correct
					HelperThread ttsA = new HelperThread("Correct");
					ttsA.start();
					a.setHeaderText("Your answer was correct");
					money += value;
				}
				else {
					a.setTitle("Incorrect");
					// tts the answer
					HelperThread ttsA = new HelperThread("Answer was " + answer);
					ttsA.start();
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

				update(category, lineNum);
				a.showAndWait();
				startScene();
			}	
		});

		dkBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Get the answer
				String readLine = null;
				try {
					readLine = Files.readAllLines(Paths.get("./saves/"+category)).get(lineNum);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String answer = readLine.substring(readLine.indexOf("|") + 1);
				answer = answer.substring(answer.indexOf("|") + 1);
				answer = answer.substring(answer.indexOf("|") + 1);

				Alert a = new Alert(AlertType.CONFIRMATION);
				a.setTitle("Answer");
				// tts the answer
				HelperThread ttsA = new HelperThread("Answer was " + answer);
				ttsA.start();
				a.setHeaderText("The correct answer was " + answer);

				update(category, lineNum);
				a.showAndWait();
				startScene();
			}
		});

		// Replay the question
		replay.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent arg0) {
				// Get the slider value
				double speed = slider.getValue();

				String readLine = null;
				String question = null;
				try {
					readLine = Files.readAllLines(Paths.get("./saves/"+category)).get(lineNum);
					question = readLine.substring(readLine.indexOf("|")+ 1);
					question = question.substring(0, question.indexOf("|"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// tts the question
				HelperThread ttsQ = new HelperThread(question, speed);
				ttsQ.start();
			}
		});

		// Layout of the answer scene where user gets to input answer to question
		VBox layout = new VBox(20);
		layout.setAlignment(Pos.BASELINE_CENTER);
		layout.setPadding(new Insets(100));
		Label clue = new Label("Clue: " + text + "...");
		clue.setMinWidth(Region.USE_PREF_SIZE);

		layout.getChildren().addAll(clue, txtInput, btnEnter, dkBtn, replay, slider);
		return layout;
	}

	/*
	 * Method updates save files so that questions which have been answered are removed
	 */
	public void update(String cateFile, int lineRemove) {
		lineRemove++;
		File inputFile = new File("./saves/"+cateFile);
		File tmp = new File("./saves/"+cateFile+"Tmp");

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentLine;
		int count = 0;
		try {
			writer = new BufferedWriter(new FileWriter(tmp));
			while((currentLine = reader.readLine()) != null) {
				count++;
				if (count == lineRemove) {
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inputFile.delete();
		tmp.renameTo(inputFile);
	}
}
