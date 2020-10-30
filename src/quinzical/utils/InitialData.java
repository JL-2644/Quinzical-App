package quinzical.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to initialize all the information needed
 * (category names, question, answer) for international module 
 * and practice module
 * 
 * @author Marcus and JiaQi
 *
 */
public class InitialData {
	
	private String _file;
	
	/**
	 * This is the constructor
	 * @param file the file needed for the initialization
	 */
	public InitialData(String file) {
		_file = file;
	}
	
	/**
	 * Initialize the information used for the game from categories/ folder.
	 * @param catNames array of category names
	 * @param cat the list of category names
	 * @param question the list of list of questions
	 * @param clue the list of list of clues
	 * @param answer the list of list of answers
	 */
	public void initial(String[] catNames, List<String> cat, List<List<String>> question, 
			List<List<String>> clue, List<List<String>> answer) {
				
		try {
				for (String line:catNames) {
					cat.add(line);
					String file = _file + line;
					// read the file with the category names and lists provided
					readFile(file, question, clue, answer);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read file to give useful information such as clues, questions and answers.
	 * 
	 * @param file the name of the file needed to be read
	 * @param question the list of list of questions
	 * @param clue the list of list of clues
	 * @param answer the list of list of answers
	 */
	public void readFile(String file, List<List<String>> question, 
			List<List<String>> clue, List<List<String>> answer) {
		String line = "";
		String split = "\\|";
		// renew the lists
		List<String> questionTmp = new ArrayList<String>();
		List<String> answerTmp = new ArrayList<String>();
		List<String> clueTmp = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			while ((line = br.readLine()) != null) {

				String[] after = line.split(split);

				// add questions, clues and answers into separate lists
				questionTmp.add(after[0]);
				clueTmp.add(after[1]);
				answerTmp.add(after[2].trim());
			}

			// add lists of questions, clues and answers into separate lists for different categories
			question.add(questionTmp);
			clue.add(clueTmp);
			answer.add(answerTmp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
