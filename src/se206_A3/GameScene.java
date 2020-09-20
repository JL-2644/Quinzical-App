package se206_A3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameScene {

	private Stage _primary;
	private Scene _menu, _game;
	private String[] _catNames;
	private Random _rnd;
	private int _monVal;
	
	public GameScene(String[] catNames, Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}
	
	public void startScene() {
		// Create a categories list
		List<String> categories = new ArrayList<String>();
		// Instantiate a random object
		_rnd = new Random();
		
		// Add 5 random unique categories to the list
		while (categories.size() < 5) {
			// Generates a random index number
			int index = _rnd.nextInt(_catNames.length);
			// Check if category has already been added
			if(!categories.contains(_catNames[index])) {
				categories.add(_catNames[index]);
			}
		}
		
		// Create a layout for each category
		VBox cateLayout = new VBox(50);
		_monVal = 100;
		while (_monVal < 600) {
			
			
			_monVal += 100;
		}

		// Creates tab for each category
		TabPane tabs= new TabPane();
		// Tabs cannot be closed
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		// Create the tabs for the 5 categories
		for (int i = 0; i < categories.size(); i++) {
			tabs.getTabs().add(new Tab(categories.get(i), new Label(i+ "")));
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
