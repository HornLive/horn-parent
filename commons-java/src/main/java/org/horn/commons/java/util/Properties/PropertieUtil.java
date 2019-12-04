package org.horn.commons.java.util.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertieUtil {
	private static Properties p = new Properties();

	public PropertieUtil(String filename) {
		System.out.println("加载配置文件：" + filename);
		try {
			// 配置文件在class下，即Src下
			InputStream in = PropertieUtil.class.getClassLoader().getResourceAsStream(filename);
			// InputStream in = new
			// FileInputStream(System.getProperty("user.dir") +"/online.properties");
			p.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return p.getProperty(key).trim();
	}

	// 测试Config
	public static void main(String[] args) throws IOException {
		PropertieUtil conf = new PropertieUtil("conf.properties");
		System.out.println(PropertieUtil.get("hql"));
	}
}
