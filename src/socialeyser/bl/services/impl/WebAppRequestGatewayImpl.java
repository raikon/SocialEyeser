 package socialeyser.bl.services.impl;
 
 import java.io.IOException;
 import java.util.Properties;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.core.io.Resource;
 import org.springframework.web.client.RestClientException;
 import org.springframework.web.client.RestTemplate;
 import socialeyser.bl.services.interfaces.WebAppRequestGateway;
 import socialeyser.model.CrisisAlert;
 import socialeyser.model.exception.WebException;
 
 public class WebAppRequestGatewayImpl
   implements WebAppRequestGateway
 {
   private RestTemplate restTemplate;
   private static final Logger log = LoggerFactory.getLogger(WebAppRequestGatewayImpl.class);
   private String crisisUrl;
   
   public String getCrisisUrl()
   {
     return this.crisisUrl;
   }
   
   public void setCrisisUrl(String crisisUrl)
   {
     this.crisisUrl = crisisUrl;
   }
   
   public WebAppRequestGatewayImpl()
   {
     Resource resource = new ClassPathResource("/etc/conf/storm.properties");
     Properties properties = new Properties();
     try
     {
       properties.load(resource.getInputStream());
     }
     catch (IOException e) {}
     setCrisisUrl(properties.getProperty("webAppCrisisurl"));
   }
   
   public RestTemplate getRestTemplate()
   {
     return this.restTemplate;
   }
   
   public void setRestTemplate(RestTemplate restTemplate)
   {
     this.restTemplate = restTemplate;
   }
   
   public void sendCrisisAlert(CrisisAlert alert)
     throws WebException
   {
     log.debug("sending crisis alert:" + alert.toString());
     try
     {
       getRestTemplate().put(getCrisisUrl(), alert, new Object[0]);
     }
     catch (RestClientException e)
     {
       throw new WebException("web service not available");
     }
   }
 }




