package org.horn.commons.java.io;

import java.io.*;
import java.util.ArrayList;

public class FileIO {
	public static void main(String[] args) throws IOException {
		// fileView("D:\\data\\kafka");
		// generateFile("1.txt", 1000000);

		inputstream3("data/test");

	}

	public static void inputstream(String path) throws IOException {
		File f = new File(path);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		int len = in.read(b);
		in.close();
		System.out.println(new String(b, 0, len));
	}

	/**
	 * 根据文件的大小来定义字节数组的大小
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void inputstream2(String path) throws IOException {
		File f = new File(path);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[(int) f.length()];
		in.read(b);
		in.close();
		System.out.println(new String(b));
	}

	/**
	 * 一个字节一个字节读入
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void inputstream3(String path) throws IOException {
		File f = new File(path);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[(int) f.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) in.read();
		}
		in.close();
		System.out.println(new String(b));
	}

	public static void fileView(String path) {
		System.out.println("Machine readable data files :");
		ArrayList<File> filelist = new ArrayList<File>();

		File trainpath = new File(path);
		if (trainpath.isDirectory()) {
			File[] files = trainpath.listFiles();
			for (int i = 0; i < files.length; i++) {
				filelist.add(files[i]);
				System.out.println("haha\t" + files[i].getPath());
			}
		} else {
			filelist.add(trainpath);
			System.out.println("\t" + trainpath.getName());
		}
	}

	// 转编码
	public static void dataView(String inpath) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(inpath));
			BufferedWriter bw = new BufferedWriter(new FileWriter("f:/1.txt"));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("f:/2.txt"));
			File file = new File(inpath);
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream bufin = new BufferedInputStream(in);
			// FileOutputStream out = new FileOutputStream("f:/2.txt");
			String line;
			byte[] b = new byte[(int) file.length()];
			int length = 0;
			StringBuilder sb = new StringBuilder();
			while ((length = bufin.read(b)) != -1) {
				sb.append(new String(b, "gbk"));
				bw.write(new String(sb.toString().getBytes("utf8"), "utf8"));
			}
			System.out.println(sb.toString());

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				bw2.write(new String(line.getBytes("utf8"), "utf8"));
			}

			bw.flush();
			bw.close();
			bw2.flush();
			bw2.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateFile(String outfile, int rowNum) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			for (int i = 0; i < rowNum; i++) {
				bw.write(i + "," + "This_is_test_to_hbase_ZjYzMTMxODEtY2Q4Zi01MTYyLTc2M2UtN2NhNTRhYjBjMmJlZjYzMTMxODEtY2Q4Zi01MTYyLTc2M2UtN2NhNTRhYjBjMmJl");
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
