 package socialeyser.storm.bolt.topicStreaming;
 
 import org.apache.log4j.Logger;
 
 public class BasicTopicTweetStreamLoop
   implements Runnable
 {
   private static Logger log = Logger.getLogger(BasicTopicTweetStreamLoop.class);
   public TopicDefinition topic_definition;
   public TopicDefinitionChangeListener topic_definition_change_listener;
   public TweetListener tweet_listener;
   public QueryTweetStream main_stream;
   public QueryTweetStream aux_stream;
   public long phase1_timeout;
   public long phase2_timeout;
   public int phase2_max_repetitions;
   public double tweets_in_common_threshold;
   
   public BasicTopicTweetStreamLoop(TopicDefinition topic_definition, BasicTopicTweetStreamParameters parameters, QueryTweetStream main_stream, QueryTweetStream aux_stream, TweetListener tweet_listener, TopicDefinitionChangeListener topic_definition_change_listener)
   {
     this.topic_definition = topic_definition;
     this.phase1_timeout = parameters.getP1();
     this.phase2_timeout = parameters.getP2();
     this.phase2_max_repetitions = parameters.getP2r();
     this.tweets_in_common_threshold = parameters.getTt();
     this.main_stream = main_stream;
     this.aux_stream = aux_stream;
     this.topic_definition_change_listener = topic_definition_change_listener;
   }
   
   public void run()
   {
     for (;;)
     {
       Query query = new Query(this.topic_definition.getKeywords());
       this.main_stream.setQuery(query);
       final HashtagCounter counter = new HashtagCounter(this.topic_definition.getKeywords());
       log.info("starting p1 with keywords:" + this.topic_definition.getKeywords());
       this.main_stream.setTweetListener(new TweetListener()
       {
         public void onTweet(Tweet tweet)
         {
           counter.onTweet(tweet);
         }
       });
       this.main_stream.open();
       try
       {
         Thread.sleep(this.phase1_timeout);
       }
       catch (InterruptedException e)
       {
         this.main_stream.cleanup();
         return;
       }
       this.main_stream.cleanup();
     }
   }
 }




