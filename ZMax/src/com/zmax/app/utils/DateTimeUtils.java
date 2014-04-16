package com.zmax.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
	
	private static final String FORMAT_TIME = "HH:mm";
	private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
	
	public static String formatDateTime(long time) {
		return new SimpleDateFormat(FORMAT_DATETIME).format(new Date(time));
	}
	
	public static String formatTime(long time) {
		long currentTime = System.currentTimeMillis();
		long extra = currentTime - time;
		
		if (extra < 1000) {
			extra = 1000;
		}
		
		extra /= 1000;
		if (extra < 60) {
			return extra + "秒前";
		}
		
		extra /= 60;
		if (extra < 60) {
			return extra + "分钟前";
		}
		
		Date calCreateTime = new Date(time);
		
		Calendar calCurrent = Calendar.getInstance();
		calCurrent.setTimeInMillis(currentTime);
		calCurrent.set(Calendar.HOUR_OF_DAY, 0);
		calCurrent.set(Calendar.MINUTE, 0);
		calCurrent.set(Calendar.SECOND, 0);
		calCurrent.set(Calendar.MILLISECOND, 0);
		
		extra = time - calCurrent.getTimeInMillis();
		if (extra > 0) {
			return "今天 " + new SimpleDateFormat(FORMAT_TIME).format(calCreateTime);
		}
		
		return new SimpleDateFormat(FORMAT_DATETIME).format(calCreateTime);
	}
	
	// 新浪rss时间格式为EEE, d MMM yyyy HH:mm:ss GMT 将其转化为long
	public static String parseSinaGmtTime(String time) {
		
		Long timemillis = 0l;
		try {
			timemillis = Date.parse(time);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return formatTime(timemillis);
	}
	
	// /*s*/
	// private final static ThreadLocal<SimpleDateFormat> dateFormater = new
	// ThreadLocal<SimpleDateFormat>() {
	// @Override
	// protected SimpleDateFormat initialValue() {
	// return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// }
	// };
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};
	private final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmm");
		}
	};
	private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM月dd日");
		}
	};
	
	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater2.get().parse(sdate);
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	public static Date toDatePrecisely(String sdate) {
		try {
			return dateFormater4.get().parse(sdate);
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	// public static String friendly_time(String sdate) {
	// Date time = toDate(sdate);
	// if (time == null) {
	// return "Unknown";
	// }
	// String ftime = "";
	// Calendar cal = Calendar.getInstance();
	//
	// /*
	// * long lt = (time.getTime() )/86400000; long ct =
	// * cal.getTimeInMillis()/86400000;
	// */
	//
	// int days = 0;
	//
	// days = (int) ((cal.getTimeInMillis() - time.getTime()) / 86400000);
	// if (days == 0) {
	//
	// ftime = "今天";
	// } else if (days == 1) {
	// ftime = "昨天";
	// }
	//
	// else {
	//
	// ftime = dateFormater3.get().format(time);
	// }
	// return ftime;
	// }
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		ftime = dateFormater3.get().format(time);
		return ftime;
	}
	
	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}
	
	public static String getWeekOfDate(String sdate) {
		
		Date dt = toDatePrecisely(sdate);
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) w = 0;
		
		return weekDays[w];
	}
}
