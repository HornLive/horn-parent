package org.horn.commons.java.lang.comparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 实现自然顺序接口的类，并自定义自然顺序
 * 
 * @author lihongen
 *
 */
public class MyComparable implements Comparable {
	String id;
	public float weight;

	public MyComparable(String p, float w) {
		// TODO Auto-generated constructor stub
		this.id = p;
		this.weight = w;
	}

	public int compareTo(Object obj) {

		MyComparable another = (MyComparable) obj;
//		int num = new Float(this.weight).compareTo(new Float(poi.weight));
		int num = new Float(another.weight).compareTo(new Float(this.weight));
		return num == 0 ? this.id.compareTo(another.id) : num;

		/*
		 * if(this.age>stu.age) return 1; if(this.age==stu.age) return
		 * this.name.compareTo(stu.name); return -1;
		 */
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ArrayList<MyComparable> list = new ArrayList<MyComparable>();

		list.add(new MyComparable("a", 0.5f));
		list.add(new MyComparable("b", 0.7f));
		list.add(new MyComparable("c", 0.2f));
		list.add(new MyComparable("d", 0.1f));
		list.add(new MyComparable("f", 0.1f));
		list.add(new MyComparable("e", 0.1f));

		Collections.sort(list);

		for (MyComparable myComparable : list) {
			System.out.println(myComparable.id + "=" + myComparable.weight);
		}
	}
}
