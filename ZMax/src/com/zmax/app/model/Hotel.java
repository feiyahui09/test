package com.zmax.app.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "hotel")
public class Hotel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据库操作相关
	 * 0，未开业；
	 * 1，已开业；
	 * -----------------------
	 */
	@DatabaseField
	public boolean  isUpcoming;
	/**
	 * -----------------------
	 */
	@DatabaseField
	public int id;
	@DatabaseField
	public String name;
	@DatabaseField
	public String open_date;
	@DatabaseField
	public String poster;
	@DatabaseField
	public String pms_hotel_id;
}
