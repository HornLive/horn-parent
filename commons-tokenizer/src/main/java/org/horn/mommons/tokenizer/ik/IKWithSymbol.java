package org.horn.mommons.tokenizer.ik;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;

public class IKWithSymbol {

	public static void main(String[] args) throws Exception {
		String input = "^_^ 你好，ik分词！";
		Lexeme beforeWord = null;
		Lexeme currentWord = null;
		StringBuffer sb = new StringBuffer();
		IKSegmenter ik = new IKSegmenter(new StringReader(input), true);
		System.out.println("原句：" + input);
		while ((currentWord = ik.next()) != null) {
			sb.append(appendSymbol(input, beforeWord, currentWord));
			beforeWord = currentWord;
		}
		sb.append(appendSymbol(input, beforeWord, currentWord));
		System.out.println("分词：" + sb.toString().replaceAll(" +", " ").trim());
	}

	/**
	 * 补全IK分词遗漏的符号
	 * 
	 * @param line
	 * @param before
	 * @param cur
	 * @return
	 */
	public static String appendSymbol(String line, Lexeme before, Lexeme cur) {
		String res = "";
		if (before == null) {// 第一个词前边的符号
			res = cur.getLexemeText() + " ";
			int start = cur.getBegin();
			if (start > 0) {
				String left = appendWhiteSpace(line.substring(0, start));
				res = left + res;
			}
		} else if (cur == null) {// 最后一个词后边的符号
			int end = before.getEndPosition();
			if (end < line.length()) {
				String right = appendWhiteSpace(line.substring(before.getEndPosition()));
				res = right;
			}
		} else { // 和前一个词之间的符号
			res = cur.getLexemeText() + " ";
			int beforeEnd = before.getEndPosition();
			if (cur.getBegin() > beforeEnd) {
				String mid = appendWhiteSpace(line.substring(beforeEnd, cur.getBegin()));
				res = mid + res;
			}
		}
		return res;
	}

	/**
	 * 你好吗 -> 你 好 吗
	 * 
	 * @param src
	 * @return
	 */
	public static String appendWhiteSpace(String src) {
		String dst = "";
		for (char c : src.toCharArray()) {
			dst += c + " ";
		}
		return dst;
	}
}
