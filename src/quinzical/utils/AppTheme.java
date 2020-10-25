package quinzical.utils;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AppTheme {
	
	private Paint paint;
	private Image image;
	private Background bg;
	private String str;
	// Setting the linear gradient
	private final Stop[] stops = new Stop[] { new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.web("#fc8b8b")) };
	private final LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
	private final Stop[] stops2 = new Stop[] { new Stop(0, Color.DARKSLATEBLUE), new Stop(1, Color.DARKRED) };
	private final LinearGradient gradient2 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops2);
	
	public AppTheme() {
		paint = gradient;
		BackgroundFill bgf = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
		bg = new Background(bgf);
		str = "light";
	}
	
	public void changeLight() {
		str = "light";
		paint = gradient;
		BackgroundFill bgf = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
		bg = new Background(bgf);
	}
	
	public void changeDark() {
		str = "dark";
		paint = gradient2;
		BackgroundFill bgf = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
		bg = new Background(bgf);
	}
	
	public void changeNZ() {
		str = "nz";
		image = new Image("file:./images/NZ.png");
		BackgroundSize size = new BackgroundSize(394, 500, false, false, false, false);
		BackgroundImage bgi = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, size);
		bg = new Background(bgi);
	}
	
	public void changeMaori() {
		str = "maori";
		image = new Image("file:./images/Maori.png");
		BackgroundSize size = new BackgroundSize(354, 500, false, false, false, false);
		BackgroundImage bgi = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, size);
		bg = new Background(bgi);
	}
	
	public Background getBackground() {
		return bg;
	}
	
	/*
	 * set up for text
	 */
	public void setText(Text text) {
		text.setFill(Color.web("#f26868"));
		text.setStroke(Color.web("#e82a2a"));
		text.setFont(new Font("Arial", 25));
	}
	
	public void setSmallText(Text text) {
		if(str.contentEquals("") || str.contentEquals("light")) {
			text.setFill(Color.web("#000000"));
			text.setStroke(Color.web("#7f96eb"));
			text.setStrokeWidth(0.7);
			text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		}
		
		else if(str.contentEquals("maori")) {
			text.setFill(Color.web("#f26868"));
			text.setStroke(Color.web("#7f96eb"));
			text.setStrokeWidth(0.7);
			text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		}
		
		else if(str.contentEquals("nz")){
			text.setFill(Color.web("#ffffff"));
			text.setStroke(Color.web("#7f96eb"));
			text.setStrokeWidth(0.7);
			text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		}
		else {
			text.setFill(Color.web("#ffffff"));
			text.setStroke(Color.web("#f26868"));
			text.setStrokeWidth(0.7);
			text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		}
	}
	
	public void setCenter(Text text) {
		text.setWrappingWidth(380);
		text.setTextAlignment(TextAlignment.CENTER);
	}
}
