 package socialeyser.storm.bolt;
 
 import backtype.storm.task.OutputCollector;
 import backtype.storm.task.TopologyContext;
 import backtype.storm.topology.OutputFieldsDeclarer;
 import backtype.storm.topology.base.BaseRichBolt;
 import backtype.storm.tuple.Tuple;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class LoggingBolt
   extends BaseRichBolt
 {
   private OutputCollector collector;
   private static final long serialVersionUID = -1999477584487196660L;
   static final Logger logger = LoggerFactory.getLogger(LoggingBolt.class);
   
   public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
   {
     setCollector(collector);
   }
   
   public void execute(Tuple input)
   {
     String error = input.getStringByField("error");
     logger.info(error);
     getCollector().ack(input);
   }
   
   public void declareOutputFields(OutputFieldsDeclarer declarer) {}
   
   public OutputCollector getCollector()
   {
     return this.collector;
   }
   
   public void setCollector(OutputCollector collector)
   {
     this.collector = collector;
   }
 }




