 package socialeyser.storm.bolt.topicStreaming;
 
 public class BasicTopicTweetStream
   implements TopicTweetStream
 {
   private Thread loop;
   private TopicDefinition topic_definition;
   private TopicDefinitionChangeListener topic_definition_change_listener;
   private TweetListener tweet_listener;
   private QueryTweetStream main_stream;
   private QueryTweetStream aux_stream;
   private BasicTopicTweetStreamParameters parameters;
   
   public BasicTopicTweetStream(QueryTweetStream main_stream, QueryTweetStream aux_stream, BasicTopicTweetStreamParameters parameters)
   {
     this.parameters = parameters;
     this.main_stream = main_stream;
     this.aux_stream = aux_stream;
   }
   
   public void setTopicDefinition(TopicDefinition topic_definition)
   {
     this.topic_definition = topic_definition;
   }
   
   public void setTopicDefinitionChangeListener(TopicDefinitionChangeListener listener)
   {
     this.topic_definition_change_listener = listener;
   }
   
   public void setTweetListener(TweetListener listener)
   {
     this.tweet_listener = listener;
   }
   
   public void open()
   {
     Thread loop = new Thread(new BasicTopicTweetStreamLoop(this.topic_definition, this.parameters, this.main_stream, this.aux_stream, this.tweet_listener, this.topic_definition_change_listener));
     this.loop = loop;
     loop.start();
   }
   
   public void close()
   {
     this.loop.interrupt();
   }
   
   public void cleanup() {}
   
   public void restart() {}
 }




