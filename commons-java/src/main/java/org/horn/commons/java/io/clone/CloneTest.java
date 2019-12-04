package org.horn.commons.java.io.clone;

import java.util.Vector;

/**
 * Copyright (c), 2017 , 南京途牛科技有限公司
 * 
 * @author Horn Live
 * @date 创建时间： 2017年4月14日 下午1:57:04
 * @version 3.0
 */
public class CloneTest {
	public static void main(String[] args) {
		A a1 = new A();
		A a2 = new A();
		a1.name[0] = "a";
		a1.name[1] = "1";
		a2 = (A) a1.clone();
		a2.name[0] = "b";
		a2.name[1] = "1";
		System.out.println("a1.name=" + a1.name);
		System.out.println("a1.name=" + a1.name[0] + a1.name[1]);
		System.out.println("a2.name=" + a2.name);
		System.out.println("a2.name=" + a2.name[0] + a2.name[1]);
	}
}

class A implements Cloneable {
	public String name[];
	public Vector<B> claB;

	public A() {
		name = new String[2];
		claB = new Vector<B>();
	}
	
	/**
	 * 当Class A成员变量类型是java的基本类型时（外加String类型）,只要实现如上简单的clone（称影子clone）就可以。但是如果Class A成员变量是数组或复杂类型时，就必须实现深度clone。
	 * 需要注意的是Class A存在更为复杂的成员变量时，如Vector等存储对象地址的容器时，就必须clone彻底。
	 */

	public Object clone() {
		A o = null;
		try {
			o = (A) super.clone();
			o.name = (String[]) name.clone();// 深度clone
			o.claB = new Vector<B>();// 将clone进行到底
			for (int i = 0; i < claB.size(); i++) {
				B temp = (B) claB.get(i).clone();// 当然Class B也要实现相应clone方法
				o.claB.add(temp);
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}

class B {
	public Object clone() {  
        B o = null;  
        try {  
            o = (B) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  

}