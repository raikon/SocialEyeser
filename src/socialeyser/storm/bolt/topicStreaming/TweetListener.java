package socialeyser.storm.bolt.topicStreaming;

public abstract interface TweetListener
{
  public abstract void onTweet(Tweet paramTweet);
}




