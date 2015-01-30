 package socialeyser.storm.bolt.topicStreaming;
 
 import java.util.HashSet;
 import java.util.Set;
 import org.apache.log4j.Logger;
 import twitter4j.FilterQuery;
 import twitter4j.HashtagEntity;
 import twitter4j.StallWarning;
 import twitter4j.Status;
 import twitter4j.StatusDeletionNotice;
 import twitter4j.StatusListener;
 import twitter4j.TwitterStream;
 import twitter4j.TwitterStreamFactory;
 import twitter4j.conf.ConfigurationBuilder;
 
 public final class T4JQueryTweetStream
   implements QueryTweetStream
 {
   private static Logger log = Logger.getLogger(T4JQueryTweetStream.class);
   private TweetListener listener;
   private Query query;
   private TwitterCredentials credentials;
   private TwitterStream stream;
   
   private static class BasicListener
     implements StatusListener
   {
     private TweetListener listener;
     
     BasicListener(TweetListener listener)
     {
       this.listener = listener;
     }
     
     private Tweet status2tweet(Status status)
     {
       long id = status.getId();
       String text = status.getText();
       
       Set<String> hashtags = new HashSet();
       for (HashtagEntity hashtag : status.getHashtagEntities()) {
         hashtags.add(hashtag.getText().toLowerCase());
       }
       return new Tweet(id, text, hashtags);
     }
     
     public void onStatus(Status status)
     {
       Tweet tweet = status2tweet(status);
       this.listener.onTweet(tweet);
     }
     
     public void onException(Exception arg0) {}
     
     public void onScrubGeo(long arg0, long arg1) {}
     
     public void onStallWarning(StallWarning arg0) {}
     
     public void onTrackLimitationNotice(int arg0) {}
     
     public void onDeletionNotice(StatusDeletionNotice arg0) {}
   }
   
   public T4JQueryTweetStream(TwitterCredentials credentials)
   {
     this.credentials = credentials;
   }
   
   public void setTweetListener(TweetListener listener)
   {
     this.listener = listener;
   }
   
   public void setQuery(Query query)
   {
     this.query = query;
   }
   
   public void open()
   {
     log.info("opening stream");
     
     ConfigurationBuilder cb = new ConfigurationBuilder().setDebugEnabled(false).setOAuthConsumerKey(this.credentials.getConsumerKey()).setOAuthConsumerSecret(this.credentials.getConsumerSecret()).setOAuthAccessToken(this.credentials.getAccessToken()).setOAuthAccessTokenSecret(this.credentials.getAccessTokenSecret());
     
 
 
 
 
     TwitterStream stream = new TwitterStreamFactory(cb.build()).getInstance();
     
     this.stream = stream;
     stream.addListener(new BasicListener(this.listener));
     String[] keywords = (String[])this.query.getKeywords().toArray(new String[0]);
     log.info("keywords to track:" + keywords[0]);
     FilterQuery fq = new FilterQuery().track(keywords);
     stream.filter(fq);
   }
   
   public void close()
   {
     this.stream.shutdown();
   }
   
   public void cleanup()
   {
     this.stream.cleanUp();
   }
   
   public void restart()
   {
     String[] keywords = (String[])this.query.getKeywords().toArray(new String[0]);
     FilterQuery fq = new FilterQuery().track(keywords);
     this.stream.filter(fq);
   }
 }




