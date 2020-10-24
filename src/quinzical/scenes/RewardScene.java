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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

/**
 * Once the users have finished the NZ quiz game. They are then moved to this scene
 * where the reward is displayed. There are three tiers, gold, silver and bronze that
 * the user receives depending on their score.
 * 
 * @author JiaQi and Marcus
 */
public class RewardScene extends Menu {

	private Scene _reward, menu;
	private Button menuBtn, addScore;
	private Stage primary;
	private final DropShadow shadow = new DropShadow();
	private Background bg;
	private int score;
	private ImageView view;
	private Image medal;

	public RewardScene(Stage primary, Scene menu, AppTheme theme) {
		this.primary = primary;
		this.menu = menu;
		super.theme = theme;
	}
	/*
	 * Starts the reward scene
	 */
	public void startScene() {

		bg = theme.getBackground();
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
		// Gold reward
		if(score >= 6000) {
			medal = new Image("file:./images/gold.png");
			reward.setText("You have won Gold");
		}
		//Silver reward
		else if (score >= 3000 && score < 6000) {
			medal = new Image("file:./images/silver.png");
			int diff = 6000 - score;
			reward.setText("You have won Silver. You were " + diff + " away from gold");
		}
		//Bronze reward
		else {
			medal = new Image("file:./images/bronze.png");
			int diff = 3000 - score;
			reward.setText("You have won Bronze. You were " + diff + " away from silver");
		}
		
		view = new ImageView(medal);
		view.setFitHeight(150);
		view.setPreserveRatio(true);
		
		menuBtn =  new Button("Play Again?");
		menuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clear();
				// Go back to menu
				primary.setScene(menu);
			}	
		});

		addScore = new Button("Add to leaderBoard?");
		addScore.setOnAction(new EventHandler<ActionEvent>() {
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
				primary.setScene(menu);
			}

		});

		// Create a layout for the reward scene
		VBox rewardLayout = new VBox(40);
		rewardLayout.setAlignment(Pos.BASELINE_CENTER);
		rewardLayout.setPadding(new Insets(60));
		rewardLayout.setBackground(bg);
		rewardLayout.getChildren().addAll(title, finalScore, view, reward, addScore, menuBtn);
		_reward = new Scene(rewardLayout, 650, 600);

		// Display the scene
		primary.setScene(_reward);
		primary.show();
	}
	/*
	 * Clear all the save data
	 */
	private void clear() {
		File saveDir = new File("./saves");
		for(File file : saveDir.listFiles()) {
			file.delete();
		}
	}

	/*
	 * Saves the users to a leader board file. Thus, even after quitting the 
	 * game the users in the leader board are remembered.
	 */
	private void save(String name, int score) {
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
