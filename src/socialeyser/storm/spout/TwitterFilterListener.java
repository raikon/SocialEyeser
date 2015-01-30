 package socialeyser.storm.spout;
 
 import java.util.concurrent.LinkedBlockingQueue;
 import twitter4j.StallWarning;
 import twitter4j.Status;
 import twitter4j.StatusDeletionNotice;
 import twitter4j.StatusListener;
 
 public class TwitterFilterListener
   implements StatusListener
 {
   private LinkedBlockingQueue<Status> queue;
   
   public TwitterFilterListener(LinkedBlockingQueue<Status> queue)
   {
     this.queue = queue;
   }
   
   public void onStatus(Status status)
   {
     this.queue.offer(status);
   }
   
   public void onDeletionNotice(StatusDeletionNotice sdn) {}
   
   public void onTrackLimitationNotice(int i) {}
   
   public void onScrubGeo(long l, long l1) {}
   
   public void onException(Exception e) {}
   
   public void onStallWarning(StallWarning arg0) {}
 }




