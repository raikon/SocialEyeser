 package socialeyser.storm.bolt.topicStreaming;
 
 public final class BasicTopicTweetStreamParameters
 {
   private long p1;
   private long p2;
   private int p2r;
   private double tt;
   
   public BasicTopicTweetStreamParameters(long p1, long p2, int p2r, double tt)
   {
     this.p1 = p1;
     this.p2 = p2;
     this.p2r = p2r;
     this.tt = tt;
   }
   
   public long getP1()
   {
     return this.p1;
   }
   
   public long getP2()
   {
     return this.p2;
   }
   
   public int getP2r()
   {
     return this.p2r;
   }
   
   public double getTt()
   {
     return this.tt;
   }
 }




