package logic;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.classifiers.bayes.NaiveBayesUpdateable;

import java.io.File;
import java.io.IOException;


public class Analysis {

	public Analysis() {
		startWeka();
		
	}

	private void startWeka() {
		
		ArffLoader loader = new ArffLoader();
	    String x = "test.arff";
	    
		try {
			loader.setFile(new File(x));
			Instances structure = loader.getStructure();
		    structure.setClassIndex(structure.numAttributes() - 1);

		    // train NaiveBayes
		    NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
		    nb.buildClassifier(structure);
		    Instance current;
		    while ((current = loader.getNextInstance(structure)) != null)
		      nb.updateClassifier(current);

		    // output generated model
		    System.out.println(nb);
		    
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		
	}

}
