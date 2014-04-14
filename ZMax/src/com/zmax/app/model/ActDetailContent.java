package com.zmax.app.model;

import java.io.Serializable;
import java.util.List;

public class ActDetailContent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String name;
	public String start_date;
	public String end_date;
	public String event_begin;
	public List<ActDetailDescription> description_items;
	public List<ActDetailHotel> hotels;
}
