 package socialeyser.bl.services.impl.sa.modsupport;
 
 import java.io.IOException;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import socialeyser.bl.services.impl.sa.persistence.SentimentAnalyser;
 import socialeyser.model.Message;
 import socialeyser.model.RawMessage;
 
 public class Analysis
   implements SentimentAnalyser
 {
   private static final Logger logger = LoggerFactory.getLogger(Analysis.class);
   private Lexicon lexicon = new Lexicon();
   
   public double sentiment(Message message)
     throws IOException
   {
     Double currentValue = Double.valueOf(0.0D);
     Tokenizer tokenizer = new Tokenizer();
     
 
 
     Double score = Double.valueOf(0.0D);
     String[] words = message.getRawMessage().getTweetText().split("\\s+");
     logger.info("message to split:   " + message.getRawMessage().getTweetText());
     for (String word : words)
     {
       logger.info("word to evaluate: " + word);
       word = word.replaceAll("([^a-zA-Z\\s])", "");
       logger.info("word to evaluate with characters replacement:  " + word);
       
 
 
 
       currentValue = this.lexicon.getSentiment(word);
       logger.info("adding value: " + currentValue.toString());
       score = Double.valueOf(score.doubleValue() + currentValue.doubleValue());
     }
     logger.info("total score: " + score);
     logger.info("\n");
     return score.doubleValue();
   }
 }




