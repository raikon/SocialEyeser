package socialeyser.bl.services.interfaces;

import socialeyser.model.Message;

public abstract interface SentimentClassifier
{
  public abstract Message classifyMessage(Message paramMessage);
}




