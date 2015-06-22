package logic;

import gui.MainWindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class Training {

	public class EvaluatedPhrase
	{
		public int value;
		public boolean has_value;
		public final String str;

		public EvaluatedPhrase(String s, int v) {
			value = v;
			str = s;
			has_value = true;
		}

		public EvaluatedPhrase(String s) {
			has_value = false;
			value = 0;
			str = s;
		}
	}

	private BufferedReader br;
	private String filename;
	private BufferedWriter bw;
	private Vector<String> phrasesNotEvaluated;
	private Vector<String> phrasesEvaluated;
	private Vector<String> initialSetup;
	private Vector<EvaluatedPhrase> phrasesEvaluatedByProlog;

	public Training() {

		try {

			//br = new BufferedReader(new FileReader("sun_5.arff"));
			filename = MainWindow.textArffLearning.getText();
			br = new BufferedReader(new FileReader(filename));
			System.out.println("Opening " + filename);

			PrologSocket.getInstance().connect();
			phrasesEvaluatedByProlog = new Vector<EvaluatedPhrase>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validateFile(String filenameTest) {

		int num_line = 1;
		BufferedReader br1 = null;
		
		try {
			
			br1 = new BufferedReader(new FileReader(filenameTest));
			
			String line;
			while ((line = br1.readLine()) != null) {

				if(num_line <= 6) {

					if(num_line == 1) {
						if(!line.contains("@relation "))
							return false;	
					} else if(num_line == 3) {
						if(!line.equals("@attribute text string"))
							return false;	
					} else if(num_line == 3) {
						if(!line.equals("@attribute @@class@@ {neg,pos,neu}"))
							return false;	
					} else if(num_line == 6) {
						if(!line.equals("@data"))
							return false;	
					}
					num_line++;
				}
				else {

					String[] parts = line.split("',");
					String phrase = parts[0];

					if(parts.length != 2)
						return false;
					
					if (phrase == null || phrase.length() == 0 || phrase.charAt(0) != '\'')
						return false;
				
					num_line++;
				}
			}

		} catch (Exception e) {
			return false;

		} finally {

			try {
				br1.close();
			} catch (IOException e) {
				
			}
		}

		if(num_line - 1 < 7)
			return false;
		
		return true;
	}

	public Vector<String> getPhrases() {

		initialSetup = new Vector<String>();
		phrasesEvaluated = new Vector<String>();
		phrasesNotEvaluated = new Vector<String>();

		int num_line = 1;

		try {
			String line;
			while ((line = br.readLine()) != null) {

				if(num_line <= 6) {
					initialSetup.addElement(line);
					num_line++;
				}
				else {

					String[] parts = line.split("',");
					String phrase = parts[0];
					String evaluation = parts[1];

					if (phrase != null && phrase.length() > 0 && phrase.charAt(0) == '\'') {
						phrase = phrase.substring(1, phrase.length());
					}

					if(evaluation.equals("?")) {
						phrasesNotEvaluated.add(phrase);
					} else {

						phrasesEvaluated.add(line);
					}
				}
			}

		} catch (Exception e) {

			//e.printStackTrace();
			System.out.println("File incorrect!");

		} finally {

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return phrasesNotEvaluated;
	}

	public EvaluatedPhrase getValue(int index, String phrase) {

		if(index < phrasesEvaluatedByProlog.size())
			return null;

		try {

			System.out.println(phrase);

			int value = PrologSocket.getInstance().evaluateSentence(phrase);
			EvaluatedPhrase ev = new EvaluatedPhrase(phrase, value);
			phrasesEvaluatedByProlog.addElement(ev);

			return ev;

		} catch (Exception e) {
			EvaluatedPhrase ev = new EvaluatedPhrase(phrase);
			phrasesEvaluatedByProlog.addElement(ev);

			return ev;
		}
	}

	public void saveToArffFile(Vector<EvaluatedPhrase> evaluated, Vector<String> notEvaluated) {



		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false)));

			for(int i = 0; i < initialSetup.size(); i++) {

				if(i != 0)
					bw.write("\n");

				bw.write(initialSetup.get(i));
			}

			for(int i = 0; i < phrasesEvaluated.size(); i++) {

				bw.write("\n" + phrasesEvaluated.get(i));
			}

			int num_evaluated = evaluated.size();
			for(int i = 0; i < notEvaluated.size(); i++) {

				if(i >= num_evaluated) {
					bw.write("\n'" + notEvaluated.get(i) + "',?");

				} else {

					int value = evaluated.get(i).value;
					String value_result = new String();

					if(value == 0)
						value_result = "neu";
					else if(value < 0)
						value_result = "neg";
					else
						value_result = "pos";

					bw.write("\n'" + evaluated.get(i).str + "'," + value_result);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				bw.close();
			}
			catch (Exception ex) {}
		}
	}
}
