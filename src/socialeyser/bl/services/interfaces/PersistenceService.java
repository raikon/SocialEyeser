package socialeyser.bl.services.interfaces;

import socialeyser.model.Media;
import socialeyser.model.Message;
import socialeyser.model.MonitoringMetrics;
import socialeyser.model.RawMessage;
import socialeyser.model.User;
import socialeyser.model.exception.PersistenceException;

public abstract interface PersistenceService
{
  public abstract void writeRawMessage(RawMessage paramRawMessage)
    throws PersistenceException;
  
  public abstract void writeMessage(Message paramMessage)
    throws PersistenceException;
  
  public abstract RawMessage getRawMessage(Long paramLong)
    throws PersistenceException;
  
  public abstract Message getMessage(Long paramLong)
    throws PersistenceException;
  
  public abstract void deleteRawMessage(Long paramLong)
    throws PersistenceException;
  
  public abstract void deleteMessage(Long paramLong)
    throws PersistenceException;
  
  public abstract void writeMonitoringMetrics(MonitoringMetrics paramMonitoringMetrics)
    throws PersistenceException;
  
  public abstract void writeUser(User paramUser)
    throws PersistenceException;
  
  public abstract User getUser(String paramString)
    throws PersistenceException;
  
  public abstract void writeMedia(Media paramMedia)
    throws PersistenceException;
  
  public abstract Media getMedia(String paramString)
    throws PersistenceException;
}




