 package socialeyser.model.exception;
 
 public class PersistenceException
   extends Exception
 {
   private static final long serialVersionUID = 3752174919883825054L;
   
   public PersistenceException(String message)
   {
     super(message);
   }
   
   public PersistenceException(Throwable throwable)
   {
     super(throwable);
   }
   
   public PersistenceException(String s, Throwable throwable)
   {
     super(s, throwable);
   }
 }




