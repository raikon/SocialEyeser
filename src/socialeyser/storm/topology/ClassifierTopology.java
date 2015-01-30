 package socialeyser.storm.topology;
 
 import backtype.storm.Config;

 import backtype.storm.LocalCluster;
 import backtype.storm.StormSubmitter;
 import backtype.storm.topology.BoltDeclarer;
 import backtype.storm.topology.TopologyBuilder;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Properties;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.core.io.FileSystemResource;
 import org.springframework.core.io.Resource;
 import socialeyser.model.Enrichment;
 import socialeyser.model.Message;
 import socialeyser.model.RawMessage;
 import socialeyser.storm.bolt.CrisisEvaluatorBolt;
 import socialeyser.storm.bolt.InfluenceEvaluatorBolt;
 import socialeyser.storm.bolt.LoggingBolt;
 import socialeyser.storm.bolt.PersistenceBolt;
 import socialeyser.storm.bolt.SentimentClassifierBolt;
 import socialeyser.storm.spout.TwitterSampleSpout;
 
 public class ClassifierTopology
 {
   static final String ERRORSTREAMID = "error";
   static final String CONTEXTFILENAME = "/etc/conf/spring-context.xml";
   static final String SENTIMENTCONTEXTFILENAME = "/etc/conf/spring-sentiment-classifier-context.xml";
   
   public static void main(String[] args)
     throws Exception
   {
     Properties properties = loadStormProperties();
     ArrayList<Long> follow = new ArrayList();
     ArrayList<String> track = new ArrayList();
     TopologyBuilder builder = new TopologyBuilder();
     
     track = parseArgs(properties, follow, track, args[0]);
     String clusterMode = args[1];
     
     long[] followArray = toArray(follow);
     String[] trackArray = (String[])track.toArray(new String[track.size()]);
     Config conf = configureTopology(builder, followArray, trackArray, args[0]);
     if ("local".equals(clusterMode))
     {
       LocalCluster cluster = new LocalCluster();
       cluster.submitTopology(args[0], conf, builder.createTopology());
     }
     else
     {
       StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
     }
   }
   
   private static Properties loadStormProperties()
   {
     Resource resource = new ClassPathResource("/etc/conf/storm.properties");
     Properties properties = new Properties();
     try
     {
       properties.load(resource.getInputStream());
     }
     catch (IOException e) {}
     return properties;
   }
   
   private static long[] toArray(ArrayList<Long> follow)
   {
     long[] followArray = new long[follow.size()];
     for (int i = 0; i < follow.size(); i++) {
       followArray[i] = ((Long)follow.get(i)).longValue();
     }
     return followArray;
   }
   
   private static Config configureTopology(TopologyBuilder builder, long[] followArray, String[] trackArray, String topologyTitle)
   {
     builder.setSpout("twitter_spout", new TwitterSampleSpout(trackArray, followArray));
     
     builder.setBolt("persistence_bolt", new PersistenceBolt("error", "/etc/conf/spring-context.xml")).shuffleGrouping("twitter_spout");
     
 
     builder.setBolt("influence_evaluator_bolt", new InfluenceEvaluatorBolt("error", "/etc/conf/spring-context.xml")).shuffleGrouping("persistence_bolt");
     
 
     builder.setBolt("semantic_classifier_bolt", new SentimentClassifierBolt("error", "/etc/conf/spring-sentiment-classifier-context.xml")).shuffleGrouping("influence_evaluator_bolt");
     
 
     builder.setBolt("crisis_evaluator_bolt", new CrisisEvaluatorBolt("/etc/conf/spring-context.xml", "error", topologyTitle), Integer.valueOf(1)).shuffleGrouping("semantic_classifier_bolt");
     
 
     ((BoltDeclarer)((BoltDeclarer)((BoltDeclarer)((BoltDeclarer)builder.setBolt("logging_bolt", new LoggingBolt()).shuffleGrouping("persistence_bolt", "error")).shuffleGrouping("influence_evaluator_bolt", "error")).shuffleGrouping("semantic_classifier_bolt", "error")).shuffleGrouping("crisis_evaluator_bolt", "error")).shuffleGrouping("semantic_classifier_bolt", "error");
     
 
 
 
 
     Config conf = new Config();
     conf.registerSerialization(RawMessage.class);
     conf.registerSerialization(Enrichment.class);
     conf.registerSerialization(Message.class);
     conf.setDebug(true);
     conf.setMaxTaskParallelism(3);
     return conf;
   }
   
   private static ArrayList<String> parseArgs(Properties properties, ArrayList<Long> follow, ArrayList<String> track, String topologyName)
     throws IOException
   {
     FileSystemResource topologyConfigurationFile = new FileSystemResource(properties.getProperty("topologyConfigurationPath") + "/" + topologyName + ".properties");
     
 
     Properties topologyProperties = new Properties();
     topologyProperties.load(topologyConfigurationFile.getInputStream());
     String arg = topologyProperties.getProperty("keys");
     track.addAll(Arrays.asList(arg.split(",")));
     return track;
   }
   
   private static boolean isNumericalArgument(String argument)
   {
     String[] args = argument.split(",");
     boolean isNumericalArgument = true;
     for (String arg : args) {
       try
       {
         Integer.parseInt(arg);
       }
       catch (NumberFormatException nfe)
       {
         isNumericalArgument = false;
         break;
       }
     }
     return isNumericalArgument;
   }
 }




