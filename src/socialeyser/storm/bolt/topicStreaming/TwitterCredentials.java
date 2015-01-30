 package socialeyser.storm.bolt.topicStreaming;
 
 public final class TwitterCredentials
 {
   private String consumer_key;
   private String consumer_secret;
   private String access_token;
   private String access_token_secret;
   
   public TwitterCredentials(String consumer_key, String consumer_secret, String access_token, String access_token_secret)
   {
     this.consumer_key = consumer_key;
     this.consumer_secret = consumer_secret;
     this.access_token = access_token;
     this.access_token_secret = access_token_secret;
   }
   
   public String getConsumerKey()
   {
     return this.consumer_key;
   }
   
   public String getConsumerSecret()
   {
     return this.consumer_secret;
   }
   
   public String getAccessToken()
   {
     return this.access_token;
   }
   
   public String getAccessTokenSecret()
   {
     return this.access_token_secret;
   }
 }




