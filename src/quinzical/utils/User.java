package quinzical.utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

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
