package com.zmax.app.chat.promelo;

import org.json.JSONObject;

/**
 * Callback function of server response.
 * 
 */
public interface DataCallBack {
	void responseData(JSONObject message);
}
