package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
	final Path PATH = FileSystems.getDefault().getPath("status.txt");
	String topic;

	public Converter(String topic) {
		this.topic = topic;
	}

	public void generateArff() {
		try {
			PrintWriter writer = new PrintWriter("statuses.arff", "UTF-8");

			writer.println("@relation " + topic + "\n");
			writer.println("@attribute text string");
			writer.println("@attribute @@class@@ {neg,pos}\n");
			writer.println("@data");

			for (String line : Files.readAllLines(PATH)) {

				String escapedLine = org.apache.commons.lang3.StringEscapeUtils.escapeJava(
						removeUrl(line).replaceAll("\u2018", "").replaceAll("\u2019", "").replaceAll("'", "\\'")).replaceAll("'", " ");
				if(escapedLine.split(" ").length > 10)
					writer.println("\'" + escapedLine + "\',?");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void classify() {
		Runtime r = Runtime.getRuntime();
		Process proc;
		try {
			proc = r.exec("java -cp ./lib/weka.jar weka.classifiers.trees.RandomForest -p 0 -l text_n_sentencesRF.model -T statuses.arff");

			BufferedReader stdInput = new BufferedReader(new 
					InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(proc.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	//Input the String that contains the url

	private String removeUrl(String commentstr)
	{
		String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(commentstr);
		int i = 0;
		while (m.find()) {
			commentstr = commentstr.replaceAll(m.group(i),"").trim();
			i++;
		}
		return commentstr;
	}


}

