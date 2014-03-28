package com.zmax.app.utils;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: iron
 * Date: 8/7/13
 * Time: 11:23 PM
 */
public class MDate {
	private Calendar date;
	private int delt;
	private int days;
	
	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public int getDelt() {
		return delt;
	}
	
	public void setDelt(int delt) {
		this.delt = delt;
	}
	
	public int getDays() {
		return days;
	}
	
	public void setDays(int days) {
		this.days = days;
	}
}
