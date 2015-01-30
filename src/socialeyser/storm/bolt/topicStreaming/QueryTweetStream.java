package socialeyser.storm.bolt.topicStreaming;

public abstract interface QueryTweetStream
  extends TweetStream
{
  public abstract void setQuery(Query paramQuery);
}




