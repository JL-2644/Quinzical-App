package quinzical.scenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

public class GameScene extends Menu{

	private Stage _primary;
	private Scene _menu, _game;
	private String[] _catNames;
	private Random _rnd;
	private int _monVal;
	private List<String> _categories, _lines, _questions;
	private Button _valueBtn, _backBtn, btnClicked;
	private final DropShadow shadow = new DropShadow();
	private Background _bg;
	
	public GameScene(String[] catNames, Stage primary, Scene menu, AppTheme theme) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
		super.theme = theme;
	}

	/*
	 * Starts the game scene
	 */
	public void startScene() {

		_bg = theme.getBackground();
		shadow.setColor(Color.web("#7f96eb"));
		
		// Create lists
		_categories = new ArrayList<String>();
		_questions = new ArrayList<String>();
		_lines = new ArrayList<String>();

		// Instantiate a random object
		_rnd = new Random();

		// Creates a directory for save files
		new File("./saves").mkdir();

		File saveDir = new File("./saves");
		File winFile = new File("./saves/winnings");
		String[] saveFiles = saveDir.list();

		// Initial preparation of save states
		if ( saveFiles.length < 1) {
			// Selects 5 random unique categories
			while (_categories.size() < 5) {
				// Generates a random index number
				int index = _rnd.nextInt(_catNames.length);
				// Check if category has already been added
				if(!_categories.contains(_catNames[index])) {
					_categories.add(_catNames[index]);
				}
			}
			// Save the selected categories to a save folder
			for (int i = 0; i < _categories.size(); i++) {
				createSave(_categories.get(i), "categories");
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
			// exclude winnings and international file
			for (String category: saveFiles) {
				if(!category.equals("winnings") && !category.equals("International")) {
					_categories.add(category);
				}

			}
		}
		File international = new File("./saves/International");

		// Check if all sections have been completed
		int count = 0;
		for (int i = 0; i < _categories.size(); i++) {
			File file = new File("./saves/"+ _categories.get(i));
			if (file.length() == 0) {
				count++;
			}
		}

		// If the game has been completed, display the reward scene
		if(count == _categories.size() && international.length() == 0) {
			// Start up the reward scene
			RewardScene reward = new RewardScene(_primary, _menu, theme);
			reward.startScene();
			return;
		}

		// If two sections have been completed, open up international module

		if(count >= 2 ) {
			// If first time unlocking then display a pop up 
			if(!international.exists()) {
				createSave("International", "categoriesInternational");

				Alert a = new Alert(AlertType.CONFIRMATION);
				a.setTitle("Unlocked");
				a.setHeaderText("Congratulations, you have unlocked the international section");
				a.showAndWait();
			}

			_categories.add("International");
		}

		TabPane tabs= new TabPane();
		// Tabs cannot be closed
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		// Create the tabs for the 5 categories
		for (int i = 0; i < _categories.size(); i++) {
			// Create a layout for each category
			VBox cateLayout = new VBox(30);
			cateLayout.setBackground(_bg);
			cateLayout.setAlignment(Pos.CENTER);
			cateLayout.setPadding(new Insets(30));

			// Create a title
			Text title = new Text("Select " + _categories.get(i) + " question?");
			title.setTextAlignment(TextAlignment.CENTER);
			title.setFont(new Font(15));
			// Add title to layout
			cateLayout.getChildren().add(title);

			// New text file inside saves for the category
			File savefile = new File("./saves/" + _categories.get(i));

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
					String cateNum = _categories.get(i);
					while ((line = value.readLine()) != null) {
						int lineNum = row;
						line = line.substring(0, line.indexOf("|"));
						_valueBtn = new Button(line);
						_valueBtn.setEffect(shadow);

						// All other buttons disabled except the lowest value one
						if(lineNum != 0) {
							_valueBtn.setDisable(true);
						}

						_valueBtn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								btnClicked = (Button) event.getSource();
								AnswerScene answer = new AnswerScene(btnClicked, cateNum, 
										lineNum, _primary, _catNames, _menu, theme);

								answer.startScene();
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
			_backBtn.setEffect(shadow);
			_backBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					_primary.setScene(_menu);
				}	
			});

			// Add btn to layout
			cateLayout.getChildren().add(_backBtn);
			tabs.getTabs().add(new Tab(_categories.get(i), cateLayout));
		}

		// Creates a layout for the whole game module scene
		VBox gameLayout = new VBox(50);
		gameLayout.getChildren().addAll(tabs);
		_game = new Scene(gameLayout, 500, 500);

		// Display the scene
		_primary.setScene(_game);
		_primary.show();
	}

	/*
	 * This method creates a save file for a specific category
	 */
	private void createSave(String cateName, String dir) {
		// New text file inside saves for the category
		File savefile = new File("./saves/" + cateName);
		// Text file inside categories folder
		File catefile = new File("./" + dir + "/" + cateName);

		// Store all the lines from the category into a list
		try (BufferedReader value = new BufferedReader(new FileReader(catefile))) {
			String line;
			while ((line = value.readLine()) != null) {
				_lines.add(line);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}

		// Get 5 random questions from that list, write to the new file
		_monVal = 100;
		while (_questions.size() < 5) {
			int rndLineIndex = _rnd.nextInt(_lines.size());
			String line = _lines.get(rndLineIndex);

			if(!_questions.contains(line)) {
				// Write the line to the new file
				BufferedWriter out = null;
				try {
					int val = _monVal;
					savefile.createNewFile();
					// Appends the new lines to the file with the values
					out = new BufferedWriter(new FileWriter(savefile, true));
					out.write(val + "|" + line);
					out.newLine();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				_monVal += 100;
				_questions.add(line);
			}
		}
		_questions.clear();
		_lines.clear();
	}
}
