package socialeyser.bl.services.impl.sa.modsupport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import socialeyser.model.Message;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MyTrainer {
	
	private Set<Message> trainSet;
	private String[] classList;
	
	private Instances trainingData;
	private StringToWordVector filter;
	private FilteredClassifier classifier;
	private Evaluation eval;
//	private CrossValResult crossValidation;
	
	public MyTrainer() {}
	
	public MyTrainer(Set<Message> trainSet, String[] classList) {
		this.trainSet = trainSet;
		this.classList = classList;
	}
	
	public FilteredClassifier train() throws Exception {
		ARFFMaker maker = new ARFFMaker();
		String arff = maker.makeARFF_training(this.trainSet, this.classList);
		this.loadDataset(arff);
		this.evaluate();
		this.learn();
		
		return this.classifier;
	}
	
//	public Result makeResults() {
//		Map<Integer, String> labels = this.trainSet.extractLabels();
//		Result r = new Result(labels, this.classList);
//		TweetStatsMaker2 stats = new TweetStatsMaker2(r);
//		stats.generateTweetCounts();
//		stats.generateTweetPercentages();
//		stats.generateCrossValidation(this.eval, this.classList);
//
//		return r;	
//	}
	
	private void loadDataset(String arffPath) {
		try {
			//Lettura dei dati di training dal file ARFF trainer
			BufferedReader reader = new BufferedReader(new FileReader(arffPath));
			ArffReader arff_train = new ArffReader(reader);
			this.trainingData = arff_train.getData();
			System.out.println("===== Loaded dataset: " + arffPath + " =====");
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Problem found when reading datasets");
		}
	}
	
	private void learn() {
		try {
			trainingData.setClassIndex(trainingData.numAttributes() - 1);

			filter = new StringToWordVector();
			filter.setAttributeIndices("first");
			
			/*Utilizzo di n-gram diversi: default n=1, quindi se filter.setTokenzer disattivato n=1*/
			NGramTokenizer tokenizer = new NGramTokenizer(); 
			String[] options = new String[6]; 
			options[0] = "-max"; 
			options[1] = "1"; 
			options[2] = "-min"; 
			options[3] = "1"; 
			options[4] = "-delimiters"; 
			options[5] = " \r"; 
			tokenizer.setOptions(options);
			//filter.setTokenizer(tokenizer);
			
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);		
			classifier.setClassifier(new NaiveBayesMultinomial());
			classifier.buildClassifier(trainingData);
			// Uncomment to see the classifier
			// System.out.println(classifier);

			System.out.println("===== Training on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			System.out.println("Problem found when training");
			System.out.println(e);
		}
	}
	
	private void evaluate() {
		try {
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			filter = new StringToWordVector();	
			filter.setAttributeIndices("first");
			
			/*Utilizzo di n-gram diversi: default n=1, quindi se filter.setTokenzer disattivato n=1*/
			NGramTokenizer tokenizer = new NGramTokenizer(); 
			String[] options = new String[6]; 
			options[0] = "-max"; 
			options[1] = "1"; 
			options[2] = "-min"; 
			options[3] = "1"; 
			options[4] = "-delimiters"; 
			options[5] = " \r"; 
			tokenizer.setOptions(options);
			filter.setTokenizer(tokenizer);
			
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			classifier.setClassifier(new NaiveBayesMultinomial());
		      
			this.eval = new Evaluation(trainingData);
			
			/*Necessario per stampare i dettagli di predizione del valutatore*/
			StringBuffer output = new StringBuffer();
			AbstractOutput printout = new CSV(); 
		    printout.setBuffer(output); 
		    printout.setAttributes("1");
			
			this.eval.crossValidateModel(classifier, trainingData, 10, new Random(1),printout, null, true);
			
			//TRACE
			System.out.println(this.eval.toSummaryString());
			System.out.println(this.eval.toClassDetailsString());
			
			/*stampa valori di predizione*/
			System.out.println(output); 
		
			createConfusionMatrix(this.eval);
			
			System.out.println("===== Evaluating on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			System.out.println("Problem found when evaluating");
			System.out.println(e);
		}
	}

	public static void createConfusionMatrix(Evaluation eval) {
		double[][] cmMatrix = eval.confusionMatrix();
		System.out.println();
		System.out.println("== Confusion Matrix =="+"\n");
		System.out.println(" a      b     c");
		for(int row_i=0; row_i<cmMatrix.length; row_i++){
			for(int col_i=0; col_i<cmMatrix.length; col_i++){
				System.out.print(cmMatrix[row_i][col_i]+"  ");  
			}
			System.out.print("|");
			if (row_i==0)
				System.out.println("a = positive");
			if (row_i==1)
				System.out.println("b = negative");
			if (row_i==2) 
				System.out.println("c = neutral");
		}
		System.out.println("\n");
	}
	
//	public CrossValResult getCrossvalidation() {
//		return this.crossValidation;
//	}
}
