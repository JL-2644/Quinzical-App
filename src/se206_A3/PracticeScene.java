package se206_A3;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PracticeScene {
	
	private Stage _primary;
	private Scene _menu;
	private String[] _catNames;
	private List<Button> catList = new ArrayList<Button>();
	
	public PracticeScene(String[] catNames, Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}
	
	public void startScene() {
		int count = 0;
		Label label = new Label("Pick a catergory!!!");
		label.setFont(new Font("Arial", 24));
		VBox vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(label);
		VBox.setMargin(label, new Insets(10, 10, 10, 10));
		for (String cat:_catNames) {
			Button catButton = new Button(cat);
			vbox.getChildren().add(catButton);
			VBox.setMargin(catButton, new Insets(10, 10, 10, 10));
			catList.add(catButton);
			count++;
		}
		Scene scene = new Scene(vbox, 500, 45 * count + 124);
		_primary.setScene(scene);
	}
}
