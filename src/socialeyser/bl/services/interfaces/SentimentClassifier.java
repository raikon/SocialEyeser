package socialeyser.bl.services.interfaces;

import java.util.Set;

import socialeyser.model.Message;

public abstract interface SentimentClassifier
{
  public abstract Message classifyMessage(Message paramMessage, Set<Message> trainingSet) throws Exception;
}




