package socialeyser.bl.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import socialeyser.bl.services.interfaces.InfluenceEvaluator;
import socialeyser.model.Enrichment;
import socialeyser.model.Message;
import socialeyser.model.RawMessage;
import socialeyser.model.User;
import socialeyser.model.UserInfluenceScore;

public class InfluenceEvaluatorImpl
implements InfluenceEvaluator
{
	private static final Logger log = LoggerFactory.getLogger(InfluenceEvaluatorImpl.class);
	private static final String USER = "user";
	private static final String FOLLOWERS = "followers";
	private RestTemplate restTemplate;
	private String ScoreServiceUrl;

	public InfluenceEvaluatorImpl()
	{
		Resource resource = new ClassPathResource("/etc/conf/storm.properties");

		Properties properties = new Properties();
		try
		{
			properties.load(resource.getInputStream());
		}
		catch (IOException e) {}
		this.ScoreServiceUrl = properties.getProperty("influenceScoreUrl");
	}

	public Message evaluateInfluence(RawMessage raw_message)
	{
		Map<String, String> vars = new HashMap();
		vars.put("user", new Long(raw_message.getUser().getId()).toString());
		vars.put("followers", "1");
		Message message = new Message();
		message.setRawMessage(raw_message);
		log.info("URL:" + this.ScoreServiceUrl);
		log.info("vars:" + vars.toString());
		UserInfluenceScore score = (UserInfluenceScore)getRestTemplate().getForObject(this.ScoreServiceUrl, UserInfluenceScore.class, vars);

		log.info("score:" + score.toString());
		message.getEnrichment().setUserInfluence(score.getScore().doubleValue());

		return message;
	}

	public RestTemplate getRestTemplate()
	{
		return this.restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}
}

