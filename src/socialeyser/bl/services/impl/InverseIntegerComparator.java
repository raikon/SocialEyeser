 package socialeyser.bl.services.impl;
 
 import java.util.Comparator;
 
 public class InverseIntegerComparator
   implements Comparator<Integer>
 {
   public int compare(Integer arg0, Integer arg1)
   {
     return -arg0.compareTo(arg1);
   }
 }




