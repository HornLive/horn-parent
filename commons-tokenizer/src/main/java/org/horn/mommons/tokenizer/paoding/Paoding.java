package org.horn.mommons.tokenizer.paoding;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.StringReader;

public class Paoding {

	public static void main(String[] args) {
		Paoding pd = new Paoding();
		String out = pd.paoding("你好啊南京");
		System.out.println(out);
	}

	public String paoding(String line) {
		PaodingAnalyzer analyzer = new PaodingAnalyzer();
		StringReader sr = new StringReader(line);
		TokenStream ts = analyzer.tokenStream("", sr);
		StringBuilder sb = new StringBuilder();
		try {
			while (ts.incrementToken()) {
				CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);
				sb.append(ta.toString());
				sb.append(" ");
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}

}
