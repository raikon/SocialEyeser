 package socialeyser.model;
 
 import java.util.List;
 import java.util.Map;
 import java.util.TreeMap;
 import socialeyser.bl.services.impl.InverseIntegerComparator;
 
 public class MonitoringMetrics
 {
   private long messageCount;
   private Map<Integer, List<User>> userCounts;
   private Map<Integer, List<String>> hashtagCounts;
   private Map<Integer, List<Media>> mediaCounts;
   private Map<Integer, List<String>> linkCounts;
   private double sentimentAverage;
   private double influenceAverage;
   private double totalInfluence;
   private double totalSentiment;
   
   public MonitoringMetrics()
   {
     setUserCounts(new TreeMap(new InverseIntegerComparator()));
     setHashtagCounts(new TreeMap(new InverseIntegerComparator()));
     setMediaCounts(new TreeMap(new InverseIntegerComparator()));
     setLinkCounts(new TreeMap(new InverseIntegerComparator()));
   }
   
   public long getMessageCount()
   {
     return this.messageCount;
   }
   
   public void setMessageCount(long messageCount)
   {
     this.messageCount = messageCount;
   }
   
   public Map<Integer, List<User>> getUserCounts()
   {
     return this.userCounts;
   }
   
   public void setUserCounts(Map<Integer, List<User>> userCounts)
   {
     this.userCounts = userCounts;
   }
   
   public Map<Integer, List<String>> getHashtagCounts()
   {
     return this.hashtagCounts;
   }
   
   public void setHashtagCounts(Map<Integer, List<String>> hashtagCounts)
   {
     this.hashtagCounts = hashtagCounts;
   }
   
   public Map<Integer, List<Media>> getMediaCounts()
   {
     return this.mediaCounts;
   }
   
   public void setMediaCounts(Map<Integer, List<Media>> mediaCounts)
   {
     this.mediaCounts = mediaCounts;
   }
   
   public Map<Integer, List<String>> getLinkCounts()
   {
     return this.linkCounts;
   }
   
   public void setLinkCounts(Map<Integer, List<String>> linkCounts)
   {
     this.linkCounts = linkCounts;
   }
   
   public double getSentimentAverage()
   {
     return this.sentimentAverage;
   }
   
   public void setSentimentAverage(double sentimentAverage)
   {
     this.sentimentAverage = sentimentAverage;
   }
   
   public double getInfluenceAverage()
   {
     return this.influenceAverage;
   }
   
   public void setInfluenceAverage(double influenceAverage)
   {
     this.influenceAverage = influenceAverage;
   }
   
   public double getTotalInfluence()
   {
     return this.totalInfluence;
   }
   
   public void setTotalInfluence(double totalInfluence)
   {
     this.totalInfluence = totalInfluence;
   }
   
   public double getTotalSentiment()
   {
     return this.totalSentiment;
   }
   
   public void setTotalSentiment(double totalSentiment)
   {
     this.totalSentiment = totalSentiment;
   }
   
   public void increaseTotalInfluence(double value)
   {
     setTotalInfluence(getTotalInfluence() + value);
   }
   
   public void increaseTotalSentiment(double value)
   {
     setTotalSentiment(getTotalSentiment() + value);
   }
   
   public void increaseMessageCount(long value)
   {
     setMessageCount(value + getMessageCount());
   }
   
   public String toString()
   {
     return "MonitoringMetrics [messageCount=" + this.messageCount + ", userCounts=" + this.userCounts + ", hashtagCounts=" + this.hashtagCounts + ", mediaCounts=" + this.mediaCounts + ", linkCounts=" + this.linkCounts + ", sentimentAverage=" + this.sentimentAverage + ", influenceAverage=" + this.influenceAverage + ", totalInfluence=" + this.totalInfluence + ", totalSentiment=" + this.totalSentiment + "]";
   }
 }




