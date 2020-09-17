package se206_A3;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Quinzical extends Application{

	private Button pracBtn, gameBtn, quitBtn;
	private Scene menuScene;
	
	@Override
	public void start(Stage primaryStage) {
		
		// Create new buttons for the three options
		pracBtn= new Button("Enter practice module");
		gameBtn= new Button("Enter Game Module");
		quitBtn= new Button("Quit Game");
		
		// Title
		Text title = new Text("Welcome to Quinzical!");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(new Font(25));
		
		// Handle when the practice module button is pressed
		pracBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Start up the practice module scene
				PracticeScene prac = new PracticeScene(primaryStage, menuScene);
				prac.startScene();	
			}
		});
		
		// Handle when the games button is pressed
		gameBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Start up the game module scene
				GameScene game = new GameScene(primaryStage, menuScene);
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
		
		// Layout of menu
		VBox layout = new VBox(40);
		layout.setAlignment(Pos.BASELINE_CENTER);
		layout.setPadding(new Insets(100));
		layout.getChildren().addAll(title, pracBtn, gameBtn, quitBtn);

		menuScene = new Scene(layout, 450, 450);
		primaryStage.setScene(menuScene);
		primaryStage.setTitle("Quinzical");
		
		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
