 package socialeyser.model.persistence.entities;
 
 import socialeyser.model.User;
 
 public class UserEntity
 {
   String id;
   User user;
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public User getUser()
   {
     return this.user;
   }
   
   public void setUser(User user)
   {
     this.user = user;
   }
 }




