 package socialeyser.bl.services.impl.sa.modsupport;
 
 import java.util.StringTokenizer;
 
 public class TextNormalizer
 {
   static String tweet = "";
   
   public TextNormalizer(String tweet)
   {
     tweet = tweet;
   }
   
   public String getTweet()
   {
     return tweet;
   }
   
   public static String getTweetWithoutUrlsAnnotations(String Tweet)
   {
     StringTokenizer tokens = new StringTokenizer(Tweet, " ");
     String newTweet = "";
     while (tokens.hasMoreTokens())
     {
       String temp = tokens.nextToken();
       if ((!temp.contains("@")) && (!temp.contains("http")) && (!temp.contains("#"))) {
         newTweet = newTweet + temp + " ";
       }
     }
     tweet = "";
     tweet = newTweet;
     return tweet;
   }
 }




