 package socialeyser.model.persistence.entities;
 
 import socialeyser.model.MonitoringMetrics;
 
 public class MonitoringMetricsEntity
 {
   private String id;
   private String monitoringTitle;
   private long messageCount;
   private long creationDate;
   private MonitoringMetrics metrics;
   
   public long getMessageCount()
   {
     return this.messageCount;
   }
   
   public void setMessageCount(long messageCount)
   {
     this.messageCount = messageCount;
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public String getMonitoringTitle()
   {
     return this.monitoringTitle;
   }
   
   public void setMonitoringTitle(String monitoringTitle)
   {
     this.monitoringTitle = monitoringTitle;
   }
   
   public MonitoringMetrics getMetrics()
   {
     return this.metrics;
   }
   
   public void setMetrics(MonitoringMetrics metrics)
   {
     this.metrics = metrics;
   }
   
   public long getCreationDate()
   {
     return this.creationDate;
   }
   
   public void setCreationDate(long creationDate)
   {
     this.creationDate = creationDate;
   }
   
   public String toString()
   {
     return "MonitoringMetricsEntity [id=" + this.id + ", monitoringTitle=" + this.monitoringTitle + ", messageCount=" + this.messageCount + ", creationDate=" + this.creationDate + ", metrics=" + this.metrics + "]";
   }
 }




