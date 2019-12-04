package org.horn.mommons.tokenizer.mmseg4j;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class StopWordFilter {
	Set<String> stopSet = new HashSet<String>();

	public void init() {
		BufferedReader stopReader = null;
		String stopWord;
		try {
			stopReader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(new File(this.getClass()
									.getResource("/").getPath()
									+ ".."
									+ File.separator
									+ "src"
									+ File.separator
									+ "data"
									+ File.separator
									+ "chinese_stopword.dic"))));
			while ((stopWord = stopReader.readLine()) != null) {
				if (stopWord.startsWith("#")) {
					continue;
				}
				stopSet.add(stopWord);
			}
			stopReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String excludeStopWord(String word) {
		if (stopSet.contains(word)) {
			return "";
		}
		return word;
	}
}
