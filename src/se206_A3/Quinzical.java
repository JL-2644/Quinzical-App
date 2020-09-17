package se206_A3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
		pracBtn= new Button("Enter practive module");
		gameBtn= new Button("Enter Game Module");
		quitBtn= new Button("Quit Game");
		
		// Title
		Text title = new Text("Welcome to Quinzical!");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(new Font(25));
		
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
