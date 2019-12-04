package org.horn.mommons.tokenizer.MM;

import java.util.Scanner;

public class Words {
	private String word;
	public String getWord() {
		return word;
	}
	public Words(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("输入待分割字符串！");
		String s = scanner.nextLine();
		this.word = s;
	}
}
