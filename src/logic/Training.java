package logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Training {

	BufferedReader br;
	
	public Training() {
		
		try {
			br = new BufferedReader(new FileReader("phrases.txt"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getNextPhrase() {
		
		try {
			
			String line;
		    if ((line = br.readLine()) != null) {
		    	
		    	//TODO: augusto
		    	
		    	return line;
		    }
	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

}
