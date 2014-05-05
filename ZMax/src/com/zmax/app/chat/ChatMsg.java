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
	public int gender;
	@DatabaseField
	public String from;
	@DatabaseField
	public int code;
	@DatabaseField
	public ChatMsgContent msg;
	@DatabaseField
	public String type;//图片or文字
	
	// 额外增加，帮助显示聊天列表
	@DatabaseField
	public int item_type;// 指定是哪种类型，左边文字，右边图片还是？
	
	public String tipTime;
	
	// // 错误返回如下
	// @DatabaseField
	// public String message;
	// @DatabaseField
	// public boolean error;
	
}
