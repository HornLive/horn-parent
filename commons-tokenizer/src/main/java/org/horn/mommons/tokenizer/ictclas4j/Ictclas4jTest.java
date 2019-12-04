package org.horn.mommons.tokenizer.ictclas4j;

import org.ictclas4j.bean.SegResult;
import org.ictclas4j.segment.Segment;

public class Ictclas4jTest {

	public static void main(String[] args) {
		Segment s = new Segment(1);
		SegResult ss = s.split("我们都是中国人");
		
		System.out.println(ss.toString());;
	}

}
