 package socialeyser.storm.bolt.topicStreaming;
 
 import java.util.LinkedList;
 import java.util.List;
 
 public final class TweetHistory
   implements TweetListener
 {
   private List<Long> history = new LinkedList();
   
   public void onTweet(Tweet tweet)
   {
     this.history.add(Long.valueOf(tweet.getId()));
   }
   
   public int getLength()
   {
     return this.history.size();
   }
   
   public static int getNumberOfTweetsInCommon(TweetHistory th1, TweetHistory th2)
   {
     int result = 0;
     
     int i = 0;
     int j = 0;
     while ((i < th1.history.size()) && (j < th2.history.size())) {
       if (((Long)th1.history.get(i)).longValue() < ((Long)th2.history.get(j)).longValue())
       {
         i++;
       }
       else if (((Long)th1.history.get(i)).longValue() > ((Long)th2.history.get(j)).longValue())
       {
         j++;
       }
       else
       {
         result++;
         i++;
         j++;
       }
     }
     return result;
   }
 }




