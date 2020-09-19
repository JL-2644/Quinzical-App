package se206_A3;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene {

	private Stage _primary;
	private Scene _menu;
	private String[] _catNames;
	
	public GameScene(String[] catNames, Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}
	
	public void startScene() {

	}
}
