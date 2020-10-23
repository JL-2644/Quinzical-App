package quinzical.scenes;

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

	private Button _backBtn, _clear;
	private TableView<User> table;
	private Stage _primary;
	private final DropShadow shadow = new DropShadow();
	private static ObservableList<User> users = FXCollections.observableArrayList();
	private Scene _menu;
	private Background _bg;

	public LeaderBoard(Stage primary, Scene menu, AppTheme theme) {
		_primary = primary;
		_menu = menu;
		super.theme = theme;
	}

	public void start() {
		
		_bg = theme.getBackground();
		shadow.setColor(Color.web("#7f96eb"));

		TableColumn<User, String> nameColumn = new TableColumn<>("Name");
		TableColumn<User, Integer> scoreColumn = new TableColumn<>("Score");
		nameColumn.setMinWidth(200);
		scoreColumn.setMinWidth(250);
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		scoreColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("score"));

		table = new TableView<>();
		table.prefHeightProperty().bind(_primary.heightProperty().multiply(0.8));
		table.setItems(getUsers());
		table.getColumns().addAll(nameColumn, scoreColumn);

		// Button to go back to menu
		_backBtn = new Button("Back to Menu");
		_backBtn.setEffect(shadow);
		_backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_primary.setScene(_menu);
			}	
		});
		
		_clear = new Button("Clear LeaderBoard");

		TilePane tileBtns = new TilePane(Orientation.HORIZONTAL);
		tileBtns.setAlignment(Pos.BASELINE_CENTER);
		tileBtns.setHgap(150);
		tileBtns.getChildren().addAll(_clear, _backBtn);
		
		VBox vBox = new VBox(20);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(table, tileBtns);
		vBox.setBackground(_bg);

		Scene scene = new Scene(vBox, 650, 600);
		_primary.setScene(scene);
		_primary.show();

	}
	public void add(String name, int score) {
		users.add(new User(name, score));
	}

	public ObservableList<User> getUsers() {
		return users;
	}
}
