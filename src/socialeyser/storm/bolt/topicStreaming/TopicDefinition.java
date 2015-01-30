 package socialeyser.storm.bolt.topicStreaming;
 
 import java.util.HashSet;
 import java.util.Set;
 
 public class TopicDefinition
 {
   private Set<String> keywords;
   
   public TopicDefinition(String... keywords)
   {
     this.keywords = new HashSet();
     for (String keyword : keywords) {
       this.keywords.add(keyword);
     }
   }
   
   public TopicDefinition(Set<String> keywords)
   {
     this.keywords = new HashSet(keywords);
   }
   
   public Set<String> getKeywords()
   {
     return new HashSet(this.keywords);
   }
 }




