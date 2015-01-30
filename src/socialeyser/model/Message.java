 package socialeyser.model;
 
 public class Message
 {
   private RawMessage rawMessage;
   private Enrichment enrichment;
   
   public Message()
   {
     setEnrichment(new Enrichment());
     setRawMessage(new RawMessage());
   }
   
   public RawMessage getRawMessage()
   {
     return this.rawMessage;
   }
   
   public void setRawMessage(RawMessage rawMessage)
   {
     this.rawMessage = rawMessage;
   }
   
   public Enrichment getEnrichment()
   {
     return this.enrichment;
   }
   
   public void setEnrichment(Enrichment enrichment)
   {
     this.enrichment = enrichment;
   }
 }




