 package socialeyser.storm.bolt;
 
 import backtype.storm.task.OutputCollector;
 import backtype.storm.task.TopologyContext;
 import backtype.storm.topology.OutputFieldsDeclarer;
 import backtype.storm.topology.base.BaseRichBolt;
 import backtype.storm.tuple.Fields;
 import backtype.storm.tuple.Tuple;
 import backtype.storm.tuple.Values;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.context.support.ClassPathXmlApplicationContext;
 import socialeyser.bl.services.interfaces.InfluenceEvaluator;
 import socialeyser.bl.services.interfaces.PersistenceService;
 import socialeyser.model.Message;
 import socialeyser.model.RawMessage;
 
 public class InfluenceEvaluatorBolt
   extends BaseRichBolt
 {
   private static final Logger logger = LoggerFactory.getLogger(InfluenceEvaluatorBolt.class);
   private static final long serialVersionUID = -1515974725983480044L;
   private InfluenceEvaluator influenceEvaluator;
   private OutputCollector collector;
   private PersistenceService persistenceService;
   private ClassPathXmlApplicationContext appContext;
   private String errorStreamId;
   private String springContextFileName;
   
   public InfluenceEvaluatorBolt(String errorstreamid, String springContextFileName)
   {
     setErrorStreamId(errorstreamid);
     setSpringContextFileName(springContextFileName);
   }
   
   public void execute(Tuple input)
   {
     RawMessage rawMessage = (RawMessage)input.getValueByField("rawMessage");
     
     Message message = this.influenceEvaluator.evaluateInfluence(rawMessage);
     this.collector.emit(input, new Values(new Object[] { message }));
     this.collector.ack(input);
   }
   
   public void declareOutputFields(OutputFieldsDeclarer declarer)
   {
     declarer.declare(new Fields(new String[] { "message" }));
     declarer.declareStream(getErrorStreamId(), new Fields(new String[] { "error" }));
   }
   
   public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
   {
     this.collector = collector;
     logger.info("started creating context");
     setAppContext(new ClassPathXmlApplicationContext(getSpringContextFileName()));
     logger.info("context created");
     setInfluenceEvaluator((InfluenceEvaluator)getAppContext().getBean("UserInfluenceEvaluationService"));
     setPersistenceService((PersistenceService)getAppContext().getBean("PersistenceService"));
   }
   
   public InfluenceEvaluator getInfluenceEvaluator()
   {
     return this.influenceEvaluator;
   }
   
   public void setInfluenceEvaluator(InfluenceEvaluator influenceEvaluator)
   {
     this.influenceEvaluator = influenceEvaluator;
   }
   
   public PersistenceService getPersistenceService()
   {
     return this.persistenceService;
   }
   
   public void setPersistenceService(PersistenceService persistenceService)
   {
     this.persistenceService = persistenceService;
   }
   
   public void cleanup() {}
   
   public Map<String, Object> getComponentConfiguration()
   {
     return null;
   }
   
   public ClassPathXmlApplicationContext getAppContext()
   {
     return this.appContext;
   }
   
   public void setAppContext(ClassPathXmlApplicationContext appContext)
   {
     this.appContext = appContext;
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
 }




