package quinzical.scenes;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

public class Menu extends Application{

	private Button pracBtn, gameBtn, quitBtn, settingBtn;
	private Scene menuScene;
	private final DropShadow shadow = new DropShadow();
	protected AppTheme theme = new AppTheme();
	protected VBox layout;
	private Background _bg;
	
	@Override
	public void start(Stage primaryStage) {
		
		_bg = theme.getBackground();
		
		shadow.setColor(Color.web("#7f96eb"));
		
		// Get an array of all the category names
		File cateDir = new File("./categories");
		String[] cateFiles = cateDir.list();

		// Create new buttons for the three options
		pracBtn= new Button("Enter Practice Module");
		gameBtn= new Button("Enter Game Module");
		quitBtn= new Button("Quit Game");
		settingBtn= new Button("Settings");
		pracBtn.setEffect(shadow);
		gameBtn.setEffect(shadow);
		quitBtn.setEffect(shadow);
		settingBtn.setEffect(shadow);
		
		// Title
		Text title = new Text("Welcome to Quinzical!");
		theme.setText(title);
		title.setTextAlignment(TextAlignment.CENTER);
		
		// Handle when the practice module button is pressed
		pracBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Start up the practice module scene
				PracticeScene prac = new PracticeScene(cateFiles, primaryStage, menuScene, theme);
				prac.startScene();	
			}
		});
		
		// Handle when the games button is pressed
		gameBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Start up the game module scene
				GameScene game = new GameScene(cateFiles, primaryStage, menuScene, theme);
				game.startScene();
			}
		});
		
		//Handle the quit button when pressed
		quitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Create a prompt to confirm game close
				Alert confirm = new Alert(AlertType.CONFIRMATION);
				confirm.setTitle("Quinzical");
				confirm.setHeaderText("Do you want to exit the game?");
				
				ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

				confirm.getButtonTypes().setAll(yesButton, noButton);
				
				confirm.showAndWait();
				
				// If user selected yes close the game, else continue to play
				if(confirm.getResult() == yesButton) {
					primaryStage.close();
				}
			}
		});
		
		// Handle when the settings button is pressed
		settingBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Start up the game module scene
				SettingScene setting = new SettingScene(primaryStage, menuScene, theme, layout);
				setting.startScene();
			}
		});
		
		// Layout of menu
		layout = new VBox(40);
		layout.setAlignment(Pos.BASELINE_CENTER);
		layout.setBackground(_bg);
		layout.setPadding(new Insets(100));
		layout.getChildren().addAll(title, pracBtn, gameBtn,settingBtn, quitBtn);

		menuScene = new Scene(layout, 500, 500);
		primaryStage.setScene(menuScene);
		primaryStage.setTitle("Quinzical");
		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
