package quinzical.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.utils.AppTheme;
import quinzical.utils.HelperThread;
import quinzical.utils.InitialData;

public class PracticeScene extends Menu{

	private Stage _primary;
	private Scene _menu;
	private String[] _catNames;
	private Alert a = new Alert(AlertType.NONE);
	private List<Button> catList = new ArrayList<Button>(); 
	private List<List<String>> question = new ArrayList<List<String>>();
	private List<List<String>> clue = new ArrayList<List<String>>();
	private List<List<String>> answer = new ArrayList<List<String>>();
	private List<String> _cat = new ArrayList<String>();
	private int _attempts = 0;
	private Button _back = new Button("Main Menu");
	private final DropShadow shadow = new DropShadow();
	private Background _bg;
	private Button[] _macrons;

	/*
	 * Constructor
	 */
	public PracticeScene(String[] catNames, Stage primary, Scene menu, AppTheme theme) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
		super.theme = theme;
	}

	/*
	 * Method to start the scene
	 */
	public void startScene() {
		
		//Initialize the data(questions, answers, categories)
		InitialData data = new InitialData();
		data.initial(_catNames, _cat, question, clue, answer);
		int count = 0;
		
		// Set shadow color
		shadow.setColor(Color.web("#7f96eb"));
		
		//button for going to main menu
		Button back = new Button("Main Menu");
		back.setEffect(shadow);
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				_primary.setTitle("Quinzical");
				_primary.setScene(_menu);
				_primary.centerOnScreen();
			}
		});
		Text label = new Text("Pick a catergory!!!");
		theme.setText(label);
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(back);
		vbox.getChildren().add(label);
		
		//create buttons according to amount of categories
		for (String cat:_cat) {
			Button catButton = new Button(cat);
			catButton.setEffect(shadow);
			vbox.getChildren().add(catButton);
			catList.add(catButton);
			count++;
		}
		
		_bg = theme.getBackground();
		vbox.setBackground(_bg);
		
		//Practice scene
		Scene scene = new Scene(vbox, 500, 45 * count + 164);
		_primary.setTitle("Practice");
		_primary.setScene(scene);

		//Set up for every category buttons
		for (Button cat:catList) {
			cat.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent e) {
					int tmp = catList.indexOf(cat);
					
					//Find a random question
					int ran = getRandomElement(question.get(tmp));
					String randomQuestion = question.get(tmp).get(ran);
					
					Text label2 = new Text("Question");
					theme.setText(label2);
					VBox vbox2 = new VBox(20);
					vbox2.setAlignment(Pos.CENTER);
					vbox2.setBackground(_bg);
					Text que = new Text(randomQuestion);
					theme.setSmallText(que);
					double len = que.getLayoutBounds().getWidth();
					TextField answerTxt = new TextField();
					answerTxt.setMaxWidth(180);
					Button confirm = new Button("Submit");
					confirm.setEffect(shadow);
					
					//set up slider for tts
					Slider slider = new Slider();
					slider.setMin(0.5);
					slider.setMax(2);
					slider.setValue(1);
					slider.setShowTickLabels(true);
					slider.setShowTickMarks(true);
					slider.setBlockIncrement(0.25);
					slider.setMaxWidth(180);
					
					// Macron buttons
					_macrons = new Button[5];
					TilePane macronTile = new TilePane(Orientation.HORIZONTAL);
					macronTile.setAlignment(Pos.BASELINE_CENTER);
					macronTile.setHgap(30);
					_macrons[0] = new Button("ā");
					_macrons[1] = new Button("ē");
					_macrons[2] = new Button("ī");
					_macrons[3] = new Button("ō");
					_macrons[4] = new Button("ū");
					for (int i = 0; i < _macrons.length; i++) {
						_macrons[i].setOnAction(new EventHandler<ActionEvent> () {
							@Override
							public void handle(ActionEvent event) {
								String current = answerTxt.getText();
								String getLetter = ((Button)event.getSource()).getText();
								answerTxt.setText(current + getLetter);
							}
						});
						macronTile.getChildren().add(_macrons[i]);
					}
					
					
					Text clueLabel = new Text(clue.get(tmp).get(ran) + ": ...");
					theme.setSmallText(clueLabel);
					Text info = new Text("Adjust question speed (default is 1)");
					theme.setSmallText(info);
					
					//replay button
					Button replay = new Button("Replay");
					replay.setEffect(shadow);
					replay.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							double speed = slider.getValue();
							HelperThread ttsQ = new HelperThread(randomQuestion, speed);
							ttsQ.start();
						}
					});
					vbox2.getChildren().addAll(label2, que, slider, info, replay, clueLabel, macronTile, answerTxt, confirm);
					int sceneWidth = setWidth(len);
					Scene scene2 = new Scene(vbox2, sceneWidth, 500);
					// tts the question
					HelperThread ttsQ = new HelperThread(randomQuestion);
					ttsQ.start();

					//change scene to the random question
					_primary.setScene(scene2);
					_primary.centerOnScreen();
					_attempts = 0;

					confirm.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							// Get user input, check if it matches the actual answer
							String sen;
							String userAns = answerTxt.getText().trim().toLowerCase();
							String fullAns = answer.get(tmp).get(ran);
							boolean status = false;
							//If answer contains "/" then check all possible answers
							if (fullAns.contains("/") == true) {
								String[] parts = fullAns.split("/");
								for (String diffAns : parts) {
									//if it matches, status would be true and it would break the for loop
									if (userAns.equals(diffAns.trim().toLowerCase())) {
										status = true;
										break;
									}
								}
							}
							
							//if user answer equals actual answer or if status is true
							if (userAns.equals(fullAns.toLowerCase()) || status == true) {

								sen = "Correct!!!";
								HelperThread ttsQ = new HelperThread(sen);
								ttsQ.start();
								a = new Alert(AlertType.NONE, sen, ButtonType.OK);
								a.setTitle("Result");
								a.showAndWait();
								_primary.setScene(scene);
								_primary.centerOnScreen();
							} else {
								_attempts++;
								//After all attempts
								if (_attempts == 3) {
									Text label3 = new Text("Question");
									theme.setText(label3);
									Text label4 = new Text("Answer");
									theme.setText(label4);
									VBox vbox3 = new VBox(40);
									vbox3.setAlignment(Pos.CENTER);
									vbox3.setBackground(_bg);
									Text que = new Text(randomQuestion);
									theme.setSmallText(que);
									
									//Button to go back to practice module
									Button practice = new Button("Practice Module");
									practice.setEffect(shadow);
									practice.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent e) {
											_primary.setTitle("Practice");
											_primary.setScene(scene);
											_primary.centerOnScreen();
										}
									});
									//show user the correct answer
									sen = "The correct answer is: " + answer.get(tmp).get(ran);
									Text ans = new Text(sen);
									theme.setSmallText(ans);
									_back.setEffect(shadow);
									_back.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent e) {
											_primary.setTitle("Quinzical");
											_primary.setScene(_menu);
											_primary.centerOnScreen();
										}
									});
									
									//tts the answer
									HelperThread ttsQ = new HelperThread(sen);
									ttsQ.start();
									vbox3.getChildren().addAll(label3, que, label4, ans, _back, practice);
									Scene scene3 = new Scene(vbox3, sceneWidth, 500);
									_primary.setScene(scene3);
									_primary.centerOnScreen();
								} else {
									//not revealing the correct answer if the user hasn't 
									//answered the questions three times
									//already
									sen = "Wrong Answer";
									HelperThread ttsQ = new HelperThread(sen);
									ttsQ.start();
									a = new Alert(AlertType.NONE, sen, ButtonType.OK);
									a.setTitle("Result");
									a.showAndWait();

									//User gets the first letter of the answer as a hint
									if (_attempts == 2) {
										Text hint = new Text("Hint: " + answer.get(tmp).get(ran).charAt(0));
										theme.setSmallText(hint);
										VBox vbox4 = new VBox(20);
										vbox4.setAlignment(Pos.CENTER);
										vbox4.setBackground(_bg);
										vbox4.getChildren().addAll(label2, que, slider, info, replay, hint, clueLabel,
												macronTile, answerTxt, confirm);

										// change to a scene that contain hint
										Scene scene4 = new Scene(vbox4, sceneWidth, 500);
										_primary.setScene(scene4);
										_primary.centerOnScreen();
									}
								}
							}
						}
					});
				}
			});
		}
	}

	/*
	 * get random position from a list
	 */
	public int getRandomElement(List<String> list) {
		Random rand = new Random();
		return rand.nextInt(list.size());
	}
	
	/*
	 * set minimum window width for practice scene.
	 */
	public int setWidth(double len) {
		int sceneWidth = (int)len + 100;
		if (sceneWidth < 500) {
			sceneWidth = 500;
		}
		return sceneWidth;
	}
}
