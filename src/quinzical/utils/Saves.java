package quinzical.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * This class creates a save state of the 5 categories selected by putting them into 
 * a new directory called saves. This is done to allow users to continue with their 
 * NZ quiz game even after quitting.
 * 
 * @author JiaQi and Marcus
 *
 */
public class Saves {

	private List<String> categories, lines, questions;
	private Random rnd;
	private String[] catNames;
	private int monVal;
	
	public Saves(String[] catNames) {
		this.catNames = catNames;
	}
	/**
	 *  This method return the list of categories that are either newly created or 
	 *  obtained from previous save states
	 */
	public List<String> loadCategories() {
		// Create lists
		categories = new ArrayList<String>();
		questions = new ArrayList<String>();
		lines = new ArrayList<String>();

		rnd = new Random();

		// Creates a directory for save files
		new File("./saves").mkdir();
		new File("./leaderboard").mkdir();

		File saveDir = new File("./saves");
		File winFile = new File("./saves/winnings");
		File scoreFile = new File("./leaderboard/score");
		String[] saveFiles = saveDir.list();
		
		if(!scoreFile.exists()) {
			try {
				scoreFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Initial preparation of save states
		if ( saveFiles.length < 1) {
			// Selects 5 random unique categories
			while (categories.size() < 5) {
				// Generates a random index number
				int index = rnd.nextInt(catNames.length);
				// Check if category has already been added
				if(!categories.contains(catNames[index])) {
					categories.add(catNames[index]);
				}
			}
			// Save the selected categories to a save folder
			for (int i = 0; i < categories.size(); i++) {
				createSave(categories.get(i), "categories");
			}

			// Create a winnings file to store money earned
			BufferedWriter writer = null;
			try {
				winFile.createNewFile();
				writer = new BufferedWriter(new FileWriter(winFile));
				writer.write("" + 0);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		else {
			// If game is ongoing, add the category names to the list
			// exclude winnings and international file
			for (String category: saveFiles) {
				if(!category.equals("winnings") && !category.equals("International")) {
					categories.add(category);
				}

			}
		}
		
		return categories;
	}

	/*
	 * This method creates a save file for a specific category
	 */
	public void createSave(String cateName, String dir) {
		// New text file inside saves for the category
		File savefile = new File("./saves/" + cateName);
		// Text file inside categories folder
		File catefile = new File("./" + dir + "/" + cateName);

		// Store all the lines from the category into a list
		try (BufferedReader value = new BufferedReader(new FileReader(catefile))) {
			String line;
			while ((line = value.readLine()) != null) {
				lines.add(line);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}

		// Get 5 random questions from that list, write to the new file
		monVal = 100;
		while (questions.size() < 5) {
			int rndLineIndex = rnd.nextInt(lines.size());
			String line = lines.get(rndLineIndex);

			if(!questions.contains(line)) {
				// Write the line to the new file
				BufferedWriter out = null;
				try {
					int val = monVal;
					savefile.createNewFile();
					// Appends the new lines to the file with the values
					out = new BufferedWriter(new FileWriter(savefile, true));
					out.write(val + "|" + line);
					out.newLine();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				monVal += 100;
				questions.add(line);
			}
		}
		questions.clear();
		lines.clear();
	}

}
