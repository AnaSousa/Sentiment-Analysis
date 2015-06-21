package logic;

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
		public final boolean has_value;
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
	private BufferedWriter bw;
	private Vector<EvaluatedPhrase> phrasesNotEvaluated;
	private Vector<String> phrasesEvaluated;
	private Vector<String> initialSetup;

	public Training() {

		try {
			br = new BufferedReader(new FileReader("sun_5.arff"));
			PrologSocket.getInstance().connect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Vector<EvaluatedPhrase> getPhrases() {

		initialSetup = new Vector<String>();
		phrasesEvaluated = new Vector<String>();
		phrasesNotEvaluated = new Vector<EvaluatedPhrase>();

		int num_line = 1;

		try {
			String line;
			while ((line = br.readLine()) != null) {

				if(num_line <= 6) {
					initialSetup.addElement(line);
					num_line++;
				}
				else {

					System.out.println(line);

					String[] parts = line.split("',");
					String phrase = parts[0];
					String evaluation = parts[1];

					if (phrase != null && phrase.length() > 0 && phrase.charAt(0) == '\'') {
						phrase = phrase.substring(1, phrase.length());
					}

					System.out.println(phrase);
					System.out.println(evaluation);

					if(evaluation.equals("?")) {
						try {
							int value = PrologSocket.getInstance().evaluateSentence(phrase);
							phrasesNotEvaluated.add(new EvaluatedPhrase(phrase, value));

						} catch (Exception e) {
							phrasesNotEvaluated.add(new EvaluatedPhrase(phrase));
						}
					} else {

						phrasesEvaluated.add(line);
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		return phrasesNotEvaluated;
	}

	public void saveToArffFile(Vector<EvaluatedPhrase> result, int num_evaluated) {

		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.arff")));

			/*bw.write("@relation ");
			bw.write("sun\n\n"); //TODO:
			bw.write("@attribute text string\n" +
					"@attribute @@class@@ {neg,neu,pos}\n\n"
					+ "@data\n"); */

			for(int i = 0; i < initialSetup.size(); i++) {

				if(i != 0)
					bw.write("\n");

				bw.write(initialSetup.get(i));
			}

			for(int i = 0; i < phrasesEvaluated.size(); i++) {

				bw.write("\n" + phrasesEvaluated.get(i));
			}

			for(int i = 0; i < result.size(); i++) {

				if(i > num_evaluated) {
					bw.write("\n'" + result.get(i).str + "',?");
					
				} else {
					
					int value = result.get(i).value;
					String value_result = new String();

					if(value == 0)
						value_result = "neu";
					else if(value < 0)
						value_result = "neg";
					else
						value_result = "pos";

					bw.write("\n'" + result.get(i).str + "'," + value_result);
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
