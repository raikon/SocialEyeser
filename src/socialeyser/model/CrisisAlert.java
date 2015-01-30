 package socialeyser.model;
 
 import java.util.Date;
 
 public class CrisisAlert
 {
   private String monitoringTitle;
   private String crisisLevel;
   private Date detectionTime;
   
   public String getCrisisLevel()
   {
     return this.crisisLevel;
   }
   
   public void setCrisisLevel(String crisisLevel)
   {
     this.crisisLevel = crisisLevel;
   }
   
   public Date getDetectionTime()
   {
     return this.detectionTime;
   }
   
   public void setDetectionTime(Date detectionTime)
   {
     this.detectionTime = detectionTime;
   }
   
   public String toString()
   {
     return "CrisisAlert [monitoringTitle=" + this.monitoringTitle + ", crisisLevel=" + this.crisisLevel + ", detectionTime=" + this.detectionTime + "]";
   }
   
   public String getMonitoringTitle()
   {
     return this.monitoringTitle;
   }
   
   public void setMonitoringTitle(String monitoringTitle)
   {
     this.monitoringTitle = monitoringTitle;
   }
 }




