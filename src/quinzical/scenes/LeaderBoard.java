package quinzical.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import quinzical.utils.User;

public class LeaderBoard {

	private Button _backBtn;
	private TableView<User> table;
	private Stage _primary;
	private final DropShadow shadow = new DropShadow();
	private static ObservableList<User> users = FXCollections.observableArrayList();
	private Scene _menu;

	public LeaderBoard(Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
	}

	public void start() {
		shadow.setColor(Color.web("#7f96eb"));

		TableColumn<User, String> nameColumn = new TableColumn<>("Name");
		TableColumn<User, Integer> scoreColumn = new TableColumn<>("Score");
		nameColumn.setMinWidth(200);
		scoreColumn.setMinWidth(250);
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		scoreColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("score"));

		table = new TableView<>();
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

		VBox vBox = new VBox(20);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(table, _backBtn);

		Scene scene = new Scene(vBox, 500, 500);
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
