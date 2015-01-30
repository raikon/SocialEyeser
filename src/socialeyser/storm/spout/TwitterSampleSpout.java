 package socialeyser.storm.spout;
 
 import backtype.storm.Config;
 import backtype.storm.spout.SpoutOutputCollector;
 import backtype.storm.task.TopologyContext;
 import backtype.storm.topology.OutputFieldsDeclarer;
 import backtype.storm.topology.base.BaseRichSpout;
 import backtype.storm.tuple.Fields;
 import backtype.storm.tuple.Values;
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.Properties;
 import java.util.concurrent.LinkedBlockingQueue;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.core.io.Resource;
 import socialeyser.bl.services.interfaces.PersistenceService;
 import socialeyser.model.GeoLocationInfo;
 import socialeyser.model.Media;
 import socialeyser.model.RawMessage;
 import socialeyser.model.UserMention;
 import twitter4j.FilterQuery;
 import twitter4j.GeoLocation;
 import twitter4j.HashtagEntity;
 import twitter4j.MediaEntity;
 import twitter4j.Status;
 import twitter4j.StatusListener;
 import twitter4j.Twitter;
 import twitter4j.TwitterException;
 import twitter4j.TwitterFactory;
 import twitter4j.TwitterStream;
 import twitter4j.TwitterStreamFactory;
 import twitter4j.URLEntity;
 import twitter4j.UserMentionEntity;
 import twitter4j.conf.Configuration;
 import twitter4j.conf.ConfigurationBuilder;
 
 public class TwitterSampleSpout
   extends BaseRichSpout
 {
   private static final long serialVersionUID = 1161723181959063511L;
   private PersistenceService persistenceService;
   private SpoutOutputCollector _collector;
   LinkedBlockingQueue<Status> queue = null;
   private TwitterStream _twitterStream;
   private Twitter _twitterServices;
   private String[] keys;
   private long[] accounts;
   private static Logger log = LoggerFactory.getLogger(TwitterSampleSpout.class);
   
   public TwitterSampleSpout(String[] keywords, long[] accounts)
   {
     this.keys = keywords;
     this.accounts = accounts;
   }
   
   public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
   {
     this.queue = new LinkedBlockingQueue(1000);
     this._collector = collector;
     Properties properties = new Properties();
     InputStream inputProperty = null;
     Resource resource = new ClassPathResource("/etc/conf/twitter_credentials.properties");
     try
     {
       inputProperty = resource.getInputStream();
       properties.load(inputProperty);
       log.info("consumer_key:" + properties.getProperty("consumerkey"));
       log.info("consumer_secret:" + properties.getProperty("consumersecret"));
       log.info("access_token:" + properties.getProperty("accesstoken"));
       log.info("access_secret:" + properties.getProperty("accesstokensecret"));
     }
     catch (IOException e)
     {
       throw new RuntimeException(e);
     }
     StatusListener listener = new TwitterFilterListener(this.queue);
     Configuration config = configureTwitterAPI(properties);
     TwitterStreamFactory fact = new TwitterStreamFactory(config);
     TwitterFactory servicesFactory = new TwitterFactory(config);
     set_twitterServices(servicesFactory.getInstance());
     this._twitterStream = fact.getInstance();
     this._twitterStream.addListener(listener);
     this._twitterStream.filter(new FilterQuery(0, this.accounts, this.keys));
     for (int i = 0; i < this.keys.length; i++) {
       log.info("listening to twitter for keyword:" + this.keys[i]);
     }
     try
     {
       inputProperty.close();
     }
     catch (IOException e)
     {
       throw new RuntimeException(e);
     }
   }
   
   private Configuration configureTwitterAPI(Properties properties)
   {
     ConfigurationBuilder cb = new ConfigurationBuilder();
     
     cb.setDebugEnabled(true).setOAuthConsumerKey(properties.getProperty("consumerkey")).setOAuthConsumerSecret(properties.getProperty("consumersecret")).setOAuthAccessToken(properties.getProperty("accesstoken")).setOAuthAccessTokenSecret(properties.getProperty("accesstokensecret"));
     
 
 
 
 
 
 
     Configuration config = cb.build();
     return config;
   }
   
   public void nextTuple()
   {
     Status ret = null;
     ret = (Status)this.queue.poll();
     if (ret != null)
     {
       RawMessage rawMessage = populateRawmessage(ret);
       log.info("rawMessage Text:" + rawMessage.getTweetText());
       this._collector.emit(new Values(new Object[] { rawMessage }), String.valueOf(ret.getId()));
     }
     else
     {
       try
       {
         Thread.sleep(50L);
       }
       catch (InterruptedException e)
       {
         throw new RuntimeException(e);
       }
     }
   }
   
   private RawMessage populateRawmessage(Status ret)
   {
     RawMessage rawMessage = new RawMessage();
     socialeyser.model.User sourceUser = new socialeyser.model.User();
     List<UserMention> userMentions = new ArrayList();
     List<String> hashtags = new ArrayList();
     List<Media> medias = new ArrayList();
     rawMessage.setCreatedAt(ret.getCreatedAt());
     List<String> links = new ArrayList();
     sourceUser.setId(ret.getUser().getId());
     sourceUser.setName(ret.getUser().getName());
     sourceUser.setScreenName(ret.getUser().getScreenName());
     rawMessage.setUser(sourceUser);
     rawMessage.setFavouriteCount(ret.getFavoriteCount());
     rawMessage.setRetweetCount(ret.getRetweetCount());
     rawMessage.setTweetId(ret.getId());
     rawMessage.setTweetText(ret.getText());
     if (ret.getGeoLocation() != null) {
       rawMessage.setGeoLocationInfo(new GeoLocationInfo(ret.getGeoLocation().getLatitude(), ret.getGeoLocation().getLongitude()));
     }
     for (UserMentionEntity u : ret.getUserMentionEntities())
     {
       UserMention user = new UserMention();
       user.setId(u.getId());
       user.setName(u.getName());
       user.setScreenName(u.getScreenName());
       userMentions.add(user);
     }
     rawMessage.setUserMentions(userMentions);
     for (HashtagEntity h : ret.getHashtagEntities()) {
       hashtags.add(h.getText());
     }
     rawMessage.setHashtags(hashtags);
     for (MediaEntity m : ret.getMediaEntities())
     {
       Media media = new Media();
       media.setId(m.getId());
       media.setType(m.getType());
       media.setUrl(m.getExpandedURL());
       medias.add(media);
     }
     rawMessage.setMedias(medias);
     for (URLEntity u : ret.getURLEntities()) {
       links.add(u.getExpandedURL());
     }
     rawMessage.setLinks(links);
     log.info("emitting message text:" + ret.getText() + "  id:" + String.valueOf(ret.getId()));
     
     return rawMessage;
   }
   
   public void close()
   {
     this._twitterStream.shutdown();
   }
   
   public Map<String, Object> getComponentConfiguration()
   {
     Config ret = new Config();
     ret.setMaxTaskParallelism(1);
     return ret;
   }
   
   public void fail(Object id)
   {
     String msgId = (String)id;
     try
     {
       Status tweet = get_twitterServices().showStatus(new Long(msgId).longValue());
       log.info("Message could not be written to database:" + msgId);
     }
     catch (TwitterException e) {}
   }
   
   public void declareOutputFields(OutputFieldsDeclarer declarer)
   {
     declarer.declare(new Fields(new String[] { "rawMessage" }));
   }
   
   public Twitter get_twitterServices()
   {
     return this._twitterServices;
   }
   
   public void set_twitterServices(Twitter _twitterServices)
   {
     this._twitterServices = _twitterServices;
   }
 }




