package se206_A3.quinzical.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UpdateCategory {
	
	private String _catName;
	private int _lineRemove;

	public UpdateCategory(String catName, int lineRemove) {
		_catName = catName;
		_lineRemove = lineRemove;
	}
	
	public void update() {
		_lineRemove++;
		File inputFile = new File("./saves/"+_catName);
		File tmp = new File("./saves/"+_catName+"Tmp");

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentLine;
		int count = 0;
		try {
			writer = new BufferedWriter(new FileWriter(tmp));
			while((currentLine = reader.readLine()) != null) {
				count++;
				if (count == _lineRemove) {
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inputFile.delete();
		tmp.renameTo(inputFile);
	}
}
