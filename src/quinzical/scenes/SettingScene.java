package quinzical.scenes;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;

public class SettingScene extends Menu{

	private Stage _primaryStage;
	private Scene _menu;
	private Button _light, _dark, _nz, _maori, _back, _reset;
	private final DropShadow shadow = new DropShadow();
	private Background _bg;
	
	public SettingScene(Stage primaryStage, Scene menuScene, AppTheme theme, VBox layout) {
		_primaryStage = primaryStage;
		_menu = menuScene;
		super.theme = theme;
		super.layout = layout;
	}

	public void startScene() {
		Text label = new Text("Choose a background theme!");
		label.setFill(Color.web("#f26868"));
		label.setStroke(Color.web("#e82a2a"));
		label.setFont(new Font("Arial", 24));
		
		shadow.setColor(Color.web("#7f96eb"));
		_light = new Button("Light Theme");
		_dark = new Button("Dark Theme");
		_nz = new Button("New Zealand Flag");
		_maori = new Button("Maori Flag");
		_back = new Button("Main Menu");
		_reset= new Button("Reset Game");
		
		_light.setEffect(shadow);
		_dark.setEffect(shadow);
		_nz.setEffect(shadow);
		_maori.setEffect(shadow);
		_back.setEffect(shadow);
		_reset.setEffect(shadow);
		_bg = theme.getBackground();
		VBox vbox = new VBox(30);
		vbox.getChildren().addAll(label, _light, _dark, _nz, _maori, _reset, _back);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.setBackground(_bg);
		vbox.setPadding(new Insets(100));
		Scene setting = new Scene(vbox, 500, 500);
		_primaryStage.setScene(setting);
		_primaryStage.setTitle("Settings");
		_primaryStage.centerOnScreen();
		
		// Handle when the light button is pressed
		_light.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theme.changeLight();
				_bg = theme.getBackground();
				VBox vbox2 = new VBox(40);
				vbox2.getChildren().addAll(label, _light, _dark, _nz, _maori, _back);
				vbox2.setAlignment(Pos.BASELINE_CENTER);
				vbox2.setBackground(_bg);
				vbox2.setPadding(new Insets(100));
				Scene setting = new Scene(vbox2, 500, 500);
				_primaryStage.setScene(setting);
				layout.setBackground(_bg);
			}
		});
		
		// Handle when the dark button is pressed
		_dark.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theme.changeDark();
				_bg = theme.getBackground();
				VBox vbox2 = new VBox(40);
				vbox2.setBackground(_bg);
				vbox2.getChildren().addAll(label,_light, _dark, _nz, _maori, _back);
				vbox2.setAlignment(Pos.BASELINE_CENTER);
				vbox2.setPadding(new Insets(100));
				Scene setting = new Scene(vbox2, 500, 500);
				_primaryStage.setScene(setting);
				layout.setBackground(_bg);
			}
		});
		
		// Handle when NZ button is pressed
		_nz.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theme.changeNZ();
				_bg = theme.getBackground();
				VBox vbox2 = new VBox(40);
				vbox2.getChildren().addAll(label, _light, _dark, _nz, _maori, _back);
				vbox2.setAlignment(Pos.BASELINE_CENTER);
				vbox2.setBackground(_bg);
				vbox2.setPadding(new Insets(100));
				Scene setting = new Scene(vbox2, 500, 500);
				_primaryStage.setScene(setting);
				layout.setBackground(_bg);
			}
		});
		
		// Handle when Maori button is pressed
		_maori.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theme.changeMaori();
				_bg = theme.getBackground();
				VBox vbox2 = new VBox(40);
				vbox2.getChildren().addAll(label, _light, _dark, _nz, _maori, _back);
				vbox2.setAlignment(Pos.BASELINE_CENTER);
				vbox2.setBackground(_bg);
				vbox2.setPadding(new Insets(100));
				Scene setting = new Scene(vbox2, 500, 500);
				_primaryStage.setScene(setting);
				layout.setBackground(_bg);
			}
		});
		
		// Handle when back button is pressed
		_back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_primaryStage.setScene(_menu);
			}
		});
		
		_reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File saveDir = new File("./saves");
				// Clear all save data
				for(File file : saveDir.listFiles()) {
					file.delete();
				}
			}	
		});
	}
}
