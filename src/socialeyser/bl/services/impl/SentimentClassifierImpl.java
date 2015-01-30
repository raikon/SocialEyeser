 package socialeyser.bl.services.impl;
 
 import java.io.IOException;
 import java.util.Random;
 import javax.annotation.PostConstruct;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import socialeyser.bl.services.impl.sa.modsupport.Analysis;
 import socialeyser.bl.services.interfaces.SentimentClassifier;
 import socialeyser.model.Enrichment;
 import socialeyser.model.Message;
 
 public class SentimentClassifierImpl
   implements SentimentClassifier
 {
   private static final Logger log = LoggerFactory.getLogger(SentimentClassifierImpl.class);
   static final int POSITIVE = 1;
   static final int NEGATIVE = -1;
   static final int NEUTRAL = 0;
   private Analysis analysis;
   
   @PostConstruct
   public void init()
   {
     log.info("started parsing dictionary");
     this.analysis = new Analysis();
     log.info("done parsing file");
   }
   
   public Message classifyMessage(Message message)
   {
     Random rand = new Random();
     
     double classifyresult = rand.nextDouble();
     int evaluation;
     
     if (classifyresult < 0.33D)
     {
       evaluation = 1;
     }
     else
     {
       
       if (classifyresult < 0.66D) {
         evaluation = -1;
       } else {
         evaluation = 0;
       }
     }
     try
     {
       message.getEnrichment().setSemanticClassification(this.analysis.sentiment(message));
     }
     catch (IOException e)
     {
       e.printStackTrace();
       throw new RuntimeException(e);
     }
     return message;
   }
   
   private int scaleResult(double sentiment)
   {
     log.info("sentiment result:" + sentiment);
     int result = (int)Math.round(sentiment / 2.0D);
     log.info("rounded to:" + result);
     
     return result;
   }
 }




