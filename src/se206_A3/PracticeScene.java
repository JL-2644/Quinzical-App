package se206_A3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
	private List<List<String>> value = new ArrayList<List<String>>(); 
	private List<List<String>> question = new ArrayList<List<String>>(); 
	private List<List<String>> answer = new ArrayList<List<String>>();
	private List<Button> questionList = new ArrayList<Button>();
	private List<String> cat = new ArrayList<String>();
	private boolean status = false;
	
	public PracticeScene(String[] catNames, Stage primary, Scene menu) {
		_primary = primary;
		_menu = menu;
		_catNames = catNames;
	}
	
	public void startScene() {
		initial();
		int count = 0;
		Label label = new Label("Pick a catergory!!!");
		label.setFont(new Font("Arial", 24));
		VBox vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(label);
		VBox.setMargin(label, new Insets(10, 10, 10, 10));
		for (String cat:_catNames) {
			Button catButton = new Button(cat);
			vbox.getChildren().add(catButton);
			VBox.setMargin(catButton, new Insets(10, 10, 10, 10));
			catList.add(catButton);
			count++;
		}
		Scene scene = new Scene(vbox, 500, 45 * count + 124);
		_primary.setScene(scene);
		
		for (Button cat:catList) {
			cat.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle (ActionEvent e) {
					int tmp = catList.indexOf(cat);
					Label label2 = new Label("Pick a question!!!");
					label2.setFont(new Font("Arial", 24));
					VBox vbox2 = new VBox(5);
					vbox2.setAlignment(Pos.CENTER);
					vbox2.getChildren().add(label2);
					VBox.setMargin(label2, new Insets(10, 10, 10, 10));
					
					//Add buttons to questionList.
					for (int i = 0; i<question.get(tmp).size(); i++) {
						Button questionButton = new Button(Integer.toString(i));
						vbox2.getChildren().add(questionButton);
						VBox.setMargin(questionButton, new Insets(10, 10, 10, 10));
						questionList.add(questionButton);

					}
					
					//Tells user when a category has no questions left to attempt, switch back to main menu if it does.
					if (questionList.size()==0) {
						a = new Alert(AlertType.INFORMATION, "No more questions left for this category!!!", ButtonType.OK);
						a.setTitle("Information");
						a.showAndWait();
						_primary.setScene(_menu);
					}
					//Otherwise change to the next scene.
					else {
						Scene scene2 = new Scene(vbox2, 500, 320);
						_primary.setScene(scene2);
					}
					
					for (Button quesButton:questionList) {
						quesButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle (ActionEvent e) {

								int tmp2 = questionList.indexOf(quesButton);
								Label label3 = new Label("Question:");
								label3.setFont(new Font("Arial", 24));
								VBox vbox3 = new VBox(5);
								vbox3.setAlignment(Pos.CENTER);
								VBox.setMargin(label3, new Insets(10, 10, 10, 10));
								
								//Change to a new scene to show user the chosen question and there is a textField and Button to answer with.
								String q = question.get(tmp).get(tmp2);
								Text que = new Text(q);
								TextField answerTxt = new TextField();
								Button confirm = new Button("Submit");
								vbox3.getChildren().addAll(label3, que, answerTxt, confirm);
								VBox.setMargin(que, new Insets(10, 10, 10, 10));
								VBox.setMargin(answerTxt, new Insets(10, 10, 10, 10));
								VBox.setMargin(confirm, new Insets(10, 10, 10, 10));
						        	Scene scene4 = new Scene(vbox3, 500, 320);
						        	speak(q);
						       	 	_primary.setScene(scene4);
						        
						        	confirm.setOnAction(new EventHandler<ActionEvent>() {
						        		@Override
						        		public void handle (ActionEvent e) {
						        		//Get user input, check if it matches the actual answer, 
						        		//if yes winning would increase, otherwise it would decrease.
						        		//Change back to main menu scene after answering the question.
						        		//Attempted questions' information would be deleted from the lists.
						        			String sen;
						        			String userAns = answerTxt.getText().trim().toLowerCase();
						        			if (userAns.equals(answer.get(tmp).get(tmp2).toLowerCase())) {
						        			
						        				sen = "Correct!!!";
						        				speak(sen);
						        				a = new Alert(AlertType.NONE, sen, ButtonType.OK);
											a.setTitle("Result");
											a.showAndWait();
											
											question.get(tmp).remove(tmp2);
											answer.get(tmp).remove(tmp2);
											_primary.setScene(_menu);
						        			}
						        			else {

						        				sen = "Wrong Answer, the correct answer is: " + answer.get(tmp).get(tmp2);
						        				speak(sen);
						        				a = new Alert(AlertType.NONE, sen, ButtonType.OK);
											a.setTitle("Result");
											a.showAndWait();
											
											question.get(tmp).remove(tmp2);
											answer.get(tmp).remove(tmp2);
											_primary.setScene(_menu);
						        			}
						        		}
						        	});
							}
						});
					}
				}
			});
		}
	}
	
	//Initialize the information used for the game from categories/ folder.
		public void initial() {
			try {
				String find = "ls categories/";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", find);
				Process process = pb.start();

				BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				
				int exitStatus = process.waitFor();
				String line;
					
				if (exitStatus == 0) {
							
					while ((line = stdout.readLine()) != null) {
						status = true;
						cat.add(line);
						String file = "categories/" + line;
						readFile(file);
					}
				} 
				
				else {
					
					while ((line = stderr.readLine()) != null) {
						status = false;
						Alert a = new Alert(AlertType.ERROR);
						a.setHeaderText("Can't find directory");
						a.setTitle("Error encountered");
						a.setContentText(line);
						a.show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//Read file to give useful information such as values, questions and answers.
		public void readFile(String file) {
			String line = "";
	        String split = ";";
	        List<String> questionTmp = new ArrayList<String>();
	        List<String> answerTmp = new ArrayList<String>();
	        
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

	            while ((line = br.readLine()) != null) {

	                String[] after = line.split(split);
	                
	                questionTmp.add(after[0]);
	                answerTmp.add(after[1].trim());
	            }
	            
	            question.add(questionTmp);
	            answer.add(answerTmp);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	
	//Play audio using festival via bash.
	public void speak (String sen) {
		String command = "echo " + sen + " | festival --tts";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			
		try {
			Process process = pb.start();
			process.destroy();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
