 package socialeyser.bl.services.impl.sa.modsupport;
 
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Set;
 import java.util.Vector;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.core.io.ClassPathResource;
 import org.springframework.core.io.Resource;
 
 public class Lexicon
 {
   private HashMap<String, Double> _dict;
   private static final Logger logger = LoggerFactory.getLogger(Lexicon.class);
   
   public Lexicon()
   {
     Resource resource = new ClassPathResource("/etc/conf/SentiWordNet_3.0.0_20130122.txt");
     logger.info("resource found:" + resource.getFilename());
     this._dict = new HashMap();
     HashMap<String, Vector<Double>> _temp = new HashMap();
     try
     {
       logger.info("parsing file");
       BufferedReader csv = new BufferedReader(new InputStreamReader(resource.getInputStream()));
       String line = "";
       while ((line = csv.readLine()) != null)
       {
         String[] data = line.split("\t");
         
         Double score = Double.valueOf(Double.parseDouble(data[2]) - Double.parseDouble(data[3]));
         
 
         String[] words = data[4].split(" ");
         for (String w : words)
         {
           String[] w_n = w.split("#"); int 
             tmp195_194 = 0; String[] tmp195_192 = w_n;tmp195_192[tmp195_194] = (tmp195_192[tmp195_194] + "#" + data[0]);
           
           int index = Integer.parseInt(w_n[1]) - 1;
           if (_temp.containsKey(w_n[0]))
           {
             Vector<Double> v = (Vector)_temp.get(w_n[0]);
             if (index > v.size()) {
               for (int i = v.size(); i < index; i++) {
                 v.add(Double.valueOf(0.0D));
               }
             }
             v.add(index, score);
             _temp.put(w_n[0], v);
           }
           else
           {
             Vector<Double> v = new Vector();
             for (int i = 0; i < index; i++) {
               v.add(Double.valueOf(0.0D));
             }
             v.add(index, score);
             _temp.put(w_n[0], v);
           }
         }
       }
       logger.info("file parsed");
       Set<String> temp = _temp.keySet();
       Iterator<String> iterator = temp.iterator();
       while (iterator.hasNext())
       {
         String word = (String)iterator.next();
         Vector<Double> v = (Vector)_temp.get(word);
         double score = 0.0D;
         double sum = 0.0D;
         for (int i = 0; i < v.size(); i++) {
           score += 1.0D / (i + 1) * ((Double)v.get(i)).doubleValue();
         }
         for (int i = 1; i <= v.size(); i++) {
           sum += 1.0D / i;
         }
         score /= sum;
         this._dict.put(word, Double.valueOf(score));
       }
       csv.close();
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
   }
   
   public Double getSentiment(String word)
   {
     Double total = new Double(0.0D);
     logger.info("looking for entries in the dictionary for word:  " + word);
     if (this._dict.get(word + "#n") != null)
     {
       logger.info("found entry in the dictionary with key: " + word + "#n" + " value: " + this._dict.get(new StringBuilder().append(word).append("#n").toString()));
       
       total = Double.valueOf(((Double)this._dict.get(word + "#n")).doubleValue() + total.doubleValue());
     }
     if (this._dict.get(word + "#a") != null)
     {
       logger.info("found entry in the dictionary with key: " + word + "#a" + " value: " + this._dict.get(new StringBuilder().append(word).append("#n").toString()));
       
       total = Double.valueOf(((Double)this._dict.get(word + "#a")).doubleValue() + total.doubleValue());
     }
     if (this._dict.get(word + "#r") != null)
     {
       logger.info("found entry in the dictionary with key: " + word + "#r" + " value: " + this._dict.get(new StringBuilder().append(word).append("#n").toString()));
       
       total = Double.valueOf(((Double)this._dict.get(word + "#r")).doubleValue() + total.doubleValue());
     }
     if (this._dict.get(word + "#v") != null)
     {
       logger.info("found entry in the dictionary with key: " + word + "#v" + " value: " + this._dict.get(new StringBuilder().append(word).append("#n").toString()));
       
       total = Double.valueOf(((Double)this._dict.get(word + "#v")).doubleValue() + total.doubleValue());
     }
     logger.info("returning total:" + total.toString());
     return total;
   }
 }




