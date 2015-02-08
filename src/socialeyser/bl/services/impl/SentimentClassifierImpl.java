 package socialeyser.bl.services.impl;
 
 import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import socialeyser.bl.services.impl.sa.modsupport.MessageSets;
import socialeyser.bl.services.impl.sa.modsupport.MyClassifier;
import socialeyser.bl.services.impl.sa.modsupport.MyTrainer;
import socialeyser.bl.services.interfaces.SentimentClassifier;
import socialeyser.model.Enrichment;
import socialeyser.model.Message;
import weka.classifiers.meta.FilteredClassifier;
 
 public class SentimentClassifierImpl
   implements SentimentClassifier
 {
   private static final Logger log = LoggerFactory.getLogger(SentimentClassifierImpl.class);
   static final int POSITIVE = 1;
   static final int NEGATIVE = -1;
   static final int NEUTRAL = 0;
      
   public Message classifyMessage(Message message, Set<Message> trainingSet) throws Exception
   {
	   String[] classList = {"positive", "negative", "neutral"};
	   Properties prop = new Properties();
	   prop.load(new FileReader(new File("etc/conf/wekaClassifier.properties")));
	   
	   Set<Message> normTraining = MessageSets.normalizedSet(trainingSet, prop);
	   
	   MyTrainer tr = new MyTrainer(normTraining, classList);
	   FilteredClassifier model = tr.train();
	   MyClassifier cl = new MyClassifier(model, classList, "neutral");
	   
	   Message normMessage = MessageSets.normalizedMess(message, prop);
	   String sentiment = cl.classifyTweet(normMessage);
	   
	   normMessage.getEnrichment().setSentiment(sentiment);
	   
	   return normMessage;
   }
   
   private int scaleResult(double sentiment)
   {
     log.info("sentiment result:" + sentiment);
     int result = (int)Math.round(sentiment / 2.0D);
     log.info("rounded to:" + result);
     
     return result;
   }
 }




