package org.horn.commons.java.io.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cmd {
	public static void main(String[] args) throws IOException {

		String cmd = "cmd.exe ping ";
		String ipprefix = "192.168.10.";
		int begin = 101;
		int end = 120;
		Process p = null;
		for (int i = begin; i < end; i++) {
			p = Runtime.getRuntime().exec(cmd + ipprefix + i);
			String line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = reader.readLine()) != null) {
				System.out.println(cmd + ipprefix + i + "\t" + line);
			}
			reader.close();
			p.destroy();
		}
	}

}
