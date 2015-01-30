 package socialeyser.model.exception;
 
 public class DataSourceException
   extends Exception
 {
   private static final long serialVersionUID = -2696653891948877609L;
   
   public DataSourceException(String message)
   {
     super(message);
   }
   
   public DataSourceException(Throwable throwable)
   {
     super(throwable);
   }
   
   public DataSourceException(String s, Throwable throwable)
   {
     super(s, throwable);
   }
 }




