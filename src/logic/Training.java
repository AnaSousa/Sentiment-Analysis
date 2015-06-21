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
	private Vector<EvaluatedPhrase> phrases;
	
	public Training() {
		
		try {
			br = new BufferedReader(new FileReader("status.txt"));
			PrologSocket.getInstance().connect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Vector<EvaluatedPhrase> getPhrases() {
		
		phrases = new Vector<EvaluatedPhrase>();
		
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

	public void saveToArffFile(Vector<EvaluatedPhrase> result) {
		
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.arff")));
			
			bw.write("@relation ");
			bw.write("sun\n\n"); //TODO:
			bw.write("@attribute text string\n" +
					"@attribute @@class@@ {neg,neu,pos}\n\n"
					+ "@data\n"); 
			
			for(int i = 0; i < phrases.size(); i++) {
				
				int value = result.get(i).value;
				String value_result = new String();
				
				if(value == 0)
					value_result = "neu";
				else if(value < 0)
					value_result = "neg";
				else
					value_result = "pos";
				
				bw.write("'" + result.get(i).str + "','" + value_result + "'\n");
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
