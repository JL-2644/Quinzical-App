package se206_A3;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HelperThread extends Thread{

	private String text;
	private double _speed;
	
	public HelperThread(String text) {
		this.text = text;
		_speed = 1;
	}
	
	public HelperThread(String text, double speed) {
		this.text = text;
		_speed = speed;
	}
	
	@Override
	public void run() {
		
		// Creates a directory for tts files
		new File("./tts").mkdir();
				
		// Write to a scm file
		File tts = new File("./tts/question.scm");
		BufferedWriter scmWriter = null;
		try {
			tts.createNewFile();
			scmWriter = new BufferedWriter(new FileWriter(tts));
			scmWriter.write("(voice_kal_diphone)");
			scmWriter.newLine();
			scmWriter.write("(Parameter.set 'Duration_Stretch " + _speed + ")");
			scmWriter.newLine();
			scmWriter.write("(SayText " + "\"" + text + "\"" + ")");
			scmWriter.newLine();
			scmWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String cmd = "`" + "festival -b ./tts/question.scm" + "`";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
