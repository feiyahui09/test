package com.zmax.app.chat;

public class Message {
	
	private int type;// 指定是哪种类型
	private String value;// 值
	
	public Message() {
	}
	
	public Message(int type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
