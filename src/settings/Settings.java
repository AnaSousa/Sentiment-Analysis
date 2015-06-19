package settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Settings {
	private static final File configFile = new File("config.properties");
	private static Properties configProps = null;

	public static void loadProperties() {
		Properties defaultProps = new Properties();
		// sets default properties
		defaultProps.setProperty("classifier_command", "java -cp ./lib/weka.jar weka.classifiers.trees.RandomForest -p 0 -l text_n_sentencesRF.model -T statuses.arff");

		configProps = new Properties(defaultProps);

		// loads properties from file
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(configFile);
			configProps.load(inputStream);
			inputStream.close();
		} catch (FileNotFoundException e) {
			//Create configuration file if doesnt exist
			createConfigFile(defaultProps);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void createConfigFile(Properties defaultProps) {
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(configFile);
			defaultProps.store(outputStream, "App Settings");
			outputStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.err.println("Creating configuration file...");
	}

	public static String getProperty(String property) {
		if (configProps == null)
			loadProperties();

		return configProps.getProperty(property);
	}
}
