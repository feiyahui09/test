package com.zmax.app.chat;

public class Message {
	
	private int type;// 指定是哪种类型
	private String value;// 值
	private String name;// 值
	
	public Message() {
	}
	
	public Message(int type, String value, String name) {
		super();
		this.type = type;
		this.value = value;
		this.name = name;
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
