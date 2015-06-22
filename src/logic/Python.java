package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Python {

	static public boolean getData(String theme, String noResults, String savePath) throws IOException, InterruptedException {
		System.out.println("Python starting...");

		Runtime r = Runtime.getRuntime();
		Process p = r.exec("python ./twitterPython/app.py " + theme + " " + noResults);
		p.waitFor();
		/*BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
				System.out.println(line);
			}*/

		BufferedReader stdInput = new BufferedReader(new 
				InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(p.getErrorStream()));

		System.out.println("Here is the standard output of the command:\n");
		String s = null;
		boolean ok = true;

		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}


		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
			ok = false;
		}

		stdInput.close();
		stdError.close();

		System.out.println("Python ended");

		if(!ok)
			return ok;

		System.out.println("Generating arff...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		WekaConnection.generateArff(savePath, theme);
		System.out.println("Generating arff ended");
		
		return ok;
	}
}
