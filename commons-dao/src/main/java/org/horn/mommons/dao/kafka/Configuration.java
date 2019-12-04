package org.horn.mommons.dao.kafka;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>文件名称：Configuration.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2011-2099</p>
 * <p>公   司： 途牛 </p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2017年2月9日</p>
 *
 * @author luwei
 * @version 1.0
 */
public class Configuration {
    private Properties properties = null;
    private static Configuration instants = new Configuration();

    Configuration() {
        loadConfig();
    }


    public static Configuration getConf() {
        return instants;
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        System.out.println(configuration.getString("mysql.host"));
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public long getLong(String key, long defaultValue) {
        long value;
        try {
            value = Long.parseLong(getString(key, Long.toString(defaultValue)));
        } catch (NumberFormatException e) {
            value = defaultValue;
        }
        return value;
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public int getInt(String key) {
        int value;
        try {
            value = Integer.parseInt(getString(key, Long.toString(0)));
        } catch (NumberFormatException e) {
            value = 0;
        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        int value;
        try {
            value = Integer.parseInt(getString(key, Long.toString(defaultValue)));
        } catch (NumberFormatException e) {
            value = defaultValue;
        }
        return value;
    }

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private void loadConfig() {
        this.properties = new Properties();
        try {
            InputStream is = this.getClass()
                    .getClassLoader().getResourceAsStream("dtone.properties");
            properties.load(is);
        } catch (IOException e) {
        }
    }
}
