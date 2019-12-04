package org.horn.commons.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx {
	private final static Pattern SYMBOL =  Pattern.compile("[\\pP\\p{Punct}]");
	private static final String SYMBOL_STR = "[ <>\\pP‘’“”]";//标点符号

	public static void main(String[] args) {
		String str = "service@xsoftlab.net";
		System.out.println(matches(str));

		
		String str2 = "baike.xsoftlab.net";
		System.out.println(find(str2));
		
		Matcher matcher = SYMBOL.matcher("asdfasd。dfasdfas");
		System.out.println(matcher.find());
		
		String str3 = "!!！？？!!!!%*）%￥！KT  ss&*(,.~1如果@&(^-自己!!知道`什`么#是$苦%……Z，&那*()么一-=定——+告诉::;\\\"'/?.,><[]{}\\\\||别人什么是甜。 V去符号  标号！！当然,，。!!..**半角";
		System.out.println(str3);
		System.out.println("[A]"+str3.replaceAll("[\\pP\\p{Punct}]", "").replaceAll("\\s*",""));//清除所有符号,只留下字母 数字  汉字  共3类.
		System.out.println(str3.replaceAll("[//pP]", "#"));
		System.out.println(str3.replaceAll("[ \\pP‘’“”]", "#"));
		String[] sub_token_field = "[阿道夫]>迭>代".split(SYMBOL_STR);
		System.out.println(sub_token_field.toString());

		System.out.println(str3.replaceAll("\\p{Punct}",""));//不能完全清除标点
		System.out.println(str3.replaceAll("\\pP",""));//完全清除标点
		System.out.println(str3.replaceAll("\\p{P}",""));//同上,一样的功能

	}

	/**
	 * 匹配验证-验证Email是否正确
	 * 
	 * @param str
	 *            要验证的字符串
	 * @return
	 */
	private static boolean matches(String str) {
		// 邮箱验证规则
		String regEx = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		return matcher.matches();
	}
	
	/**
	 * 在字符串中查询字符或者字符串
	 * @param args
	 * @return
	 */
	public static boolean find(String str) {
		// 正则表达式规则
		String regEx = "baike.*";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		// 查找字符串中是否有匹配正则表达式的字符/字符串
		return matcher.find();
		
	}

}
