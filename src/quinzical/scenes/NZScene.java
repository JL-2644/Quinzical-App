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
import quinzical.utils.Preparation;

public class NZScene extends Menu{

	private Stage _primary;
	private Scene _menu, _game;
	private String[] _catNames;
	private Button _valueBtn, _backBtn, btnClicked;
	private List<String> _categories;
	private final DropShadow shadow = new DropShadow();
	private Background _bg;
	
	public NZScene(String[] catNames, Stage primary, Scene menu, AppTheme theme) {
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
		
		Preparation prep = new Preparation(_catNames);
		_categories = prep.loadCategories();
		
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
				prep.createSave("International", "categoriesInternational");

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
			cateLayout.setAlignment(Pos.CENTER);
			cateLayout.setPadding(new Insets(80));

			// Create a title
			Text title = new Text("Select " + _categories.get(i) + " question?");
			theme.setText(title);
			// Add title to layout
			cateLayout.getChildren().add(title);

			// New text file inside saves for the category
			File savefile = new File("./saves/" + _categories.get(i));

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
		gameLayout.setBackground(_bg);
		_game = new Scene(gameLayout, 650, 600);

		// Display the scene
		_primary.setScene(_game);
		_primary.show();
	}
}
