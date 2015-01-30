 package socialeyser.model.persistence.entities;
 
 public class MessageEntity
 {
   private String id;
   private RawMessageEntity rawMessage;
   private EnrichmentEntity enrichment;
   
   public RawMessageEntity getRawMessage()
   {
     return this.rawMessage;
   }
   
   public void setRawMessage(RawMessageEntity rawMessage)
   {
     this.rawMessage = rawMessage;
   }
   
   public EnrichmentEntity getEnrichment()
   {
     return this.enrichment;
   }
   
   public void setEnrichment(EnrichmentEntity enrichment)
   {
     this.enrichment = enrichment;
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
 }




