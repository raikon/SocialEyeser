package socialeyser.storm.bolt.topicStreaming;

public abstract interface TopicTweetStream
  extends TweetStream
{
  public abstract void setTopicDefinition(TopicDefinition paramTopicDefinition);
  
  public abstract void setTopicDefinitionChangeListener(TopicDefinitionChangeListener paramTopicDefinitionChangeListener);
}




