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

public class AppTheme {
	
	private Paint paint;
	private Image image;
	private Background _bg;
	private String str;
	// Setting the linear gradient
	private final Stop[] _stops = new Stop[] { new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.web("#fc8b8b")) };
	private final LinearGradient _gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, _stops);
	private final Stop[] _stops2 = new Stop[] { new Stop(0, Color.DARKSLATEBLUE), new Stop(1, Color.DARKRED) };
	private final LinearGradient _gradient2 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, _stops2);
	
	public AppTheme() {
		paint = _gradient;
		BackgroundFill bgf = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
		_bg = new Background(bgf);
		str = "light";
	}
	
	public void changeLight() {
		str = "light";
		paint = _gradient;
		BackgroundFill bgf = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
		_bg = new Background(bgf);
	}
	
	public void changeDark() {
		str = "dark";
		paint = _gradient2;
		BackgroundFill bgf = new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY);
		_bg = new Background(bgf);
	}
	
	public void changeNZ() {
		str = "nz";
		image = new Image("file:NZ.png");
		BackgroundSize size = new BackgroundSize(394, 500, false, false, false, false);
		BackgroundImage bgi = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, size);
		_bg = new Background(bgi);
	}
	
	public void changeMaori() {
		str = "maori";
		image = new Image("file:Maori.png");
		BackgroundSize size = new BackgroundSize(354, 500, false, false, false, false);
		BackgroundImage bgi = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, size);
		_bg = new Background(bgi);
	}
	
	public Background getBackground() {
		return _bg;
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
}
