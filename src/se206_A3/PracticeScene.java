package se206_A3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PracticeScene {

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
	Button _back = new Button("Main Menu");

	//Constructor
	public PracticeScene(String[] catNames, Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}

	//Method to start the scene
	public void startScene() {
		//Initialize the data(questions, answers, categories)
		initial();
		int count = 0;
		//button for going to main menu
		_back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				_primary.setTitle("Quinzical");
				_primary.setScene(_menu);
				_primary.centerOnScreen();
			}
		});
		Label label = new Label("Pick a catergory!!!");
		label.setFont(new Font("Arial", 24));
		VBox vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(_back);
		vbox.getChildren().add(label);
		VBox.setMargin(label, new Insets(10, 10, 10, 10));
		VBox.setMargin(_back, new Insets(10, 10, 10, 10));
		//create buttons according to amount of categories
		for (String cat:_cat) {
			Button catButton = new Button(cat);
			vbox.getChildren().add(catButton);
			VBox.setMargin(catButton, new Insets(10, 10, 10, 10));
			catList.add(catButton);
			count++;
		}
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
					
					Label label2 = new Label("Question");
					label2.setFont(new Font("Arial", 24));
					VBox vbox2 = new VBox(5);
					vbox2.setAlignment(Pos.CENTER);
					VBox.setMargin(label2, new Insets(10, 10, 10, 10));
					Text que = new Text(randomQuestion);
					double len = que.getLayoutBounds().getWidth();
					TextField answerTxt = new TextField();
					answerTxt.setMaxWidth(180);
					Button confirm = new Button("Submit");
					
					//set up slider for tts
					Slider slider = new Slider();
					slider.setMin(0.5);
					slider.setMax(2);
					slider.setValue(1);
					slider.setShowTickLabels(true);
					slider.setShowTickMarks(true);
					slider.setBlockIncrement(0.25);
					slider.setMaxWidth(180);
					
					VBox.setMargin(que, new Insets(10, 10, 10, 10));
					VBox.setMargin(slider, new Insets(10, 10, 10, 10));
					
					Label clueLabel = new Label(clue.get(tmp).get(ran) + ": ...");
					Label info = new Label("Adjust question speed");
					VBox.setMargin(clueLabel, new Insets(10, 10, 10, 10));
					
					//replay button
					Button replay = new Button("Replay");
					replay.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							double speed = slider.getValue();
							HelperThread ttsQ = new HelperThread(randomQuestion, speed);
							ttsQ.start();
						}
					});
					VBox.setMargin(que, new Insets(10, 10, 10, 10));
					VBox.setMargin(answerTxt, new Insets(10, 10, 10, 10));
					VBox.setMargin(confirm, new Insets(10, 10, 10, 10));
					vbox2.getChildren().addAll(label2, que, slider, info, replay, clueLabel, answerTxt, confirm);
					Scene scene2 = new Scene(vbox2, (int) len + 100, 450);
					//tts the question
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
									Label label3 = new Label("Question");
									label3.setFont(new Font("Arial", 24));
									Label label4 = new Label("Answer");
									label4.setFont(new Font("Arial", 24));
									VBox vbox3 = new VBox(5);
									vbox3.setAlignment(Pos.CENTER);
									VBox.setMargin(label3, new Insets(10, 10, 10, 10));
									VBox.setMargin(label4, new Insets(10, 10, 10, 10));
									Text que = new Text(randomQuestion);
									
									//Button to go back to practice module
									Button practice = new Button("Practice Module");
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
									VBox.setMargin(que, new Insets(10, 10, 10, 10));
									VBox.setMargin(practice, new Insets(10, 10, 10, 10));
									VBox.setMargin(ans, new Insets(10, 10, 10, 10));
									
									//tts the answer
									HelperThread ttsQ = new HelperThread(sen);
									ttsQ.start();
									vbox3.getChildren().addAll(label3, que, label4, ans, _back, practice);
									Scene scene3 = new Scene(vbox3, (int) len + 100, 450);
									_primary.setScene(scene3);
									_primary.centerOnScreen();
								} else {
									//not revealing the correct answer if the user hasn't answered the questions three times
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
										VBox vbox4 = new VBox(5);
										vbox4.setAlignment(Pos.CENTER);
										vbox4.getChildren().addAll(label2, que, slider, info, replay, hint, clueLabel,
												answerTxt, confirm);
										
										//change to a scene that contain hint
										Scene scene4 = new Scene(vbox4, (int) len + 100, 450);
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

	//Initialize the information used for the game from categories/ folder.
	public void initial() {
				
		try {
				for (String line:_catNames) {
					_cat.add(line);
					String file = "categories/" + line;
					readFile(file);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Read file to give useful information such as clues, questions and answers.
	public void readFile(String file) {
		String line = "";
		String split = "\\|";
		List<String> questionTmp = new ArrayList<String>();
		List<String> answerTmp = new ArrayList<String>();
		List<String> clueTmp = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			while ((line = br.readLine()) != null) {

				String[] after = line.split(split);

				questionTmp.add(after[0]);
				clueTmp.add(after[1]);
				answerTmp.add(after[2].trim());
			}

			question.add(questionTmp);
			clue.add(clueTmp);
			answer.add(answerTmp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//get random position from a list
	public int getRandomElement(List<String> list) {
		Random rand = new Random();
		return rand.nextInt(list.size());
	}
}
