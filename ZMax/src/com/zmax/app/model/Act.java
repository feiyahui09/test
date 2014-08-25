package com.zmax.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "act")
public class Act implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField
	public String duration; // extra added
	@DatabaseField
	public int id;
	@DatabaseField
	public String name;
	@DatabaseField
	public String start_date;
	@DatabaseField
	public String end_date;
	@DatabaseField
	public String poster;
	@DatabaseField
	public String cities;
	@DatabaseField
	public int is_avaliable;

}
