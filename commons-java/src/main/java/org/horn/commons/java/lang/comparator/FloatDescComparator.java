 package org.horn.commons.java.lang.comparator;

import java.util.Comparator;

 public class FloatDescComparator implements Comparator<Float>{

     public int compare(Float o1, Float o2) {
         return o2.compareTo(o1);
     }
 }
