package com.zmax.app.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "citylocation")
public class CityLocation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@DatabaseField
	public String province;
	
	@DatabaseField
	public String city;
	
	@Override
	public String toString() {
		return "CityLocation [province=" + province + ", city=" + city + ", city_code=" + city_code + "]";
	}
	
	@DatabaseField
	public String city_code;
}
