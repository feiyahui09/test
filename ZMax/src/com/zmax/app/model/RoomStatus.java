package com.zmax.app.model;

import java.io.Serializable;

public class RoomStatus extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public Light light;
	public AirCondition airCondition;
	public Television television;
}
