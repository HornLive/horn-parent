package org.horn.commons.java.lang.comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 自定义降序比较器,并实现list排序
 * @author lihongen
 *
 */
@SuppressWarnings("rawtypes")
public class MyComparator implements Comparator //实现Comparator，定义自己的比较方法
{
	public int compare(Object o1, Object o2) {
		Student s1=(Student)o1;
		Student s2=(Student)o2;
	
		if(s1.age > s2.age){	//这样比较是降序,如果把-1改成1就是升序.
			return -1;
		}else if(s1.age<s2.age)	{
			return 1;
		}
		else{
			return 0;
		}
	}
	
	//排序
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ArrayList<Student> list = null;
		Collections.sort(list, new MyComparator());
	}
}


class Student {
	public int id;
	public int age;
}