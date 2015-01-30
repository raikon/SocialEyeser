 package socialeyser.model.persistence.entities;
 
 import socialeyser.model.Enrichment;
 
 public class EnrichmentEntity
 {
   private String id;
   private Enrichment enrichment;
   
   public Enrichment getEnrichment()
   {
     return this.enrichment;
   }
   
   public void setEnrichment(Enrichment enrichment)
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




