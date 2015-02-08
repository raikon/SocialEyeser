 package socialeyser.storm.bolt;
 
 import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import socialeyser.bl.services.interfaces.PersistenceService;
import socialeyser.bl.services.interfaces.SentimentClassifier;
import socialeyser.bl.services.interfaces.WebAppRequestGateway;
import socialeyser.model.Enrichment;
import socialeyser.model.Message;
import socialeyser.model.RawMessage;
import socialeyser.model.exception.PersistenceException;
 
 public class SentimentClassifierBolt
   extends BaseRichBolt
 {
   private static final Logger logger = LoggerFactory.getLogger(SentimentClassifierBolt.class);
   private static final long serialVersionUID = -5396839342136498882L;
   private SentimentClassifier classifier;
   private OutputCollector collector;
   private ClassPathXmlApplicationContext appContext;
   private PersistenceService persistenceService;
   private WebAppRequestGateway webAppRequestGateway;
   private String errorStreamId;
   private String springContextFileName;
   //Campo che contiene l'insieme di addestramento: va settato esternamente con l'apposito metodo setter
   private Set<Message> trainingSet;
   
   public SentimentClassifierBolt(String errorstreamid, String springContextFileName)
   {
     setErrorStreamId(errorstreamid);
     setSpringContextFileName(springContextFileName);
   }
   
   public void execute(Tuple tuple)
   {
     Message message = (Message)tuple.getValueByField("message");
     
     try {
    	 message = this.classifier.classifyMessage(message, this.trainingSet);
     }
     catch (Exception e) {
    	 logger.info("Classification error");
    	 e.printStackTrace();
     }
     
     logger.info(message.getRawMessage().getTweetText());
     logger.info("user influence:" + message.getEnrichment().getUserInfluence());
     logger.info("sentiment predicted:" + message.getEnrichment().getSentiment());
     try
     {
       getPersistenceService().writeMessage(message);
       getCollector().emit(tuple, new Values(new Object[] { message }));
     }
     catch (PersistenceException e)
     {
       getCollector().emit(getErrorStreamId(), tuple, new Values(new Object[] { e.getMessage() }));
     }
     getCollector().ack(tuple);
   }
   
   public void declareOutputFields(OutputFieldsDeclarer declarer)
   {
     declarer.declare(new Fields(new String[] { "message" }));
     declarer.declareStream(getErrorStreamId(), new Fields(new String[] { "error" }));
   }
   
   public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
   {
     setCollector(collector);
     logger.info("started creating context");
     setAppContext(new ClassPathXmlApplicationContext(getSpringContextFileName()));
     logger.info("context created");
     this.classifier = ((SentimentClassifier)getAppContext().getBean("SentimentClassificationService"));
     setPersistenceService((PersistenceService)getAppContext().getBean("PersistenceService"));
     this.webAppRequestGateway = ((WebAppRequestGateway)getAppContext().getBean("webAppRequestGateway"));
   }
   
   public void cleanup() {}
   
   public Map<String, Object> getComponentConfiguration()
   {
     return null;
   }
   
   public OutputCollector getCollector()
   {
     return this.collector;
   }
   
   public void setCollector(OutputCollector collector)
   {
     this.collector = collector;
   }
   
   public ClassPathXmlApplicationContext getAppContext()
   {
     return this.appContext;
   }
   
   public void setAppContext(ClassPathXmlApplicationContext appContext)
   {
     this.appContext = appContext;
   }
   
   public PersistenceService getPersistenceService()
   {
     return this.persistenceService;
   }
   
   public void setPersistenceService(PersistenceService persistenceService)
   {
     this.persistenceService = persistenceService;
   }
   
   public String getErrorStreamId()
   {
     return this.errorStreamId;
   }
   
   public void setErrorStreamId(String errorStreamId)
   {
     this.errorStreamId = errorStreamId;
   }
   
   public String getSpringContextFileName()
   {
     return this.springContextFileName;
   }
   
   public void setSpringContextFileName(String springContextFileName)
   {
     this.springContextFileName = springContextFileName;
   }

	public Set<Message> getTrainingSet() {
		return trainingSet;
	}
	
	public void setTrainingSet(Set<Message> trainingSet) {
		this.trainingSet = trainingSet;
	}
   
 }