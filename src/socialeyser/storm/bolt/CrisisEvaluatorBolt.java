 package socialeyser.storm.bolt;
 
 import backtype.storm.task.OutputCollector;
 import backtype.storm.task.TopologyContext;
 import backtype.storm.topology.OutputFieldsDeclarer;
 import backtype.storm.topology.base.BaseRichBolt;
 import backtype.storm.tuple.Fields;
 import backtype.storm.tuple.Tuple;
 import backtype.storm.tuple.Values;
 import java.util.Date;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.context.support.ClassPathXmlApplicationContext;
 import socialeyser.bl.services.impl.CrisisEvaluatorImpl;
 import socialeyser.bl.services.interfaces.PersistenceService;
 import socialeyser.bl.services.interfaces.WebAppRequestGateway;
 import socialeyser.model.CrisisAlert;
 import socialeyser.model.CrisisLevels;
 import socialeyser.model.Message;
 import socialeyser.model.exception.PersistenceException;
 import socialeyser.model.exception.WebException;
 
 public class CrisisEvaluatorBolt
   extends BaseRichBolt
 {
   private static final Logger logger = LoggerFactory.getLogger(CrisisEvaluatorBolt.class);
   private static final long serialVersionUID = -1842368381010652437L;
   private static final int METRICS_MSG_WRITE_THRESHOLD = 40;
   private static final long METRICS_MILLISECOND_TIME_THRESHOLD = 60000L;
   private CrisisEvaluatorImpl crisisEvaluator;
   private ClassPathXmlApplicationContext appContext;
   private PersistenceService persistenceService;
   private WebAppRequestGateway webGateway;
   private String errorStreamId;
   private String springContextFileName;
   private OutputCollector collector;
   private String monitoringTitle;
   private Date LastMetricsUpdateTime;
   private Integer currentMsgCount;
   
   public Date getLastMetricsUpdateTime()
   {
     return this.LastMetricsUpdateTime;
   }
   
   public void setLastMetricsUpdateTime(Date lastMetricsUpdateTime)
   {
     this.LastMetricsUpdateTime = lastMetricsUpdateTime;
   }
   
   public CrisisEvaluatorBolt(String springContextFileName, String errorStreamId, String topologyTitle)
   {
     setSpringContextFileName(springContextFileName);
     setErrorStreamId(errorStreamId);
     setMonitoringTitle(topologyTitle);
     
     logger.info("title:" + topologyTitle);
   }
   
   public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector)
   {
     this.collector = collector;
     logger.info("started creating context");
     setAppContext(new ClassPathXmlApplicationContext(getSpringContextFileName()));
     logger.info("context created");
     setCrisisEvaluator((CrisisEvaluatorImpl)getAppContext().getBean("CrisisEvaluatorService"));
     setPersistenceService((PersistenceService)getAppContext().getBean("PersistenceService"));
     setWebGateway((WebAppRequestGateway)getAppContext().getBean("webAppRequestGateway"));
     setCurrentMsgCount(new Integer(0));
     setLastMetricsUpdateTime(new Date());
   }
   
   public void execute(Tuple input)
   {
     Message message = (Message)input.getValueByField("message");
     
     Date now = new Date();
     getCrisisEvaluator().computeMetrics(message);
     String crisisLevel = getCrisisEvaluator().getCrisisLevel();
     if (!crisisLevel.equals(CrisisLevels.NONE.getName())) {
       try
       {
         sendCrisisAlert(crisisLevel);
       }
       catch (WebException e1)
       {
         getCollector().emit(getErrorStreamId(), input, new Values(new Object[] { e1.getMessage() }));
       }
     }
     if ((getCurrentMsgCount().intValue() + 1 >= 40) || (now.getTime() - getLastMetricsUpdateTime().getTime() > 60000L)) {
       try
       {
         getPersistenceService().writeMonitoringMetrics(getCrisisEvaluator().getMetrics());
         setCurrentMsgCount(new Integer(getCurrentMsgCount().intValue() + 1));
         setLastMetricsUpdateTime(now);
       }
       catch (PersistenceException e)
       {
         getCollector().emit(getErrorStreamId(), input, new Values(new Object[] { e.getMessage() }));
       }
     }
     this.collector.emit(input, new Values(new Object[] { message }));
     this.collector.ack(input);
   }
   
   private void sendCrisisAlert(String crisisLevel)
     throws WebException
   {
     CrisisAlert alert = new CrisisAlert();
     alert.setCrisisLevel(crisisLevel);
     alert.setDetectionTime(new Date());
     alert.setMonitoringTitle(getMonitoringTitle());
     logger.info("sending crisis signal");
     logger.info(alert.toString());
     getWebGateway().sendCrisisAlert(alert);
   }
   
   public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
   {
     outputFieldsDeclarer.declareStream(getErrorStreamId(), new Fields(new String[] { "error" }));
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
   
   public OutputCollector getCollector()
   {
     return this.collector;
   }
   
   public void setCollector(OutputCollector collector)
   {
     this.collector = collector;
   }
   
   public PersistenceService getPersistenceService()
   {
     return this.persistenceService;
   }
   
   public void setPersistenceService(PersistenceService persistenceService)
   {
     this.persistenceService = persistenceService;
   }
   
   public Integer getCurrentMsgCount()
   {
     return this.currentMsgCount;
   }
   
   public void setCurrentMsgCount(Integer currentMsgCount)
   {
     this.currentMsgCount = currentMsgCount;
   }
   
   public CrisisEvaluatorImpl getCrisisEvaluator()
   {
     return this.crisisEvaluator;
   }
   
   public void setCrisisEvaluator(CrisisEvaluatorImpl crisisEvaluator)
   {
     this.crisisEvaluator = crisisEvaluator;
   }
   
   public WebAppRequestGateway getWebGateway()
   {
     return this.webGateway;
   }
   
   public void setWebGateway(WebAppRequestGateway webGateway)
   {
     this.webGateway = webGateway;
   }
   
   public String getMonitoringTitle()
   {
     return this.monitoringTitle;
   }
   
   public void setMonitoringTitle(String monitoringTitle)
   {
     this.monitoringTitle = monitoringTitle;
   }
 }




