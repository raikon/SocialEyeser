 package socialeyser.model;
 
 import java.text.DecimalFormat;
 import java.util.Date;
 
 public class Enrichment
 {
   private double userInfluence;
   private double semanticClassification;
   private double classificationConfidence;
   private String sentiment;
   private String normText;
   private Date creationDate;
   private Date lastUpdateDate;
   
   public double getUserInfluence()
   {
     return this.userInfluence;
   }
   
   public void setUserInfluence(double userInfluence)
   {
     this.userInfluence = userInfluence;
   }
   
   public double getSemanticClassification()
   {
     return this.semanticClassification;
   }
   
   public void setSemanticClassification(double semanticClassification)
   {
     this.semanticClassification = semanticClassification;
   }
   
   public double getClassificationConfidence()
   {
     return this.classificationConfidence;
   }
   
   public void setClassificationConfidence(double classificationConfidence)
   {
     this.classificationConfidence = classificationConfidence;
   }
   
   public Date getCreationDate()
   {
     return this.creationDate;
   }
   
   public void setCreationDate(Date creationDate)
   {
     this.creationDate = creationDate;
   }
   
   public Date getLastUpdateDate()
   {
     return this.lastUpdateDate;
   }
   
   public void setLastUpdateDate(Date lastUpdateDate)
   {
     this.lastUpdateDate = lastUpdateDate;
   }
   
   public String getFormattedSentiment()
   {
     String result = null;
     DecimalFormat df = new DecimalFormat();
     df.applyPattern("###,##0.0000");
     result = df.format(getSemanticClassification());
     return result;
   }

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public String getNormText() {
		return normText;
	}

	public void setNormText(String normText) {
		this.normText = normText;
	}
 }