 package socialeyser.model.persistence.entities;
 
 import socialeyser.model.RawMessage;
 
 public class RawMessageEntity
 {
   private String id;
   private long creationDate;
   private RawMessage rawMessage;
   
   public RawMessage getRawMessage()
   {
     return this.rawMessage;
   }
   
   public void setRawMessage(RawMessage rawMessage)
   {
     this.rawMessage = rawMessage;
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public long getCreationDate()
   {
     return this.creationDate;
   }
   
   public void setCreationDate(long creationDate)
   {
     this.creationDate = creationDate;
   }
 }




