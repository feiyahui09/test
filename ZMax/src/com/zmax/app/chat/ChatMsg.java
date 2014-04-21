package com.zmax.app.chat;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "charmsg")
public class ChatMsg implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField
	public String timestamp; // extra added
	@DatabaseField
	public String content;
	@DatabaseField
	public String gender;
	@DatabaseField
	public String name;
}
