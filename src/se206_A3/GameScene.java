package se206_A3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameScene {

	private Stage _primary;
	private Scene _menu, _game;
	private String[] _catNames;
	private Random _rnd;
	private int _monVal;
	private List<String> categories, lines, questions;
	private Button _valueBtn, btnClicked, _backBtn;
	
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
		
		// Selects 5 random unique categories
		while (categories.size() < 5) {
			// Generates a random index number
			int index = _rnd.nextInt(_catNames.length);
			// Check if category has already been added
			if(!categories.contains(_catNames[index])) {
				categories.add(_catNames[index]);
			}
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
			
			// Get 5 random questions from that category
			_monVal = 100;
			while (questions.size() < 5) {
				int rndLineIndex = _rnd.nextInt(lines.size());
				String line = lines.get(rndLineIndex);
				
				if(!questions.contains(line)) {
					questions.add(line);
					_valueBtn = new Button("" + _monVal);
					cateLayout.getChildren().add(_valueBtn);
					_monVal += 100;
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
			
			questions.clear(); // Not ideal
			lines.clear();
			
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
}
