package quinzical.scenes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;
import quinzical.utils.User;

public class LeaderBoard extends Menu{

	private Button backBtn, clear;
	private TableView<User> table;
	private Stage primary;
	private final DropShadow shadow = new DropShadow();
	private Scene menu;
	private Background bg;
	private ObservableList<User> users = FXCollections.observableArrayList();

	public LeaderBoard(Stage primary, Scene menu, AppTheme theme) {
		this.primary = primary;
		this.menu = menu;
		super.theme = theme;
	}

	public void start() {

		File scoreFile = new File("./leaderboard/score");
		
		bg = theme.getBackground();
		shadow.setColor(Color.web("#7f96eb"));

		TableColumn<User, String> nameColumn = new TableColumn<>("Name");
		TableColumn<User, Integer> scoreColumn = new TableColumn<>("Score");
		nameColumn.setMinWidth(250);
		scoreColumn.setMinWidth(300);
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		scoreColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("score"));

		table = new TableView<>();
		table.prefHeightProperty().bind(primary.heightProperty().multiply(0.8));
		// Initialize previous winners onto the leaderboard
		if(scoreFile.exists()) {
			table.setItems(loadUsers());
		}
		table.getColumns().addAll(Arrays.asList(nameColumn, scoreColumn));

		// Button to go back to menu
		backBtn = new Button("Back to Menu");
		backBtn.setEffect(shadow);
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primary.setScene(menu);
			}	
		});
		
		clear = new Button("Clear LeaderBoard");
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scoreFile.delete();
				users.clear();
			}	
		});

		TilePane tileBtns = new TilePane(Orientation.HORIZONTAL);
		tileBtns.setAlignment(Pos.BASELINE_CENTER);
		tileBtns.setHgap(150);
		tileBtns.getChildren().addAll(clear, backBtn);
		
		VBox vBox = new VBox(20);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(table, tileBtns);
		vBox.setBackground(bg);

		Scene scene = new Scene(vBox, 650, 600);
		primary.setScene(scene);
		primary.show();

	}
	
	private ObservableList<User> loadUsers() {
		File scoreFile = new File("./leaderboard/score");
		String username = null;
		int score = 0;
		try (BufferedReader read = new BufferedReader(new FileReader(scoreFile))){
			String line;
			while ((line = read.readLine()) != null) {
				username = line.substring(0, line.indexOf("|"));
				score = Integer.parseInt(line.substring(line.indexOf("|") + 1));
				users.add(new User(username, score));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
}
