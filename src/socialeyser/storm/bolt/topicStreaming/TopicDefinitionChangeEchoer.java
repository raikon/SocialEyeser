 package socialeyser.storm.bolt.topicStreaming;
 
 import java.io.PrintStream;
 import java.util.Date;
 
 public final class TopicDefinitionChangeEchoer
   implements TopicDefinitionChangeListener
 {
   public void onTopicDefinitionChange(TopicDefinition old_topic_definition, TopicDefinition new_topic_definition)
   {
     System.out.println("** " + new Date(System.currentTimeMillis()) + ": TOPIC DEFINITION CHANGE");
     System.out.println("* Old topic definition: ");
     for (String keyword : old_topic_definition.getKeywords()) {
       System.out.print(keyword + ", ");
     }
     System.out.println();
     System.out.println("* New topic definition: ");
     for (String keyword : new_topic_definition.getKeywords()) {
       System.out.print(keyword + ", ");
     }
     System.out.println();
   }
 }




