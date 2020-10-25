package quinzical.utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to create user objects which are then displayed by the
 * JavaFX table view
 * 
 * @author se206-2020
 *
 */
public class User {
	private SimpleStringProperty name;
	private SimpleIntegerProperty score;
	
	public User(String name, Integer score) {
		this.name = new SimpleStringProperty(name);
		this.score = new SimpleIntegerProperty(score);
	}
	
	public String getName() {
		return name.get();
	}
	
	public Integer getScore() {
		return score.get();
	}
}
