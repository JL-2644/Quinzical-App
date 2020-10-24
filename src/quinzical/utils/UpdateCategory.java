package quinzical.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * This class updates a particular saved category in which the question which
 * was recently answered by the user is removed from the category.
 * 
 * @author JiaQi and Marcus
 *
 */
public class UpdateCategory {
	
	private String catName;
	private int lineRemove;

	public UpdateCategory(String catName, int lineRemove) {
		this.catName = catName;
		this.lineRemove = lineRemove;
	}
	/*
	 * Removes the line from the category
	 */
	public void update() {
		lineRemove++;
		File inputFile = new File("./saves/"+catName);
		File tmp = new File("./saves/"+catName+"Tmp");

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
				if (count == lineRemove) {
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
