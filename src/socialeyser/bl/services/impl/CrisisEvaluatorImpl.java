package socialeyser.bl.services.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socialeyser.bl.services.interfaces.CrisisEvaluator;
import socialeyser.model.CrisisLevels;
import socialeyser.model.Enrichment;
import socialeyser.model.Media;
import socialeyser.model.Message;
import socialeyser.model.MonitoringMetrics;
import socialeyser.model.RawMessage;
import socialeyser.model.User;

public class CrisisEvaluatorImpl
implements CrisisEvaluator
{
	private static long CRISISINTERVALTHRESHOLD = 300000L;
	private MonitoringMetrics metrics = new MonitoringMetrics();
	private String currentCrisisLevel;
	private static final Logger log = LoggerFactory.getLogger(CrisisEvaluatorImpl.class);
	private Date lastCrisisReported;

	public String getCurrentCrisisLevel()
	{
		return this.currentCrisisLevel;
	}

	public void setCurrentCrisisLevel(String currentCrisisLevel)
	{
		this.currentCrisisLevel = currentCrisisLevel;
	}

	public Date getLastCrisisReported()
	{
		return this.lastCrisisReported;
	}

	public void setLastCrisisReported(Date lastCrisisReported)
	{
		this.lastCrisisReported = lastCrisisReported;
	}

	public CrisisEvaluatorImpl()
	{
		setCurrentCrisisLevel(CrisisLevels.NONE.getName());
	}

	public MonitoringMetrics getMetrics()
	{
		return this.metrics;
	}

	public void setMetrics(MonitoringMetrics metrics)
	{
		this.metrics = metrics;
	}

	public void computeMetrics(Message message)
	{
		long currentMessageCount = getMetrics().getMessageCount();
		getMetrics().setMessageCount(currentMessageCount++);

		User user = message.getRawMessage().getUser();
		Map<Integer, List<User>> userCounts = getMetrics().getUserCounts();
		Map<Integer, List<String>> hashtagCounts = getMetrics().getHashtagCounts();

		Map<Integer, List<Media>> mediaCounts = getMetrics().getMediaCounts();

		Map<Integer, List<String>> linkCounts = getMetrics().getLinkCounts();


		updateUserCount(message, userCounts);
		updateHashtags(message, hashtagCounts);
		updateMediaCount(message, mediaCounts);
		updateLinks(message, linkCounts);
		updateAverages(message);
	}

	private void updateLinks(Message message, Map<Integer, List<String>> linkCounts)
	{
		boolean linksAdded = false;
		Set<Map.Entry<Integer, List<String>>> entry = linkCounts.entrySet();
		if (entry.size() == 0)
		{
			List<String> linksList = new LinkedList();
			for (String s : message.getRawMessage().getLinks()) {
				linksList.add(s);
			}
			linkCounts.put(new Integer(1), linksList);
		}
		else
		{
			for (String s : message.getRawMessage().getLinks())
			{
				Iterator<Map.Entry<Integer, List<String>>> it = entry.iterator();
				while ((it.hasNext()) && (!linksAdded))
				{
					Map.Entry<Integer, List<String>> i = (Map.Entry)it.next();
					List<String> linksList = (List)i.getValue();
					if (linksList.contains(s))
					{
						linksList.remove(s);
						if (linksList.size() == 0) {
							linkCounts.remove(i.getKey());
						}
						linksList = (List)linkCounts.get(new Integer(((Integer)i.getKey()).intValue() + 1));
						if (linksList == null)
						{
							linksList = new LinkedList();
							linksList.add(s);
							linkCounts.put(new Integer(((Integer)i.getKey()).intValue() + 1), linksList);
						}
						else
						{
							linksList.add(s);
						}
						linksAdded = true;
					}
				}
				if (!linksAdded)
				{
					List<String> linksList = (List)linkCounts.get(new Integer(1));
					if (linksList == null)
					{
						linksList = new LinkedList();
						linksList.add(s);
						linkCounts.put(new Integer(1), linksList);
					}
					else
					{
						linksList.add(s);
					}
				}
			}
		}
	}

	private void updateAverages(Message message)
	{
		getMetrics().increaseTotalInfluence(message.getEnrichment().getUserInfluence());

		getMetrics().increaseTotalSentiment(new Double(message.getEnrichment().getSemanticClassification()).doubleValue());


		getMetrics().increaseMessageCount(1L);
		getMetrics().setInfluenceAverage(getMetrics().getTotalInfluence()); 


				getMetrics().setSentimentAverage(getMetrics().getTotalSentiment()); 
	}

	private void updateMediaCount(Message message, Map<Integer, List<Media>> mediaCounts)
	{
		boolean mediaAdded = false;
		Set<Map.Entry<Integer, List<Media>>> entry = mediaCounts.entrySet();
		if (entry.size() == 0)
		{
			List<Media> mediasList = new LinkedList();
			for (Media m : message.getRawMessage().getMedias()) {
				mediasList.add(m);
			}
			mediaCounts.put(new Integer(1), mediasList);
		}
		else
		{
			for (Media m : message.getRawMessage().getMedias())
			{
				Iterator<Map.Entry<Integer, List<Media>>> it = entry.iterator();
				while ((it.hasNext()) && (!mediaAdded))
				{
					Map.Entry<Integer, List<Media>> i = (Map.Entry)it.next();
					List<Media> mediasList = (List)i.getValue();
					if (mediasList.contains(m))
					{
						mediasList.remove(m);
						if (mediasList.size() == 0) {
							mediaCounts.remove(i.getKey());
						}
						mediasList = (List)mediaCounts.get(new Integer(((Integer)i.getKey()).intValue() + 1));
						if (mediasList == null)
						{
							mediasList = new LinkedList();
							mediasList.add(m);
							mediaCounts.put(new Integer(((Integer)i.getKey()).intValue() + 1), mediasList);
						}
						else
						{
							mediasList.add(m);
						}
						mediaAdded = true;
					}
				}
				if (!mediaAdded)
				{
					List<Media> mediasList = (List)mediaCounts.get(new Integer(1));
					if (mediasList == null)
					{
						mediasList = new LinkedList();
						mediasList.add(m);
						mediaCounts.put(new Integer(1), mediasList);
					}
					else
					{
						mediasList.add(m);
					}
				}
			}
		}
	}

	private void updateHashtags(Message message, Map<Integer, List<String>> hashtagCounts)
	{
		boolean hashtagAdded = false;

		Set<Map.Entry<Integer, List<String>>> entries = hashtagCounts.entrySet();
		if (entries.size() == 0)
		{
			List<String> hashTagsList = new LinkedList();
			for (String s : message.getRawMessage().getHashtags()) {
				hashTagsList.add(s);
			}
			hashtagCounts.put(new Integer(1), hashTagsList);
		}
		else
		{
			for (String s : message.getRawMessage().getHashtags())
			{
				Iterator<Map.Entry<Integer, List<String>>> it = entries.iterator();
				while ((it.hasNext()) && (!hashtagAdded))
				{
					Map.Entry<Integer, List<String>> entry = (Map.Entry)it.next();
					List<String> hashTagsList = (List)entry.getValue();
					if (hashTagsList.contains(s))
					{
						hashTagsList.remove(s);
						if (hashTagsList.size() == 0) {
							hashtagCounts.remove(entry.getKey());
						}
						hashTagsList = (List)hashtagCounts.get(new Integer(((Integer)entry.getKey()).intValue() + 1));
						if (hashTagsList == null)
						{
							hashTagsList = new LinkedList();
							hashTagsList.add(s);
							hashtagCounts.put(new Integer(((Integer)entry.getKey()).intValue() + 1), hashTagsList);
						}
						else
						{
							hashTagsList.add(s);
						}
						hashtagAdded = true;
					}
				}
				if (!hashtagAdded)
				{
					List<String> hashTagsList = (List)hashtagCounts.get(new Integer(1));
					if (hashTagsList == null)
					{
						hashTagsList = new LinkedList();
						hashTagsList.add(s);
						hashtagCounts.put(new Integer(1), hashTagsList);
					}
					else
					{
						hashTagsList.add(s);
					}
				}
			}
		}
	}

	private void updateUserCount(Message message, Map<Integer, List<User>> userCounts)
	{
		boolean userAdded = false;
		User u = message.getRawMessage().getUser();

		Set<Map.Entry<Integer, List<User>>> entrySet = userCounts.entrySet();
		if (entrySet.size() == 0)
		{
			List<User> usersList = new LinkedList();
			usersList.add(u);
			userCounts.put(new Integer(1), usersList);
		}
		else
		{
			Iterator<Map.Entry<Integer, List<User>>> it = entrySet.iterator();
			while ((it.hasNext()) && (!userAdded))
			{
				Map.Entry<Integer, List<User>> e = (Map.Entry)it.next();
				List<User> usersList = (List)e.getValue();
				if (usersList.contains(u))
				{
					usersList.remove(u);
					if (usersList.size() == 0) {
						userCounts.remove(e.getKey());
					}
					usersList = (List)userCounts.get(new Integer(((Integer)e.getKey()).intValue() + 1));
					if (usersList == null)
					{
						usersList = new LinkedList();
						usersList.add(u);
						userCounts.put(new Integer(((Integer)e.getKey()).intValue() + 1), usersList);
					}
				}
				else
				{
					usersList.add(u);
				}
				userAdded = true;
			}
			if (!userAdded)
			{
				List<User> usersList = (List)userCounts.get(new Integer(1));
				if (usersList == null)
				{
					usersList = new LinkedList();
					usersList.add(u);
					userCounts.put(new Integer(1), usersList);
				}
				else
				{
					usersList.add(u);
				}
			}
		}
	}

	public String getCrisisLevel()
	{
		String crisisLevel = CrisisLevels.NONE.getName();
		if (getMetrics().getSentimentAverage() < 0.0D)
		{
			double random = Math.random();
			if (random <= 0.33D)
			{
				if (getCurrentCrisisLevel().equals(CrisisLevels.LIGHT.getName()))
				{
					if (new Date().getTime() - getLastCrisisReported().getTime() >= CRISISINTERVALTHRESHOLD)
					{
						crisisLevel = CrisisLevels.LIGHT.getName();
						setLastCrisisReported(new Date());
						setCurrentCrisisLevel(crisisLevel);
					}
				}
				else
				{
					crisisLevel = CrisisLevels.LIGHT.getName();
					setLastCrisisReported(new Date());
					setCurrentCrisisLevel(crisisLevel);
				}
			}
			else if (random <= 0.66D)
			{
				if (getCurrentCrisisLevel().equals(CrisisLevels.MEDIUM.getName()))
				{
					if (new Date().getTime() - getLastCrisisReported().getTime() >= CRISISINTERVALTHRESHOLD)
					{
						crisisLevel = CrisisLevels.MEDIUM.getName();
						setLastCrisisReported(new Date());
						setCurrentCrisisLevel(crisisLevel);
					}
				}
				else
				{
					crisisLevel = CrisisLevels.MEDIUM.getName();
					setLastCrisisReported(new Date());
					setCurrentCrisisLevel(crisisLevel);
					setCurrentCrisisLevel(crisisLevel);
				}
			}
			else if (getCurrentCrisisLevel().equals(CrisisLevels.DANGEROUS.getName()))
			{
				if (new Date().getTime() - getLastCrisisReported().getTime() >= CRISISINTERVALTHRESHOLD)
				{
					crisisLevel = CrisisLevels.DANGEROUS.getName();
					setLastCrisisReported(new Date());
					setCurrentCrisisLevel(crisisLevel);
				}
			}
			else
			{
				crisisLevel = CrisisLevels.DANGEROUS.getName();
				setLastCrisisReported(new Date());
				setCurrentCrisisLevel(crisisLevel);
			}
		}
		return crisisLevel;
	}
}
