 package socialeyser.bl.services.impl.sa.modsupport;
 
 import java.io.IOException;
 import java.util.Arrays;
 import java.util.LinkedList;
 import socialeyser.model.Message;
 import socialeyser.model.RawMessage;
 
 public class Tokenizer
 {
   public LinkedList<String> getTokens(Message message)
     throws IOException
   {
     String text = TextNormalizer.getTweetWithoutUrlsAnnotations(message.getRawMessage().getTweetText());
     
     return splitTokens(text);
   }
   
   private LinkedList<String> splitTokens(String text)
   {
     LinkedList<String> tokens = new LinkedList();
     String[] words = text.split(" ");
     tokens.addAll(Arrays.asList(words));
     return tokens;
   }
 }




