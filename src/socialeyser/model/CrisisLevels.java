 package socialeyser.model;
 
 public enum CrisisLevels
 {
   NONE("NONE"),  DANGEROUS("DANGEROUS"),  MEDIUM("MEDIUM"),  LIGHT("LIGHT");
   
   private String name;
   
   private CrisisLevels(String name)
   {
     setName(name);
   }
   
   public boolean equalsName(String otherName)
   {
     return otherName == null ? false : this.name.equals(otherName);
   }
   
   public String toString()
   {
     return this.name;
   }
   
   public String getName()
   {
     return this.name;
   }
   
   public void setName(String name)
   {
     this.name = name;
   }
 }




