package se206_A3;


import java.io.IOException;

public class HelperThread extends Thread{

	private String text;
	public HelperThread(String text) {
		this.text = text;
	}
	
	@Override
	public void run() {
		String cmd = "echo " + "\"" + text + "\"" + " | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
