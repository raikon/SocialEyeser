 package socialeyser.storm.bolt.topicStreaming;
 
 import java.util.Comparator;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import java.util.TreeMap;
 import org.apache.log4j.Logger;
 
 public final class HashtagCounter
   implements TweetListener
 {
   private static Logger log = Logger.getLogger(HashtagCounter.class);
   
   private class ReversedIntegerComparator
     implements Comparator<Integer>
   {
     private ReversedIntegerComparator(Object o) {}
     
     public int compare(Integer i1, Integer i2)
     {
       return -1 * i1.compareTo(i2);
     }
   }
   
   private Set<String> hashtags_to_ignore = new HashSet();
   private Map<String, Integer> hashtag2count;
   private Map<Integer, Set<String>> count2hashtags;
   
   public HashtagCounter()
   {
     this.hashtag2count = new HashMap();
     this.count2hashtags = new TreeMap(new ReversedIntegerComparator(null));
   }
   
   public HashtagCounter(Set<String> hashtags_to_ignore)
   {
     this.hashtags_to_ignore = hashtags_to_ignore;
     
     this.hashtag2count = new HashMap();
     this.count2hashtags = new TreeMap(new ReversedIntegerComparator(null));
   }
   
   public void onTweet(Tweet tweet)
   {
     log.info("hashtags found:" + tweet.getHashtags());
     for (String hashtag : tweet.getHashtags()) {
       if (!this.hashtags_to_ignore.contains(hashtag))
       {
         Integer c = (Integer)this.hashtag2count.get(hashtag);
         if (c == null)
         {
           c = Integer.valueOf(0);
           Set<String> hashtags = (Set)this.count2hashtags.get(Integer.valueOf(0));
           if (hashtags == null) {
             hashtags = new HashSet();
           }
           hashtags.add(hashtag);
           this.count2hashtags.put(Integer.valueOf(0), hashtags);
         }
         this.hashtag2count.put(hashtag, Integer.valueOf(c.intValue() + 1));
         
 
         Set<String> hashtags = (Set)this.count2hashtags.get(Integer.valueOf(c.intValue() + 1));
         if (hashtags == null) {
           hashtags = new HashSet();
         }
         hashtags.add(hashtag);
         this.count2hashtags.put(Integer.valueOf(c.intValue() + 1), hashtags);
         
 
         hashtags = (Set)this.count2hashtags.get(c);
         hashtags.remove(hashtag);
       }
     }
   }
   
   public Set<String> getHashtags()
   {
     return this.hashtag2count.keySet();
   }
   
   public int getCount(String hashtag)
   {
     Integer count = (Integer)this.hashtag2count.get(hashtag);
     if (count != null) {
       return count.intValue();
     }
     return 0;
   }
   
   public List<String> getBestHashtags(int limit)
   {
     List<String> best_hashtags = new LinkedList();
     for (Integer count : this.count2hashtags.keySet()) {
       for (String hashtag : this.count2hashtags.get(count)) {
         if (best_hashtags.size() < limit) {
           best_hashtags.add(hashtag);
         }
       }
     }
     return best_hashtags;
   }
   
   public Set<String> getFrequentHashtags(int limit)
   {
     Set<String> frequent_hashtags = new HashSet();
     for (String hashtag : getHashtags()) {
       if (getCount(hashtag) >= limit) {
         frequent_hashtags.add(hashtag);
       }
     }
     return frequent_hashtags;
   }
 }




