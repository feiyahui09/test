package com.zmax.app.model;

import java.io.Serializable;

public class  Television      extends BaseModel  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String device;

	public int status;
	
	public int respone_status;
	public String message;
}
