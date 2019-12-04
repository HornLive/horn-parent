package org.horn.mommons.tokenizer.MM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
*最大正向匹配
*最长词语长度5
*/
public class Analysis {
//	public List Compare(String s) throws IOException {
	public static void main(String[] args) throws IOException {
		List<String> result = new ArrayList<String>();
		Words words = new Words();
		String word = words.getWord();
		for (int j = 0; j < word.length(); j++) {
			for (int i = 5; i > 0; i--) {
				if ((j + i) <=word.length()) {
					String ns = word.substring(j, j + i);
//					if (i == 1) {
////						word = word.replace(ns, "");
//						continue;
//					}
					List<String> list = new ArrayList<String>();
					Lexicons lexicons = new Lexicons();
					list = lexicons.split(i);
					if (list.contains(ns)) {
						result.add(ns);
//						word = word.replace(ns, "");
						j=j+ns.length()-1;
						break;
					}
					
				}
			}
		}
//		return result;
		System.out.println(result.toString());
	}
}
