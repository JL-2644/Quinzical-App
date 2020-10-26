package quinzical.scenes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

/**
 * This is the main scene for the NZ quiz game mode, five
 * categories are randomly selected. From that 5, users can 
 * choose from among these categories and answer a question
 * with a corresponding value.
 * 
 * @author JiaQi and Marcus
 */
public class NZScene extends Menu{

	private Stage primary;
	private Scene gameScene, nzScene, menu;
	private String[] catNames;
	private Button valueBtn, backBtn, btnClicked;
	private List<String> categories;
	private final DropShadow shadow = new DropShadow();
	private Background bg;

	public NZScene(String[] catNames, List<String> categories, Stage primary, Scene gameScene, 
			Scene menu, AppTheme theme) {
		
		this.menu = menu;
		this.catNames = catNames;
		this.primary = primary;
		this.gameScene = gameScene;
		this.categories = categories;
		super.theme = theme;
	}

	/*
	 * Starts the scene
	 */
	public void startScene() {

		bg = theme.getBackground();
		shadow.setColor(Color.web("#7f96eb"));

		File international = new File("./saves/International");

		// Check if all sections have been completed
		int count = 0;
		for (int i = 0; i < categories.size(); i++) {
			File file = new File("./saves/"+ categories.get(i));
			if (file.length() == 0) {
				count++;
			}
		}

		// If the game has been completed, display the reward scene
		if(count == categories.size()) {
			// Start up the reward scene
			RewardScene reward = new RewardScene(primary, gameScene, theme);
			reward.startScene();
			return;
		}

		// If two sections have been completed, and first time unlocking
		if(count >= 2 && !international.exists()) {
			try {
				international.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setTitle("Unlocked");
			a.setHeaderText("Congratulations, you have unlocked the international section");
			a.showAndWait();
		}

		TabPane tabs= new TabPane();
		// Tabs cannot be closed
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		// Create the tabs for the 5 categories
		for (int i = 0; i < categories.size(); i++) {
			// Create a layout for each category
			VBox cateLayout = new VBox(30);
			cateLayout.setAlignment(Pos.CENTER);
			cateLayout.setPadding(new Insets(80));

			// Create a title
			Text title = new Text("Select " + categories.get(i) + " question?");
			theme.setText(title);
			// Add title to layout
			cateLayout.getChildren().add(title);

			// New text file inside saves for the category
			File savefile = new File("./saves/" + categories.get(i));

			boolean empty = !savefile.exists() || savefile.length() == 0;

			if(empty) {
				Text comp = new Text("Completed"); 
				theme.setSmallText(comp);
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
						valueBtn = new Button(line);
						valueBtn.setEffect(shadow);

						// All other buttons disabled except the lowest value one
						if(lineNum != 0) {
							valueBtn.setDisable(true);
						}

						valueBtn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								btnClicked = (Button) event.getSource();
								AnswerScene answer = new AnswerScene(btnClicked, cateNum, 
										lineNum, primary, categories, gameScene, catNames, menu, theme);

								answer.startScene();
							}	
						});

						cateLayout.getChildren().add(valueBtn);
						row++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Button to go back to menu
			backBtn = new Button("Back to Games");
			backBtn.setEffect(shadow);
			backBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					GameScene scene = new GameScene(catNames, primary, menu, theme);
					scene.startScene();
				}	
			});

			// Add btn to layout
			cateLayout.getChildren().add(backBtn);
			tabs.getTabs().add(new Tab(categories.get(i), cateLayout));
		}

		// Creates a layout for the whole game module scene
		VBox gameLayout = new VBox(50);

		gameLayout.getChildren().addAll(tabs);
		gameLayout.setBackground(bg);
		nzScene = new Scene(gameLayout, 650, 600);

		// Display the scene
		primary.setScene(nzScene);
		primary.show();
	}
}
