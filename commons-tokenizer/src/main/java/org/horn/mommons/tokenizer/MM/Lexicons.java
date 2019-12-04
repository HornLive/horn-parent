package org.horn.mommons.tokenizer.MM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexicons {//词典
	private String lexicons;
	private String[] lex;
	public String getLexicons() {
		return lexicons;
	}
	public Lexicons() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("mmlexicon.txt"));
		String s = br.readLine();
		br.close();
		this.lexicons=s;
		this.lex  = s.split(" ");
	}
	public List split(int n){
		List list = new ArrayList<Object>();
		for (int i = 0;i<lex.length;i++){
			if(lex[i].length()==n){
				list.add(lex[i]);
			}
		}
		return list;
	}
}
