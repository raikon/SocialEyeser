 package socialeyser.model;
 
 import java.util.Date;
 import java.util.List;
 
 public class RawMessage
 {
   private long tweetId;
   private String tweetText;
   private Date createdAt;
   private User user;
   private GeoLocationInfo geoLocationInfo = new GeoLocationInfo();
   private int retweetCount;
   private int favouriteCount;
   private List<UserMention> userMentions;
   private List<String> hashtags;
   private List<Media> medias;
   private List<String> links;
   private Date creationDate;
   
   public long getTweetId()
   {
     return this.tweetId;
   }
   
   public void setTweetId(long tweetId)
   {
     this.tweetId = tweetId;
   }
   
   public String getTweetText()
   {
     return this.tweetText;
   }
   
   public void setTweetText(String tweetText)
   {
     this.tweetText = tweetText;
   }
   
   public Date getCreatedAt()
   {
     return this.createdAt;
   }
   
   public void setCreatedAt(Date createdAt)
   {
     this.createdAt = createdAt;
   }
   
   public User getUser()
   {
     return this.user;
   }
   
   public void setUser(User user)
   {
     this.user = user;
   }
   
   public GeoLocationInfo getGeoLocationInfo()
   {
     return this.geoLocationInfo;
   }
   
   public void setGeoLocationInfo(GeoLocationInfo geoLocationInfo)
   {
     this.geoLocationInfo = geoLocationInfo;
   }
   
   public int getRetweetCount()
   {
     return this.retweetCount;
   }
   
   public void setRetweetCount(int retweetCount)
   {
     this.retweetCount = retweetCount;
   }
   
   public int getFavouriteCount()
   {
     return this.favouriteCount;
   }
   
   public void setFavouriteCount(int favouriteCount)
   {
     this.favouriteCount = favouriteCount;
   }
   
   public List<UserMention> getUserMentions()
   {
     return this.userMentions;
   }
   
   public void setUserMentions(List<UserMention> userMentions)
   {
     this.userMentions = userMentions;
   }
   
   public List<String> getHashtags()
   {
     return this.hashtags;
   }
   
   public void setHashtags(List<String> hashtags)
   {
     this.hashtags = hashtags;
   }
   
   public List<Media> getMedias()
   {
     return this.medias;
   }
   
   public void setMedias(List<Media> medias)
   {
     this.medias = medias;
   }
   
   public List<String> getLinks()
   {
     return this.links;
   }
   
   public void setLinks(List<String> links)
   {
     this.links = links;
   }
   
   public Date getCreationDate()
   {
     return this.creationDate;
   }
   
   public void setCreationDate(Date creationDate)
   {
     this.creationDate = creationDate;
   }
 }




