package com.zmax.app.model;

import java.io.Serializable;

public class Login implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * status各返回值意义：200 => 成功；320 => 用户协议更新时使用表已最新；422 => 数据输入错误；404 => 请求失败；500
	 * => 服务器出错
	 */
	public int status;
	public String message;

	public String id_number;
	public String hotel_location;
	public String hotel_name;
	public String pms_hotel_id;
	public String room_num;
	public String start_time;
	public String end_time;
	public String auth_token;
	
	public int user_id;
	public int gender;
	public String nick_name;
}
