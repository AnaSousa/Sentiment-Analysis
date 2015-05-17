package logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
			br = new BufferedReader(new FileReader("phrases.txt"));
			PrologSocket.getInstance().connect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EvaluatedPhrase getNextPhrase() {
		
		try {
			String line;
		    if ((line = br.readLine()) != null) {
		    	try {
					int value = PrologSocket.getInstance().evaluateSentence(line);
			    	return new EvaluatedPhrase(line, value);
				} catch (Exception e) {
			    	return new EvaluatedPhrase(line);
				}
		    	
		    }
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

}
