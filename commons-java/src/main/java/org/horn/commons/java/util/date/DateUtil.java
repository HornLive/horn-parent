package org.horn.commons.java.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright (C), 2016, 途牛科技有限公司
 * 
 * @author Fantasy
 * @date 创建时间：2016年2月26日 下午7:02:00
 * @version 1.0
 * @parameter
 * @since
 * @return
 */

public class DateUtil {
	static SimpleDateFormat DT_FORMAT = new SimpleDateFormat("yyyyMMdd");// 设置日期格式.
	static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式.
	static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");// 设置日期格式.
	
	/**
	 * 功能描述: <br>
	 * 计算月
	 *
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * 功能描述: <br>
	 * 计算月
	 *
	 * @param ts
	 * @return
	 */
	public static int getMonth(long ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(ts));
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述: <br>
	 * 计算周
	 *
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 功能描述: <br>
	 * 计算周
	 *
	 * @param ts
	 * @return
	 */
	public static int getWeek(long ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(ts));
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR);
	}

	public static int getHour(String datetime) throws ParseException {
		SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(DATETIME_FORMAT.parse(datetime));
		return cal.get(Calendar.HOUR);
	}
	
	public static int getMinPartition(int interval) {
		Calendar cal = Calendar.getInstance();
		return (cal.get(Calendar.MINUTE)/5 +1)*interval;
	}

	public static long getTimeStamp(String date8) throws ParseException{
		return DT_FORMAT.parse(date8).getTime();
	}
	
	/**
	 * 
	 * @return "20180908"
	 */
	public static String getDt() {
		return DateUtil.DT_FORMAT.format(new Date());
	}
	
	public static String getDt(int deff) {
		return DT_FORMAT.format(new Date(System.currentTimeMillis() + 86400000*deff));
	}
	
	public static Date getDate(String datetime) throws ParseException {
		return DateUtil.DATETIME_FORMAT.parse(datetime);  
	}
	
	public static String getTime() throws ParseException {
		return DateUtil.TIME_FORMAT.format(new Date());
	}
	

	public static void main(String[] args) throws ParseException {
		System.out.println(getTimeStamp("20170101"));
		System.out.println(getDate("2012-1-13 17:26:33"));
		getDate("2012-1-13 17:26:33").getHours();
		System.out.println(getDt(-5));
		System.out.println(getTime());
		System.out.println("hourmin:  "+getHour()+getMinPartition(5));
		
		Calendar now = Calendar.getInstance();  
        System.out.println("年: " + now.get(Calendar.YEAR));  
        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");  
        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));  
        System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));  
        System.out.println("分: " + now.get(Calendar.MINUTE));  
        System.out.println("秒: " + now.get(Calendar.SECOND));  
        System.out.println("当前时间毫秒数：" + now.getTimeInMillis());  
        System.out.println(now.getTime());  
        
	}  
}
