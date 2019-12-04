package org.horn.commons.java.lang.comparator;

import java.util.*;

public class SortMap {

	public static void main(String[] args) {
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("a", 3.2);
		map.put("b", 1.3);
		map.put("c", 9.2);

		List arrlst = SortMap.sortByValueDesc(map);
		System.out.println(arrlst);
	}
	
	@SuppressWarnings("unchecked")
	public static List sortByValueDesc(Map map){
		List arrayList = new ArrayList(map.entrySet());

		Collections.sort(arrayList, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Map.Entry obj1 = (Map.Entry) o1;
				Map.Entry obj2 = (Map.Entry) o2;
				return ((Double) obj2.getValue()).compareTo((Double) obj1.getValue());
			}
		});
		return arrayList;
	}
	
}
