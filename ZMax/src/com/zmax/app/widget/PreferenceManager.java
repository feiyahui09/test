package com.zmax.app.widget;

import com.zmax.app.utils.Log;
import com.zmax.app.utils.MDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by fyf on 14-8-18.
 */
public class PreferenceManager {
	private static MDate mainDates;
	private static Map<String, String[]> festivals;
	private static Calendar startCal;
	private static Calendar endCal;

	private static String startDateStr;
	private static String endDateStr;
	private static int selDay = 1;

	public static void setStartDateStr(String startDateStr) {
		PreferenceManager.startDateStr = startDateStr;
	}

	public static void setEndDateStr(String endDateStr) {
		PreferenceManager.endDateStr = endDateStr;
	}

	public static MDate getMainDates() {

		mainDates = new MDate();
		mainDates.setDate(Calendar.getInstance());
		return mainDates;
	}

	public static void setDates(MDate _mainDates, int _delta, int _selDays) {

		try {
			Calendar start = (Calendar) _mainDates.getDate().clone();
			Calendar end =  (Calendar) _mainDates.getDate().clone();
			Log.i("_delta " + _delta + "       _selDays" + _selDays);
			setStartCal(addDays(start, _delta));
			setEndCal(addDays(end, _delta + _selDays));
			setSelDay(_selDays);
			startDateStr = new SimpleDateFormat("yyyy-MM-dd").format(getStartCal().getTime());
			endDateStr = new SimpleDateFormat("yyyy-MM-dd").format(getEndCal().getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public static Map<String, String[]> getFestivals() {
		return festivals;
	}

	private static Calendar addDays(Calendar calendar, int daysToAdd) {
		calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
		return calendar;
	}


	public static Calendar getStartCal() {
		return startCal;
	}

	public static void setStartCal(Calendar startCal) {
		PreferenceManager.startCal = startCal;
	}

	public static Calendar getEndCal() {
		return endCal;
	}

	public static void setEndCal(Calendar endCal) {
		PreferenceManager.endCal = endCal;
	}

	public static String getStartDateStr() {
		return startDateStr;
	}

	public static String getEndDateStr() {
		return endDateStr;
	}

	public static int getSelDay() {
		return selDay;
	}

	public static void setSelDay(int selDay) {
		PreferenceManager.selDay = selDay;
	}
}
