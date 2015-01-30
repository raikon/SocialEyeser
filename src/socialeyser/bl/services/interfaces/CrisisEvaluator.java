package socialeyser.bl.services.interfaces;

import socialeyser.model.Message;

public abstract interface CrisisEvaluator
{
  public abstract void computeMetrics(Message paramMessage);
  
  public abstract String getCrisisLevel();
}




