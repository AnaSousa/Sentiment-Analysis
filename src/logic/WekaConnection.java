package logic;

import gui.IMainPanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import settings.Constants;
import settings.Settings;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaConnection {
	final static Path PATH = FileSystems.getDefault().getPath("statuses.txt");
	String topic;

	public static void generateArff(String savePath, String theme) {
		try {
			PrintWriter writer = new PrintWriter(savePath, "UTF-8");

			writer.println("@relation " + theme + "\n");
			writer.println("@attribute text string");
			writer.println("@attribute @@class@@ {neg,pos,neu}\n");
			writer.println("@data");

			for (String line : Files.readAllLines(PATH)) {

				String escapedLine = org.apache.commons.lang3.StringEscapeUtils.escapeJava(
						removeUrl(line).replaceAll("\u2018", "").replaceAll("\u2019", "").replaceAll("'", "\\'")).replaceAll("'", " ");
				if(escapedLine.split(" ").length > 6)
					writer.println("\'" + escapedLine + "\',?");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void classify(String modelPath, String arffPath, IMainPanel panelResult) throws Exception {
		panelResult.setLoading();
			
		
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(modelPath);
		
		Instances inst = new Instances(
				new BufferedReader(
						new FileReader(arffPath)));

		inst.setClassIndex(inst.numAttributes() - 1);
		
		int quantity[] = new int[3];
		for (int i = 0; i < inst.numInstances(); i++) {
			quantity[(int)cls.classifyInstance(inst.instance(i))]++; 
		}

		float percentage = (float) 0.0;
		String max = "";
		
		if(quantity[0] > quantity[1] && quantity[0] > quantity[2]) {
			percentage = (float) (quantity[0] * 1.0 / inst.numInstances());
			max = Constants.NEGATIVE;
		}
		else if (quantity[1] > quantity[2]) {
			percentage = (float) (quantity[1] * 1.0 / inst.numInstances());
			max = Constants.POSITIVE;
		}
		else {
			percentage = (float) (quantity[2] * 1.0 / inst.numInstances());
			max = Constants.NEUTRAL;
		}
		

		panelResult.setResult(max, percentage);
	}

	public static void generateModel(String modelPath, String arffPath, IMainPanel panelResult) throws Exception {
		panelResult.setLoading();
		BufferedReader arffFile = new BufferedReader(new FileReader(arffPath));

		PrintWriter writer = new PrintWriter("tempfile.arff", "UTF-8");

		writer.println("@relation temp\n");
		writer.println("@attribute text string");
		writer.println("@attribute @@class@@ {neg,pos,neu}\n");
		writer.println("@data");
		String line;
		int count = 0;

		while((line = arffFile.readLine()) != null) {
			if(line.trim().length() > 0 && line.charAt(0) == '\'') {
				if(line.trim().charAt(line.trim().length() - 1) == '?') 
					break;

				writer.println(line.trim());

				count++;
			}
		}



		arffFile.close();
		writer.flush();
		writer.close();

		if(count == 0) {
			new File("tempfile.arff").delete();
			return;
		}

		FilteredClassifier  fc = new FilteredClassifier();
		fc.setFilter(new MultiFilter());

		MultiFilter mf = new MultiFilter();

		StringToWordVector swv;

		swv=new StringToWordVector();
		swv.setOptions(Utils.splitOptions(Settings.getProperty("model_string_to_word_vector")));

		AttributeSelection as = new AttributeSelection();
		as.setOptions(Utils.splitOptions(Settings.getProperty("model_attribute_selection")));

		Filter filters[]={swv, as};

		mf.setFilters(filters);

		fc.setFilter(mf);


		Class<?> classDefinition = Class.forName(Settings.getProperty("model_classifier"));
		Object object = classDefinition.newInstance();

		Classifier cl = (Classifier) object;
		cl.setOptions(Utils.splitOptions(Settings.getProperty("model_classifier_options")));

		System.out.println("Classifier: " + cl.getClass().toString() + " args: " + Settings.getProperty("model_classifier_options"));

		fc.setClassifier(cl);

		Instances inst = new Instances(
				new BufferedReader(
						new FileReader(arffPath)));

		inst.setClassIndex(inst.numAttributes() - 1);
		fc.buildClassifier(inst);

		weka.core.SerializationHelper.write(modelPath, fc);

		new File("tempfile.arff").delete();

		panelResult.setMessage("Generated Model with success", Constants.SUCCESS);
	}


	//Input the String that contains the url

	private static String removeUrl(String commentstr)
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

