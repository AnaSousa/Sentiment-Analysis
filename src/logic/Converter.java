package logic;

import gui.ResultWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import settings.Constants;
import settings.Settings;

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
		ResultWindow window = new ResultWindow();
		window.frmResult.setVisible(true);

		new Runnable() {

			@Override
			public void run() {

				Runtime r = Runtime.getRuntime();
				Process proc;
				try {
					proc = r.exec(Settings.getProperty("classifier_command"));

					BufferedReader stdInput = new BufferedReader(new 
							InputStreamReader(proc.getInputStream()));

					BufferedReader stdError = new BufferedReader(new 
							InputStreamReader(proc.getErrorStream()));

					// read the output from the command
					System.out.println("Here is the standard output of the command:\n");
					String s = null;
					System.out.println("------------------------Start------------------------");

					ArrayList<Float> positives = new ArrayList<Float>();
					ArrayList<Float> neutrals = new ArrayList<Float>();
					ArrayList<Float> negatives = new ArrayList<Float>();

					while ((s = stdInput.readLine()) != null) {
						if (s.length() >= 39 && !s.trim().equals("inst#     actual  predicted error prediction")) {
							String result = s.substring(25, 28);
							float probability = Float.parseFloat(s.substring(35, s.length()-1));
							System.out.println(result + " - " + probability);

							switch(result) {
							case Constants.NEGATIVE:
								negatives.add(probability);
								break;
							case Constants.NEUTRAL:
								neutrals.add(probability);
								break;
							case Constants.POSITIVE:
								positives.add(probability);
								break;
							}
						}
						else
							System.out.println(s);

					}

					System.out.println("------------------------End------------------------");

					float neutral_total = (float) 0.0;
					float positive_total = (float) 0.0;
					float negative_total = (float) 0.0;

					for (int i = 0; i < positives.size() || i < negatives.size() || i < neutrals.size(); i++) {
						if(i < positives.size()) {
							positive_total += positives.get(i);
						}

						if(i < negatives.size()) {
							negative_total += negatives.get(i);
						}

						if(i < neutrals.size()) {
							neutral_total += neutrals.get(i);
						}
					}

					System.out.println("positive " + positives.size() + " -> " + positive_total / (positives.size() == 0 ? 1 : positives.size()));
					System.out.println("negative " + negatives.size() + " -> " + negative_total / (negatives.size() == 0 ? 1 : negatives.size()));
					System.out.println("neutral " + neutrals.size() + " -> " + neutral_total / (neutrals.size() == 0 ? 1 : neutrals.size()));

					String max = "";
					float percent = (float)0.0;
					if (positives.size() > negatives.size() && positives.size() > neutrals.size()) {
						max = Constants.POSITIVE;
						percent = positives.size();
					} 
					else if (negatives.size() > neutrals.size()) {
						max = Constants.NEGATIVE;
						percent = negatives.size();
					}
					else {
						max = Constants.NEUTRAL;
						percent = neutrals.size();
					}

					percent = percent / (positives.size() + negatives.size() + neutrals.size());

					window.setResult(max, percent);
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
		}.run();
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

