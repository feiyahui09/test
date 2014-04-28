package com.zmax.app.chat;

import io.socket.IOCallback;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.zmax.app.chat.promelo.DataCallBack;
import com.zmax.app.chat.promelo.DataEvent;
import com.zmax.app.chat.promelo.DataListener;
import com.zmax.app.chat.promelo.PomeloClient;
import com.zmax.app.utils.Log;

public class ChatHelper {
	static ChatHelper helper;
	private PomeloClient client;
	private String name, rid;
	int gender, uid;
	ClientCallback clientCallback;
	IOCallback ioCallback;
	
	public static ChatHelper getHelper() {
		if (helper == null) {
			helper = new ChatHelper();
		}
		return helper;
	}
	
	public void init(Context context, String serverIP, int serverPort, int uid, String rid, final String authToken, String name,
			int gender, final ClientCallback clientCallback, final IOCallback ioCallback) throws JSONException {
		this.name = name;
		this.gender = gender;
		this.uid = uid;
		this.rid = rid;
		this.ioCallback = ioCallback;
		this.clientCallback = clientCallback;
		
		client = new PomeloClient(serverIP, serverPort);
		Log.i("gate.queryEntry");
		client.init(ioCallback, clientCallback);
		// 负债均衡
		client.request("gate.gateHandler.queryEntry", new JSONObject().put("uid", uid).put("rid", rid), new DataCallBack() {
			@Override
			public void responseData(JSONObject msg) {
				Log.i("gate:response: " + msg.toString());
				client.disconnect();
				if (clientCallback != null) clientCallback.onGateEnter(msg);
				
				try {
					client = new PomeloClient(msg.getString("host"), msg.getInt("port"));
					Log.i("connector.enter ");
					client.init(ioCallback, clientCallback);
					// 真正分配到个服务器，
					client.request("connector.entryHandler.enter", new JSONObject().put("auth_token", authToken), new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							Log.i("connector.enter:response : " + msg.toString());
							if (clientCallback != null) clientCallback.onConnectorEnter(msg);
						}
					});
					client.on("onChat", new DataListener() {
						public void receiveData(DataEvent event) {
							JSONObject msg = event.getMessage();
							Log.i("onChat: " + msg.toString());
							if (clientCallback != null) {
								clientCallback.onChat(msg.optString("body"));
								
							}
						}
					});
				}
				catch (Exception e) {
					if (clientCallback != null) clientCallback.onEnterFailed(e);
					e.printStackTrace();
				}
				
			}
			
		});
	}
	
	public void modifyInfo(String name, int gender) {
		this.name = name;
		this.gender = gender;
	}
	
	public void disConnect() {
		try {
			if (client != null) {
				client.disconnect();
				Log.i("");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String content) {
		try {
			Log.i("");
			client.inform("chat.chatHandler.send", new JSONObject().put("content", content).put("name", name).put("gender", gender));
		}
		catch (Exception e) {
			e.printStackTrace();
			if (clientCallback != null) clientCallback.onSendFailed(e);
		}
		
	}
	
	public void send(String content, DataCallBack sendCallBack) {
		if (TextUtils.isEmpty(content)) return;
		try {
			Log.i("");
			client.request("chat.chatHandler.send", new JSONObject().put("content", content).put("name", name).put("gender", gender),
					sendCallBack);
		}
		catch (Exception e) {
			e.printStackTrace();
			if (clientCallback != null) clientCallback.onSendFailed(e);
		}
		
	}
	
}
