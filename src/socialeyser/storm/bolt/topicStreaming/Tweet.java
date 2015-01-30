 package socialeyser.storm.bolt.topicStreaming;
 
 import java.util.HashSet;
 import java.util.Set;
 
 public final class Tweet
 {
   private long id;
   private String text;
   private Set<String> hashtags;
   
   public Tweet(long id, String text, Set<String> hashtags)
   {
     this.id = id;
     this.text = text;
     this.hashtags = new HashSet(hashtags);
   }
   
   public long getId()
   {
     return this.id;
   }
   
   public String getText()
   {
     return this.text;
   }
   
   public Set<String> getHashtags()
   {
     return new HashSet(this.hashtags);
   }
 }




