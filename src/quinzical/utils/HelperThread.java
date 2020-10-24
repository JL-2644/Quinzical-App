package quinzical.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A new thread that is to run the tts system. This was created as tts is too intensive
 * and will freeze GUI.
 * 
 * @author JiaQi and Marcus
 */
public class HelperThread extends Thread{

	private String text;
	private double speed;

	public HelperThread(String text) {
		this.text = text;
		this.speed = 1;
	}

	public HelperThread(String text, double speed) {
		this.text = text;
		this.speed = 2.5 - speed;
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
			scmWriter.write("(Parameter.set 'Duration_Stretch " + speed + ")");
			scmWriter.newLine();
			scmWriter.write("(SayText " + "\"" + text + "\"" + ")");
			scmWriter.newLine();
			scmWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Run the file in bash
		String cmd = "`" + "festival -b ./tts/question.scm" + "`";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
