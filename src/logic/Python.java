package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Python {
	
	static public void getData(String theme, String noResults, String savePath) throws IOException, InterruptedException {
		System.out.println("Python starting...");
		
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("python ./twitterPython/app.py " + theme + " " + noResults);
			p.waitFor();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
				System.out.println(line);
			}

			b.close();

		System.out.println("Python ended");
		
		
		System.out.println("Generating arff...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		WekaConnection.generateArff(savePath, theme);
		System.out.println("Generating arff ended");
	}
}
