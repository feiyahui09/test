package com.zmax.app.chat;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "chatbody")
public class ChatBody implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField
	public String target;
	@DatabaseField
	public String gender;
	@DatabaseField
	public String from;
	@DatabaseField
	public int code;
	@DatabaseField
	public ChatMsg msg;
	@DatabaseField
	public String content;
	
//	// 错误返回如下
//	@DatabaseField
//	public String message;
//	@DatabaseField
//	public boolean error;
	
}
