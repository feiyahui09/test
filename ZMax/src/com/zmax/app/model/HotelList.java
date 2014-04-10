package com.zmax.app.model;

import java.io.Serializable;
import java.util.List;

public class HotelList implements Serializable {
	
	private static final long serialVersionUID = 8674022169656308880L;
	/**
	 * status各返回值意义：200 => 成功；320 => 用户协议更新时使用表已最新；422 => 数据输入错误；404 => 请求失败；500
	 * => 服务器出错
	 */
	
	public int status;
	public List<Hotel> hotels;
}
