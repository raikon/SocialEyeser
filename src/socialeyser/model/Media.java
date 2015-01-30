 package socialeyser.model;
 
 public class Media
 {
   private long id;
   private String url;
   private String type;
   
   public long getId()
   {
     return this.id;
   }
   
   public void setId(long id)
   {
     this.id = id;
   }
   
   public String getUrl()
   {
     return this.url;
   }
   
   public void setUrl(String url)
   {
     this.url = url;
   }
   
   public String getType()
   {
     return this.type;
   }
   
   public void setType(String type)
   {
     this.type = type;
   }
   
   public int hashCode()
   {
     int prime = 31;
     int result = 1;
     result = 31 * result + (int)(this.id ^ this.id >>> 32);
     result = 31 * result + (this.url == null ? 0 : this.url.hashCode());
     return result;
   }
   
   public boolean equals(Object obj)
   {
     if (this == obj) {
       return true;
     }
     if (obj == null) {
       return false;
     }
     if (getClass() != obj.getClass()) {
       return false;
     }
     Media other = (Media)obj;
     if (this.id != other.id) {
       return false;
     }
     if (this.url == null)
     {
       if (other.url != null) {
         return false;
       }
     }
     else if (!this.url.equals(other.url)) {
       return false;
     }
     return true;
   }
 }




