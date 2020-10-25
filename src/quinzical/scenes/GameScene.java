package quinzical.scenes;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;
import quinzical.utils.Saves;

/**
 * This is the games module scene where users can select to go to the NZ quiz or 
 * when they have finished two categories go to the international scene.
 * 
 * @author JiaQi and Marcus
 *
 */
public class GameScene extends Menu{

	private Stage primary;
	private Scene menu, gameScene;
	private String[] catNames;
	private List<String> categories;
	private Button nZBtn, worldBtn, menuBtn;
	private final DropShadow shadow = new DropShadow();
	private Background bg;

	public GameScene(String[] catNames, Stage primary, Scene menu, AppTheme theme) {
		this.primary = primary;
		this.menu = menu;
		this.catNames = catNames;
		super.theme = theme;
	}

	public void startScene() {

		bg = theme.getBackground();

		// Retrieve the images
		Image world = new Image("file:./images/world.png");
		ImageView viewWorld = new ImageView(world);
		viewWorld.setFitHeight(125);
		viewWorld.setFitWidth(125);

		Image nz = new Image("file:./images/flag.png");
		ImageView viewNZ = new ImageView(nz);
		viewNZ.setFitHeight(125);
		viewNZ.setFitWidth(125);

		// Set the images onto the button
		nZBtn = new Button();
		nZBtn.setTranslateX(30);
		nZBtn.setTranslateY(20);
		nZBtn.setGraphic(viewNZ);

		worldBtn = new Button();
		worldBtn.setTranslateX(170);
		worldBtn.setTranslateY(20);
		worldBtn.setGraphic(viewWorld);

		Saves prep = new Saves(catNames);
		categories = prep.loadCategories();
		// Count the number of categories completed
		int count = 0;
		for (int i = 0; i < categories.size(); i++) {
			File file = new File("./saves/"+ categories.get(i));
			if (file.length() == 0) {
				count++;
			}
		}

		// Disable world until count is greater or equal to 2.
		if(count < 2 ) {
			worldBtn.setDisable(true);
		}

		menuBtn = new Button("Back to Menu");
		menuBtn.setTranslateY(160);

		// PLace all the buttons in a tilepane
		TilePane btnPane = new TilePane(Orientation.HORIZONTAL);
		btnPane.getChildren().addAll(nZBtn, worldBtn);

		// Create title for games module
		Text title = new Text("Games Mode");
		theme.setText(title);
		title.setTextAlignment(TextAlignment.CENTER);

		// Create the layout for the scene
		VBox gameLayout = new VBox(40);
		gameLayout.setAlignment(Pos.BASELINE_CENTER);
		gameLayout.setBackground(bg);
		gameLayout.setPadding(new Insets(80));
		gameLayout.getChildren().addAll(title, btnPane, menuBtn);

		gameScene = new Scene(gameLayout, 650, 600);
		primary.setScene(gameScene);
		primary.show();

		menuBtn.setEffect(shadow);
		menuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primary.setScene(menu);
			}	
		});

		nZBtn.setEffect(shadow);
		nZBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Start up the game module scene
				NZScene game = new NZScene(catNames, primary, gameScene, theme);
				game.startScene();
			}	
		});

		worldBtn.setEffect(shadow);
		worldBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File cateDir = new File("./categoriesInternational");
				String[] cateFiles = cateDir.list();
				// Start up the game module scene
				InternationalScene inter = new InternationalScene(cateFiles, primary, menu, theme, "categoriesInternational/");
				inter.startScene();
			}	
		});
	}
}
