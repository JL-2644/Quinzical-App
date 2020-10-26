package quinzical.scenes;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;
import quinzical.utils.HelperThread;
import quinzical.utils.InitialData;

public class InternationalScene extends Menu {

	private Stage primary;
	private Scene gameScene;
	private String[] catNames;
	private String file;
	private List<Button> btnList = new ArrayList<Button>();
	private List<List<Button>> catBtnList = new ArrayList<List<Button>>();
	private List<List<String>> question = new ArrayList<List<String>>();
	private List<List<String>> clue = new ArrayList<List<String>>();
	private List<List<String>> answer = new ArrayList<List<String>>();
	private List<String> cat = new ArrayList<String>();
	private final DropShadow shadow = new DropShadow();
	private Background bg;
	private Button[] macrons;

	/*
	 * Constructor
	 */
	public InternationalScene(String[] catNames, Stage primary, Scene gameScene, AppTheme theme, String file) {
		this.primary = primary;
		this.gameScene = gameScene;
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
		shadow.setColor(Color.web("#7f96eb"));

		TabPane tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		for (int i = 0; i < cat.size(); i++) {
			VBox cateLayout = new VBox(30);
			cateLayout.setAlignment(Pos.CENTER);
			cateLayout.setPadding(new Insets(80));
			String name = cat.get(i);

			// Create a title
			Text title = new Text("Select " + cat.get(i) + " question?");
			theme.setText(title);

			// Add title to layout
			cateLayout.getChildren().add(title);

			btnList = new ArrayList<Button>();

			// create buttons according to amount of categories
			for (int j = 0; j < question.get(i).size(); j++) {
				Button quesButton = new Button(Integer.toString(j + 1));
				quesButton.setEffect(shadow);
				bg = theme.getBackground();
				cateLayout.setBackground(bg);

				btnList.add(quesButton);

				cateLayout.getChildren().add(quesButton);
			}

			catBtnList.add(btnList);

			// Button to go back to menu
			Button backBtn = new Button("Back to Games");
			backBtn.setEffect(shadow);
			backBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					primary.setScene(gameScene);
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

		for (List<Button> cat : catBtnList) {
			for (Button btn : catBtnList.get(catBtnList.indexOf(cat))) {
				btn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						int j = catBtnList.get(catBtnList.indexOf(cat)).indexOf(btn);
						int i = catBtnList.indexOf(cat);
						String que = question.get(i).get(j);
						String ans = answer.get(i).get(j);
						String clueIn = clue.get(i).get(j);

						VBox vbox = new VBox(30);
						vbox.setPadding(new Insets(80));

						Text clueTxt = new Text("Clue:" + clueIn + "...");
						theme.setSmallText(clueTxt);

						TextField answerTxt = new TextField();
						answerTxt.setMaxWidth(300);

						Button confirm = new Button("Enter");
						confirm.setEffect(shadow);

						// set up slider for tts
						Slider slider = new Slider();
						slider.setMin(0.5);
						slider.setMax(2);
						slider.setValue(1);
						slider.setShowTickLabels(true);
						slider.setShowTickMarks(true);
						slider.setBlockIncrement(0.25);
						slider.setMaxWidth(320);

						// replay button
						Button replay = new Button("Replay");
						replay.setEffect(shadow);
						replay.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								double speed = slider.getValue();
								HelperThread ttsQ = new HelperThread(que, speed);
								ttsQ.start();
							}
						});

						// Don't know button
						Button dont = new Button("Don't know");
						dont.setEffect(shadow);
						dont.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								wrongAns(que, ans, game);
							}
						});

						// Macron buttons
						macrons = new Button[5];
						TilePane macronTile = new TilePane(Orientation.HORIZONTAL);
						macronTile.setAlignment(Pos.BASELINE_CENTER);
						macronTile.setHgap(30);
						macrons[0] = new Button("ā");
						macrons[1] = new Button("ē");
						macrons[2] = new Button("ī");
						macrons[3] = new Button("ō");
						macrons[4] = new Button("ū");
						for (int k = 0; k < macrons.length; k++) {
							macrons[k].setEffect(shadow);
							macrons[k].setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									String current = answerTxt.getText();
									String getLetter = ((Button) event.getSource()).getText();
									answerTxt.setText(current + getLetter);
								}
							});
							macronTile.getChildren().add(macrons[k]);
						}

						TilePane tile = new TilePane(Orientation.HORIZONTAL);
						tile.setAlignment(Pos.BASELINE_CENTER);
						tile.setHgap(30);
						tile.getChildren().addAll(confirm, dont, replay);

						Text info = new Text("Adjust question speed (default is 1)");
						theme.setSmallText(info);

						vbox.setAlignment(Pos.CENTER);
						vbox.setBackground(bg);
						vbox.getChildren().addAll(clueTxt, macronTile, answerTxt, tile, slider, info);

						Scene scene2 = new Scene(vbox, 650, 600);
						primary.setScene(scene2);

						HelperThread ttsQ = new HelperThread(que);
						ttsQ.start();

						confirm.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								// Get user input, check if it matches the actual answer
								String sen;
								String userAns = answerTxt.getText().trim().toLowerCase();

								boolean status = false;
								// If answer contains "/" then check all possible answers
								if (ans.contains("/") == true) {
									String[] parts = ans.split("/");
									for (String diffAns : parts) {
										// if it matches, status would be true and it would break the for loop
										if (userAns.equals(diffAns.trim().toLowerCase())) {
											status = true;
											break;
										}
									}
								}

								// if user answer equals actual answer or if status is true
								if (userAns.equals(ans.toLowerCase()) || status == true) {

									sen = "Correct!!!";
									HelperThread ttsQ = new HelperThread(sen);
									ttsQ.start();
									Alert a = new Alert(AlertType.NONE, sen, ButtonType.OK);
									a.setTitle("Result");
									a.showAndWait();
									primary.setScene(gameScene);
								} else {
									wrongAns(que, ans, game);
								}
							}
						});
					}
				});
			}
		}
	}

	public void wrongAns(String que, String ans, Scene scene) {
		Text label3 = new Text("Question");
		theme.setText(label3);
		Text label4 = new Text("Answer");
		theme.setText(label4);
		VBox vbox3 = new VBox(40);
		vbox3.setAlignment(Pos.CENTER);
		vbox3.setBackground(bg);
		Text queTxt = new Text(que);
		theme.setSmallText(queTxt);
		theme.setCenter(queTxt);

		// Button to go back to game module
		Button game = new Button("Back to International");
		game.setEffect(shadow);
		game.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				primary.setScene(scene);
			}
		});
		// show user the correct answer
		String sen = "The correct answer was: " + ans;
		Text ansTxt = new Text(sen);
		theme.setSmallText(ansTxt);

		// tts the answer
		HelperThread ttsQ = new HelperThread(sen);
		ttsQ.start();
		vbox3.getChildren().addAll(label3, queTxt, label4, ansTxt, game);
		Scene scene3 = new Scene(vbox3, 650, 600);
		primary.setScene(scene3);
	}
}
