package quinzical.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InitialData {
	
	private String _file;
	
	public InitialData(String file) {
		_file = file;
	}
	
	/*
	 * Initialize the information used for the game from categories/ folder.
	 */
	public void initial(String[] catNames, List<String> cat, List<List<String>> question, 
			List<List<String>> clue, List<List<String>> answer) {
				
		try {
				for (String line:catNames) {
					cat.add(line);
					String file = _file + line;
					readFile(file, question, clue, answer);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Read file to give useful information such as clues, questions and answers.
	 */
	public void readFile(String file, List<List<String>> question, 
			List<List<String>> clue, List<List<String>> answer) {
		String line = "";
		String split = "\\|";
		List<String> questionTmp = new ArrayList<String>();
		List<String> answerTmp = new ArrayList<String>();
		List<String> clueTmp = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			while ((line = br.readLine()) != null) {

				String[] after = line.split(split);

				questionTmp.add(after[0]);
				clueTmp.add(after[1]);
				answerTmp.add(after[2].trim());
			}

			question.add(questionTmp);
			clue.add(clueTmp);
			answer.add(answerTmp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
