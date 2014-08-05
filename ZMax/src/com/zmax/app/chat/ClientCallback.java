package com.zmax.app.chat;

import org.json.JSONObject;

public interface ClientCallback {
	
	public void onGateEnter(JSONObject msg);
	
	public void onConnectorEnter(JSONObject msg);
	
	public void onEnterFailed(Exception e);
	
	public void onChat(String bodyMsg);
	
	public void onKick(JSONObject 	bodyMsg);

	
	public void onSendFailed(Exception e);
	
	public void onForbidden(String  errorMessage);

	public void onDevise(JSONObject devise);
}
