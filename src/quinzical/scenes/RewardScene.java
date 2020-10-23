package quinzical.scenes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

public class RewardScene extends Menu {

	private Scene _reward, _menu;
	private Button _menuBtn, _addScore;
	private Stage _primary;
	private final DropShadow shadow = new DropShadow();
	private Background _bg;
	private int score;

	public RewardScene(Stage primary, Scene menu, AppTheme theme) {
		_primary = primary;
		_menu = menu;
		super.theme = theme;
	}
	/*
	 * Starts the reward scene
	 */
	public void startScene() {

		_bg = theme.getBackground();
		shadow.setColor(Color.web("#7f96eb"));

		// Title
		Text title = new Text("Congratulations");
		theme.setText(title);

		// Read money value from file
		File winFile = new File("./saves/winnings");
		BufferedReader win = null;
		score = 0;
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
		Text finalScore = new Text("Your final score was " + score);
		theme.setSmallText(finalScore);

		Text reward = new Text();
		theme.setSmallText(reward);
		// Gold
		if(score >= 6000) {
			reward.setText("You have won Gold");
		}
		//Silver
		else if (score >= 3000 && score < 6000) {
			reward.setText("You have won Silver");
		}
		//Bronze
		else {
			reward.setText("You have won Bronze");
		}
		
		_menuBtn =  new Button("Play Again?");
		_menuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clear();
				// Go back to menu
				_primary.setScene(_menu);
			}	
		});

		_addScore = new Button("Add to leaderBoard?");
		_addScore.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				TextInputDialog txtInput = new TextInputDialog();
				txtInput.setTitle("Add to LeaderBoard");
				txtInput.getDialogPane().setContentText("Name: ");
				TextField name = txtInput.getEditor();
				txtInput.showAndWait();
				if(name.getText().length() == 0) {
					name.setText("Anonymous");
				}
				save(name.getText(), score);
				clear();
				_primary.setScene(_menu);
			}

		});

		// Create a layout for the reward scene
		VBox rewardLayout = new VBox(50);
		rewardLayout.setAlignment(Pos.BASELINE_CENTER);
		rewardLayout.setPadding(new Insets(100));
		rewardLayout.setBackground(_bg);
		rewardLayout.getChildren().addAll(title, finalScore, reward, _addScore, _menuBtn);
		_reward = new Scene(rewardLayout, 650, 600);

		// Display the scene
		_primary.setScene(_reward);
		_primary.show();
	}
	/*
	 * Clear all the save data
	 */
	public void clear() {
		File saveDir = new File("./saves");
		for(File file : saveDir.listFiles()) {
			file.delete();
		}
	}

	public void save(String name, int score) {
		// Write user and their score to file
		File scoreFile = new File("./leaderboard/score");
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(scoreFile, true));
			out.write(name + "|" + score);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
