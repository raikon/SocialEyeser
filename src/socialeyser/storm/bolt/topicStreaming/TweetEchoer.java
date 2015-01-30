 package socialeyser.storm.bolt.topicStreaming;
 
 import java.io.PrintStream;
 import java.util.Date;
 
 public final class TweetEchoer
   implements TweetListener
 {
   public void onTweet(Tweet tweet)
   {
     System.out.println("* " + new Date(System.currentTimeMillis()) + ": " + tweet.getText());
   }
 }




