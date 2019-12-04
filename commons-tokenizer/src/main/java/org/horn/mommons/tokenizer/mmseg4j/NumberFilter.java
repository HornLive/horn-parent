package org.horn.mommons.tokenizer.mmseg4j;
/**
 * @Description 判断一个字符串是否包含数字
 * @author lihongen
 *2014年11月5日
 */
public class NumberFilter {
	public static boolean isNumber(String word) {
		for (int i = word.length(); --i >= 0;) {
			if (!Character.isDigit(word.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String resultWord(String word) {
		if (isNumber(word)) {
			return "";
		}
		return word;
	}
}
