package com.zmax.app.model;

import java.io.Serializable;

/**
 * room_temperature(室温)、setting_temperature(设置温度)、
 * schema(空调模式)、on_off(阀门)、air_blower(风机模式)、status(空调开关机)
 * 具体参数返回值含义：
 * room_temperature和setting_temperature都是以摄氏度
 * schema：col：制冷、hot：制热、nat：通风/睡眠；
 * air_blower： low：低速、mid：中速、hig：高速、auto：自动；
 * on_off：0：关，1: 开
 * status：0：关，1: 开
 * 
 * @author fyf
 * 
 */
public class AirCondition  extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	public int room_temperature;
	public int setting_temperature;
	public String schema;
	public int on_off;
	public String air_blower;
	/**
	 * //代表开关
	 */
	public int status;
	
	/**
	 * 配合后台，把原来status改为respone_status
	 */
	public int respone_status;
	public String message;
	public String device;

}
