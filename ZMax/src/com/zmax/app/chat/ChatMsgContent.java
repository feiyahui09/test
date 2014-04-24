package com.zmax.app.chat;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "chatmsgcontent")
public class ChatMsgContent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField
	public String content;
	@DatabaseField
	public String createdAt; // 2014-04-21 11:31:41
	
}
