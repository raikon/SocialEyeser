 package socialeyser.storm.bolt.topicStreaming;
 
 import java.util.HashSet;
 import java.util.Set;
 
 public final class Query
 {
   private Set<String> keywords;
   
   public Query(Set<String> keywords)
   {
     this.keywords = new HashSet(keywords);
   }
   
   public Set<String> getKeywords()
   {
     return new HashSet(this.keywords);
   }
 }




