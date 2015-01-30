 package socialeyser.model.exception;
 
 public class WebException
   extends Exception
 {
   private static final long serialVersionUID = 665482665840731612L;
   
   public WebException(String message)
   {
     super(message);
   }
   
   public WebException(Throwable throwable)
   {
     super(throwable);
   }
   
   public WebException(String s, Throwable throwable)
   {
     super(s, throwable);
   }
 }




