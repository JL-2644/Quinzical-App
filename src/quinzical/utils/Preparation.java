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

public class Preparation {

	private List<String> _categories, _lines, _questions;
	private Random _rnd;
	private String[] _catNames;
	private int _monVal;
	
	public Preparation(String[] catNames) {
		_catNames = catNames;
	}

	public List<String> loadCategories() {
		// Create lists
		_categories = new ArrayList<String>();
		_questions = new ArrayList<String>();
		_lines = new ArrayList<String>();

		_rnd = new Random();

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
			while (_categories.size() < 5) {
				// Generates a random index number
				int index = _rnd.nextInt(_catNames.length);
				// Check if category has already been added
				if(!_categories.contains(_catNames[index])) {
					_categories.add(_catNames[index]);
				}
			}
			// Save the selected categories to a save folder
			for (int i = 0; i < _categories.size(); i++) {
				createSave(_categories.get(i), "categories");
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
					_categories.add(category);
				}

			}
		}
		
		return _categories;
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
				_lines.add(line);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}

		// Get 5 random questions from that list, write to the new file
		_monVal = 100;
		while (_questions.size() < 5) {
			int rndLineIndex = _rnd.nextInt(_lines.size());
			String line = _lines.get(rndLineIndex);

			if(!_questions.contains(line)) {
				// Write the line to the new file
				BufferedWriter out = null;
				try {
					int val = _monVal;
					savefile.createNewFile();
					// Appends the new lines to the file with the values
					out = new BufferedWriter(new FileWriter(savefile, true));
					out.write(val + "|" + line);
					out.newLine();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				_monVal += 100;
				_questions.add(line);
			}
		}
		_questions.clear();
		_lines.clear();
	}

}
