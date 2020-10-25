package quinzical.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;
import quinzical.utils.InitialData;

public class InternationalScene extends Menu {

	private Stage primary;
	private Scene menu;
	private String[] catNames;
	private String file;
	private List<List<String>> question = new ArrayList<List<String>>();
	private List<List<String>> clue = new ArrayList<List<String>>();
	private List<List<String>> answer = new ArrayList<List<String>>();
	private List<String> cat = new ArrayList<String>();
	private final DropShadow shadow = new DropShadow();
	private Background bg;

	/*
	 * Constructor
	 */
	public InternationalScene(String[] catNames, Stage primary, Scene menu, AppTheme theme, String file) {
		this.primary = primary;
		this.menu = menu;
		this.catNames = catNames;
		super.theme = theme;
		this.file = file;
	}

	/*
	 * method to start the scene
	 */
	public void startScene() {
		InitialData data = new InitialData(file);
		data.initial(catNames, cat, question, clue, answer);

		TabPane tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		for (int i = 0; i < cat.size(); i++) {
			VBox cateLayout = new VBox(30);
			cateLayout.setAlignment(Pos.CENTER);
			cateLayout.setPadding(new Insets(80));
			
			// Create a title
			Text title = new Text("Select " + cat.get(i) + " question?");
			theme.setText(title);
			
			// Add title to layout
			cateLayout.getChildren().add(title);

			int row = 0;
			String name = cat.get(i);
			// create buttons according to amount of categories
			for (String question : question.get(i)) {
				int lineNum = row;
				Button quesButton = new Button(Integer.toString(row));
				quesButton.setEffect(shadow);
				bg = theme.getBackground();
				cateLayout.setBackground(bg);

				// All other buttons disabled except the lowest value one
				if (lineNum != 0) {
					quesButton.setDisable(true);
				}

//				quesButton.setOnAction(new EventHandler<ActionEvent>() {
//					@Override
//					public void handle(ActionEvent event) {
//						Button btnClicked = (Button) event.getSource();
//						AnswerScene answer = new AnswerScene(btnClicked, name, lineNum, primary, catNames, menu, theme);
//
//						answer.startScene();
//					}
//				});

				cateLayout.getChildren().add(quesButton);
				row++;
			}

			// Button to go back to menu
			Button backBtn = new Button("Back to Menu");
			backBtn.setEffect(shadow);
			backBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					primary.setScene(menu);
				}
			});

			// Add btn to layout
			cateLayout.getChildren().add(backBtn);
			tabs.getTabs().add(new Tab(name, cateLayout));
		}

		// Creates a layout for the whole game module scene
		VBox gameLayout = new VBox(50);

		gameLayout.getChildren().addAll(tabs);
		gameLayout.setBackground(bg);
		Scene game = new Scene(gameLayout, 650, 600);

		// Display the scene
		primary.setScene(game);
		primary.show();
	}
}
