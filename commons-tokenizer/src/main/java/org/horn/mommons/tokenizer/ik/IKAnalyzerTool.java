package org.horn.mommons.tokenizer.ik;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.ArrayList;


/**
 * 分词 Copyright (C), 2017 ,途牛科技有限公司
 *
 * @author lihongen
 * @data 创建时间：2017年1月21日 上午10:48:22
 * @version V2.0
 * @since
 */
public class IKAnalyzerTool {

	/**
	 * IKAnalyzer实现（过滤不是词的单个字）
	 * 
	 * @param str
	 * @return
	 */
	public static ArrayList<String> analysisWord(String str) {
		try {
			StringReader sr = new StringReader(str);
			IKSegmenter ik = new IKSegmenter(sr, true);
			ArrayList<String> result = new ArrayList<String>();

			Lexeme lex = null;
			while ((lex = ik.next()) != null) {
				String word = lex.getLexemeText();
				if (word.length() > 1) {
					result.add(word);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * IKAnalyzer实现（过滤掉词库中已有的词）
	 * 
	 * @param str
	 * @return
	 */
	public static StringBuilder analysisNoWord(String str) {
		StringBuilder sb = new StringBuilder();
		try {
			StringReader sr = new StringReader(str);
			IKSegmenter ik = new IKSegmenter(sr, true);

			Lexeme lex = null;
			while ((lex = ik.next()) != null) {
				String word = lex.getLexemeText();
				if (word.length() < 2) {
					sb.append(word);
				} else {
					sb.append(" ");
				}
			}
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * IKAnalyzer实现
	 * 
	 * @param str
	 * @return
	 */
	public static ArrayList<String> analysisAll(String str) {
		try {
			StringReader sr = new StringReader(str);
			IKSegmenter ik = new IKSegmenter(sr, true);
			ArrayList<String> result = new ArrayList<String>();

			Lexeme lex = null;
			while ((lex = ik.next()) != null) {
				String word = lex.getLexemeText();
				result.add(word);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}