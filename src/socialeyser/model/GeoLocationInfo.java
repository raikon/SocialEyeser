 package socialeyser.model;
 
 public class GeoLocationInfo
 {
   private double latitude;
   private double longitude;
   
   public GeoLocationInfo() {}
   
   public double getLatitude()
   {
     return this.latitude;
   }
   
   public GeoLocationInfo(double latitude, double longitude)
   {
     this.latitude = latitude;
     this.longitude = longitude;
   }
   
   public void setLatitude(double latitude)
   {
     this.latitude = latitude;
   }
   
   public double getLongitude()
   {
     return this.longitude;
   }
   
   public void setLongitude(double longitude)
   {
     this.longitude = longitude;
   }
 }




