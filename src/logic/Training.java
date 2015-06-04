package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Training {
	
	public class EvaluatedPhrase
	{
		public final int value;
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
	
	BufferedReader br;
	
	public Training() {
		
		try {
			br = new BufferedReader(new FileReader("status.txt"));
			PrologSocket.getInstance().connect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Vector<EvaluatedPhrase> getPhrases() {
		
		Vector<EvaluatedPhrase> phrases = new Vector<EvaluatedPhrase>();
		
		try {
			String line;
		    while ((line = br.readLine()) != null) {
		    	try {
					int value = PrologSocket.getInstance().evaluateSentence(line);
			    	phrases.add(new EvaluatedPhrase(line, value));
			    	
				} catch (Exception e) {
					phrases.add(new EvaluatedPhrase(line));
				}
		    	
		    }
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return phrases;
	}

}
