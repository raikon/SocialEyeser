 package socialeyser.bl.services.impl.persistence;
 
 import java.util.Date;
 import java.util.List;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.data.mongodb.core.MongoOperations;
 import org.springframework.data.mongodb.core.query.Criteria;
 import org.springframework.data.mongodb.core.query.Query;
 import org.springframework.data.mongodb.core.query.Update;
 import socialeyser.bl.services.interfaces.PersistenceService;
 import socialeyser.model.Media;
 import socialeyser.model.Message;
 import socialeyser.model.MonitoringMetrics;
 import socialeyser.model.RawMessage;
 import socialeyser.model.User;
 import socialeyser.model.exception.PersistenceException;
 import socialeyser.model.persistence.entities.EnrichmentEntity;
 import socialeyser.model.persistence.entities.MediaEntity;
 import socialeyser.model.persistence.entities.MessageEntity;
 import socialeyser.model.persistence.entities.MonitoringMetricsEntity;
 import socialeyser.model.persistence.entities.RawMessageEntity;
 import socialeyser.model.persistence.entities.UserEntity;
 
 public class PersistenceServiceImpl
   implements PersistenceService
 {
   private static final Logger logger = LoggerFactory.getLogger(PersistenceServiceImpl.class);
   private MongoOperations mongoOperations;
   
   public MongoOperations getMongoOperations()
   {
     return this.mongoOperations;
   }
   
   public void setMongoOperations(MongoOperations mongoOperations)
   {
     this.mongoOperations = mongoOperations;
   }
   
   public void writeRawMessage(RawMessage message)
   {
     MongoOperations ops = getMongoOperations();
     RawMessageEntity entity = new RawMessageEntity();
     entity.setCreationDate(message.getCreatedAt().getTime());
     entity.setRawMessage(message);
     ops.insert(entity);
   }
   
   public void writeMessage(Message message)
   {
     MongoOperations ops = getMongoOperations();
     MessageEntity retrievedMessage = (MessageEntity)ops.findOne(Query.query(Criteria.where("rawMessage.tweetId").in(new Object[] { Long.valueOf(message.getRawMessage().getTweetId()) })), MessageEntity.class);
     if (retrievedMessage == null)
     {
       RawMessageEntity rawMessage = (RawMessageEntity)ops.findOne(Query.query(Criteria.where("rawMessage.tweetId").in(new Object[] { Long.valueOf(message.getRawMessage().getTweetId()) })), RawMessageEntity.class);
       
 
 
       MessageEntity entity = new MessageEntity();
       entity.setRawMessage(rawMessage);
       EnrichmentEntity enrichmentEntity = new EnrichmentEntity();
       enrichmentEntity.setEnrichment(message.getEnrichment());
       enrichmentEntity.setId(rawMessage.getId());
       entity.setEnrichment(enrichmentEntity);
       entity.setRawMessage(rawMessage);
       
       ops.insert(entity);
     }
     else
     {
       ops.updateFirst(Query.query(Criteria.where("_id").in(new Object[] { retrievedMessage.getId() })), new Update().set("enrichment.enrichment", message.getEnrichment()), MessageEntity.class);
     }
   }
   
   public RawMessage getRawMessage(Long id)
   {
     return null;
   }
   
   public Message getMessage(Long id)
   {
     return null;
   }
   
   public void deleteRawMessage(Long id) {}
   
   public void deleteMessage(Long id) {}
   
   public void writeMonitoringMetrics(MonitoringMetrics metrics)
     throws PersistenceException
   {
     MonitoringMetricsEntity entity = new MonitoringMetricsEntity();
     
     entity.setCreationDate(new Date().getTime());
     entity.setMessageCount(metrics.getMessageCount());
     for (List<User> users : metrics.getUserCounts().values()) {
       for (User u : users) {
         writeUser(u);
       }
     }
     logger.info("users saved");
     for (List<Media> medias : metrics.getMediaCounts().values()) {
       for (Media m : medias) {
         writeMedia(m);
       }
     }
     logger.info("media saved");
     entity.setMetrics(metrics);
     getMongoOperations().insert(entity);
     logger.info("metrics saved");
   }
   
   public void writeUser(User user)
     throws PersistenceException
   {
     UserEntity entity = (UserEntity)getMongoOperations().findOne(Query.query(Criteria.where("user.name").in(new Object[] { user.getName() })), UserEntity.class);
     if (entity != null)
     {
       entity = new UserEntity();
       entity.setUser(user);
       getMongoOperations().insert(entity);
     }
   }
   
   public User getUser(String name)
     throws PersistenceException
   {
     User user = null;
     
     UserEntity entity = (UserEntity)getMongoOperations().findOne(Query.query(Criteria.where("user.name").in(new Object[] { name })), UserEntity.class);
     if (entity != null) {
       user = entity.getUser();
     }
     return user;
   }
   
   public void writeMedia(Media media)
     throws PersistenceException
   {
     MediaEntity entity = (MediaEntity)getMongoOperations().findOne(Query.query(Criteria.where("media._id").in(new Object[] { Long.valueOf(media.getId()) })), MediaEntity.class);
     if (entity == null)
     {
       entity = new MediaEntity();
       entity.setMedia(media);
       getMongoOperations().insert(entity);
     }
   }
   
   public Media getMedia(String url)
     throws PersistenceException
   {
     Media media = null;
     
     MediaEntity entity = (MediaEntity)getMongoOperations().findOne(Query.query(Criteria.where("media.url").in(new Object[0])), MediaEntity.class);
     if (entity != null) {
       media = entity.getMedia();
     }
     return media;
   }
 }




