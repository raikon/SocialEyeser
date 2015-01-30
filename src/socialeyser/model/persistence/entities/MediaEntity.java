 package socialeyser.model.persistence.entities;
 
 import socialeyser.model.Media;
 
 public class MediaEntity
 {
   String id;
   Media media;
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public Media getMedia()
   {
     return this.media;
   }
   
   public void setMedia(Media media)
   {
     this.media = media;
   }
 }




