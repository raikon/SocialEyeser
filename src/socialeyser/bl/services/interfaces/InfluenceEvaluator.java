package socialeyser.bl.services.interfaces;

import socialeyser.model.Message;
import socialeyser.model.RawMessage;

public abstract interface InfluenceEvaluator
{
  public abstract Message evaluateInfluence(RawMessage paramRawMessage);
}




