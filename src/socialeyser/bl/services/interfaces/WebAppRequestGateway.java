package socialeyser.bl.services.interfaces;

import socialeyser.model.CrisisAlert;
import socialeyser.model.exception.WebException;

public abstract interface WebAppRequestGateway
{
  public abstract void sendCrisisAlert(CrisisAlert paramCrisisAlert)
    throws WebException;
}




