package socialeyser.bl.services.impl.sa.modsupport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import socialeyser.model.Message;

public class MessageSets {
	
	public static Set<Message> normalizedSet(Set<Message> set, Properties prop) throws Exception {
		
		Normalizer n = readProperties(prop);
		
		for (Message mess : set) {
			String text = mess.getRawMessage().getTweetText();
			String normText = n.cleanText(text);
			mess.getEnrichment().setNormText(normText);
		}
		
		Map<String, Message> dupMap = new HashMap<>();
		
		for (Message mess : set) {
			String normText = mess.getEnrichment().getNormText();
			dupMap.put(normText, mess);
		}
		
		return new HashSet<>(dupMap.values());
	}
	
	public static Message normalizedMess(Message mess, Properties prop) throws Exception {
		
		Normalizer n = readProperties(prop);
		String normText = n.cleanText(mess.getRawMessage().getTweetText());
		mess.getEnrichment().setNormText(normText);
		
		return mess;
	}
	
	private static Normalizer readProperties(Properties prop) throws Exception {
		String stopwords = prop.getProperty("stopWordFile");
		String slang = prop.getProperty("slangFile");
		String slangTranslate = prop.getProperty("slangTranslateFile");
		
		Normalizer n = new Normalizer(stopwords, slang, slangTranslate);
		
		return n;
	}
}
