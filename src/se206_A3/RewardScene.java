package se206_A3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class RewardScene {

	private Scene _reward, _menu;
	private Button _menuBtn;
	private Stage _primary;

	public RewardScene(Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
	}

	public void startScene() {
		// Title
		Text title = new Text("Congratulations, you have finished the games module");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(new Font(15));

		// Read money value from file
		File winFile = new File("./saves/winnings");
		BufferedReader win = null;
		int score = 0;
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
		score = Integer.parseInt(moneyPool);

		// Display text of the final score
		Label finalScore = new Label("Your final score was " + score);

		_menuBtn =  new Button("Play Again?");
		_menuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File saveDir = new File("./saves");
				// Clear all save data
				for(File file : saveDir.listFiles()) {
					file.delete();
				}
				// Go back to menu
				_primary.setScene(_menu);
			}	
		});

		// Create a layout for the reward scene
		VBox rewardLayout = new VBox(50);
		rewardLayout.setAlignment(Pos.BASELINE_CENTER);
		rewardLayout.setPadding(new Insets(100));
		rewardLayout.getChildren().addAll(title, finalScore, _menuBtn);
		_reward = new Scene(rewardLayout, 500, 450);

		// Display the scene
		_primary.setScene(_reward);
		_primary.show();
	}
}
