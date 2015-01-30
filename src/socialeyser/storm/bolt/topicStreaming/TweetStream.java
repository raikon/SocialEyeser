package socialeyser.storm.bolt.topicStreaming;

public abstract interface TweetStream
{
  public abstract void setTweetListener(TweetListener paramTweetListener);
  
  public abstract void open();
  
  public abstract void close();
  
  public abstract void cleanup();
  
  public abstract void restart();
}




