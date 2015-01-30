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
 import socialeyser.bl.services.interfaces.PersistenceService;
 import socialeyser.model.RawMessage;
 import socialeyser.model.exception.PersistenceException;
 
 public class PersistenceBolt
   extends BaseRichBolt
 {
   private static final Logger logger = LoggerFactory.getLogger(PersistenceBolt.class);
   private static final long serialVersionUID = 1785565263445961830L;
   private OutputCollector collector;
   private PersistenceService persistence;
   private ClassPathXmlApplicationContext appContext;
   private String errorStreamId;
   private String springContextFileName;
   
   public PersistenceBolt(String errorstreamid, String springContextFileName)
   {
     setSpringContextFileName(springContextFileName);
     setErrorStreamId(errorstreamid);
   }
   
   public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
   {
     setCollector(collector);
     logger.info("started creating context");
     setAppContext(new ClassPathXmlApplicationContext(getSpringContextFileName()));
     logger.info("context created");
     setPersistenceService((PersistenceService)getAppContext().getBean("PersistenceService"));
   }
   
   public void execute(Tuple input)
   {
     logger.info("Persistence bolt id:" + input.getMessageId());
     RawMessage rawMessage = (RawMessage)input.getValueByField("rawMessage");
     logger.info("inside persistence bolt,message text:" + rawMessage.getTweetText());
     try
     {
       getPeristenceService().writeRawMessage(rawMessage);
       getCollector().emit(input, new Values(new Object[] { rawMessage }));
       getCollector().ack(input);
     }
     catch (PersistenceException e)
     {
       getCollector().emit(getErrorStreamId(), input, new Values(new Object[] { e.getMessage() }));
       throw new RuntimeException(e);
     }
   }
   
   public void cleanup() {}
   
   public void declareOutputFields(OutputFieldsDeclarer declarer)
   {
     declarer.declare(new Fields(new String[] { "rawMessage" }));
     declarer.declareStream(getErrorStreamId(), new Fields(new String[] { "error" }));
   }
   
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
   
   public PersistenceService getPeristenceService()
   {
     return this.persistence;
   }
   
   public void setPersistenceService(PersistenceService pers_service)
   {
     this.persistence = pers_service;
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




