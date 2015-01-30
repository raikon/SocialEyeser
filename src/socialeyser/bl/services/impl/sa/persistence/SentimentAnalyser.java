package socialeyser.bl.services.impl.sa.persistence;

import java.io.IOException;
import socialeyser.model.Message;

public abstract interface SentimentAnalyser
{
  public abstract double sentiment(Message paramMessage)
    throws IOException;
}




