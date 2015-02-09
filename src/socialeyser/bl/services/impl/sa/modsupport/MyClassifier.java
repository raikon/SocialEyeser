package socialeyser.bl.services.impl.sa.modsupport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import socialeyser.model.Message;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class MyClassifier {
	
	private Set<Message> classSet;
	private FilteredClassifier model;
	private String[] classList;
	private String defClass;
	
	private Map<Long, String> labels;
	private Instances classInstances;
	
	public MyClassifier() {}
	
	public MyClassifier(Set<Message> classSet, FilteredClassifier model, String[] classList, String defClass) {
		this.classSet = classSet;
		this.model = model;
		this.classList = classList;
		this.defClass = defClass;
		this.labels = new HashMap<>();
	}
	
	public MyClassifier(FilteredClassifier model, String[] classList, String defClass) {
		this.model = model;
		this.classList = classList;
		this.defClass = defClass;
		this.classInstances = prepareInstances();
	}
	
	private Instances prepareInstances() {
		ArrayList<Attribute> atts = new ArrayList<>();
		atts.add(new Attribute("Content", (List<String>) null));
		List<String> classes = new ArrayList<>();
		for (String cl : this.classList) {
			classes.add(cl);
		}
		atts.add(new Attribute("Class", classes));
		Instances inst = new Instances("Instances", atts, 0);
		inst.setClassIndex(1);
		return inst;
	}
	
	public Map<Long, String> makeClassification() throws Exception {
		ARFFMaker maker = new ARFFMaker();
		String arff = maker.makeARFF_classify(this.classSet, this.classList);
		this.loadDataset(arff);
		String[] classResults = classify();
		
		int i=0;
		for (Message mess: this.classSet) {
			Long code = mess.getRawMessage().getTweetId();
			this.labels.put(code, classResults[i]);
			i++;
		}
		
		return this.labels;
	}
	
//	public Result makeResults() {
//		Result res = new Result(this.labels, this.classList);
//		TweetStatsMaker2 stats = new TweetStatsMaker2(res);
//		
//		stats.generateTweetCounts();
//		stats.generateTweetPercentages();
//		
//		return res;
//	}
	
	
	private void loadDataset(String arffPath) {
		try {
			//Lettura dei dati di training dal file ARFF trainer
			BufferedReader reader = new BufferedReader(new FileReader(arffPath));
			ArffReader arff_classify = new ArffReader(reader);
			this.classInstances = arff_classify.getData();
			this.classInstances.setClassIndex(1);
			System.out.println("===== Loaded dataset: " + arffPath + " =====");
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Problem found when reading datasets");
		}
	}
	
	public String classifyTweet(Message mess) throws Exception {
		String sentiment = null;
		String tweet;
		Instance inst = new DenseInstance(2);
		inst.setValue(0, mess.getEnrichment().getNormText());
		this.classInstances.add(inst);
		try {
			double pred = this.model.classifyInstance(this.classInstances.instance(0));
			double[] distributions = this.model.distributionForInstance(this.classInstances.instance(0));
			
			tweet = this.classInstances.attribute(0).value(1);
			if (tweet.equals("") || tweet.equals(" "))
				sentiment = this.defClass;
			else
				sentiment = this.classInstances.classAttribute().value((int) pred);
			
			System.out.println("Instance: " + tweet);
			System.out.println("Class predicted: " + sentiment);
			
			double predictionValue = distributions[(int) pred];
			System.out.println("Distribution: " + predictionValue + "\n");
		}
		catch (Exception e) {
			System.out.println("Problem found when classifying the text");
			e.printStackTrace();
		}
		
		//Cancella il contenuto dell'oggetto Instances quando il tweet è stato classificato
		this.classInstances.delete();
		
		return sentiment;
	}
	
	private String[] classify() throws Exception{
		String[] sentiments = new String[this.classInstances.numInstances()];
		int j=1;
		String sentiment,tweet;
		System.out.println("QUI: "+this.classInstances.numInstances());
		try {
			System.out.println("\n Number of valid instances analyzed: "+this.classInstances.numInstances()+"\n");
			for(int k=0;k < this.classInstances.numInstances();k++) {

				System.out.println("===== Classified instance =====");
				
				double pred = this.model.classifyInstance(this.classInstances.instance(k));
				//Queste sono le distribuzioni per ogni etichetta.
				//Quanto il classificatore è sicuro che l'elemento appartenga a ciascuna delle classi
				double[] distributions = this.model.distributionForInstance(this.classInstances.instance(k));
				
				//Il file ARFF si scandisce per attributo e valore
				tweet = this.classInstances.attribute(0).value(j);
				/*INSERIRE QUI eventuali forzature di predizione a causa delle emoticon */
				if(tweet.equals("") || tweet.equals(" ")) 
					sentiment = this.defClass;
				else
					sentiment = this.classInstances.classAttribute().value((int) pred);
				
				System.out.println("Instance("+k+"): "+tweet);
				System.out.println("Class predicted: " + sentiment);
				
				/*stampa dei valori di predizione*/
				double predictionValue = distributions[(int) pred];
				System.out.println("Distribution: "+predictionValue+"\n");
				
				sentiments[k] = sentiment;
				j++;
			}
		}
		catch (Exception e) {
			System.out.println("Problem found when classifying the text");
			e.printStackTrace();
		}
		return sentiments;
	}
}