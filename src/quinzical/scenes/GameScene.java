package quinzical.scenes;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

public class GameScene extends Menu{

	private Stage primary;
	private Scene menu;
	private String[] catNames;
	private Button nZBtn, worldBtn, menuBtn;
	private final DropShadow shadow = new DropShadow();
	private Background _bg;

	public GameScene(String[] catNames, Stage primary, Scene menu, AppTheme theme) {
		this.primary = primary;
		this.menu = menu;
		this.catNames = catNames;
		super.theme = theme;
	}

	public void startScene() {

		_bg = theme.getBackground();

		Image world = new Image("file:./images/world.png");
		ImageView viewWorld = new ImageView(world);
		viewWorld.setFitHeight(125);
		viewWorld.setFitWidth(125);
		
		Image nz = new Image("file:./images/flag.png");
		ImageView viewNZ = new ImageView(nz);
		viewNZ.setFitHeight(125);
		viewNZ.setFitWidth(125);
		
		nZBtn = new Button();
		nZBtn.setTranslateX(30);
		nZBtn.setTranslateY(20);
		nZBtn.setGraphic(viewNZ);
		
		worldBtn = new Button();
		worldBtn.setTranslateX(170);
		worldBtn.setTranslateY(20);
		worldBtn.setGraphic(viewWorld);
		
		menuBtn = new Button("Back to Menu");
		menuBtn.setTranslateY(160);

		TilePane btnPane = new TilePane(Orientation.HORIZONTAL);
		btnPane.getChildren().addAll(nZBtn, worldBtn);
		
		// Title
		Text title = new Text("Games Mode");
		theme.setText(title);
		title.setTextAlignment(TextAlignment.CENTER);

		VBox gameLayout = new VBox(40);
		gameLayout.setAlignment(Pos.BASELINE_CENTER);
		gameLayout.setBackground(_bg);
		gameLayout.setPadding(new Insets(80));
		gameLayout.getChildren().addAll(title, btnPane, menuBtn);

		Scene gameScene = new Scene(gameLayout, 650, 600);
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
				NZScene game = new NZScene(catNames, primary, menu, theme);
				game.startScene();
			}	
		});
		
		worldBtn.setEffect(shadow);
		worldBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primary.setScene(menu);
			}	
		});
	}
}
