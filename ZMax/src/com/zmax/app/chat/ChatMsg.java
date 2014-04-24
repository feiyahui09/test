package com.zmax.app.chat;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "chatmsg")
public class ChatMsg implements Serializable {
	
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
	public ChatMsgContent msg;
	
	// 额外增加，帮助显示聊天列表
	@DatabaseField
	public int type;// 指定是哪种类型
	
	public String tipTime;
	
	// // 错误返回如下
	// @DatabaseField
	// public String message;
	// @DatabaseField
	// public boolean error;
	
}
