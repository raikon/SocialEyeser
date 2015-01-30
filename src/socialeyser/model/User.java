 package socialeyser.model;
 
 public class User
 {
   private long id;
   private String name;
   private String screenName;
   
   public int hashCode()
   {
     int prime = 31;
     int result = 1;
     result = 31 * result + (int)(this.id ^ this.id >>> 32);
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
     User other = (User)obj;
     if (this.id != other.id) {
       return false;
     }
     return true;
   }
   
   public User() {}
   
   public User(long id, String name)
   {
     this.id = id;
     this.name = name;
   }
   
   public long getId()
   {
     return this.id;
   }
   
   public void setId(long id)
   {
     this.id = id;
   }
   
   public String getName()
   {
     return this.name;
   }
   
   public void setName(String name)
   {
     this.name = name;
   }
   
   public String getScreenName()
   {
     return this.screenName;
   }
   
   public void setScreenName(String screenName)
   {
     this.screenName = screenName;
   }
 }




