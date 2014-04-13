package com.zmax.app.model;

import java.io.Serializable;

public class Update implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int status;
	public String message;
	public String tag;
	public String description;
	public String package_name;
}
