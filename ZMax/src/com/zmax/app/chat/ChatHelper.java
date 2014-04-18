package com.zmax.app.chat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.DataListener;
import com.netease.pomelo.PomeloClient;
import com.zmax.app.utils.Log;

public class ChatHelper {
	
	private PomeloClient client;
	private String name, gender, uid;
	private Handler handler;
	
	public interface GateEntryCallBack {
		public void onGateEnterSuccess();
		
		public void onGateEnterFailed();
		
	}
	
	public void init(Context context, String serverIP, int serverPort, String uid, final String authToken, String name, String gender,
			final GateEntryCallBack gateEntryCallBack, final DataCallBack connectorEntertyCallBack, final DataListener onChatCallBack)
			throws JSONException {
		this.name = name;
		this.gender = gender;
		this.uid = uid;
		
		handler = new Handler(context.getMainLooper());
		client = new PomeloClient(serverIP, serverPort);
		Log.i("gate.queryEntry ");
		client.init();
		// 负债均衡
		client.request("gate.gateHandler.queryEntry", new JSONObject().put("uid", uid), new DataCallBack() {
			
			@Override
			public void responseData(JSONObject msg) {
				Log.i("gate:response: " + msg.toString());
				client.disconnect();
				try {
					client = new PomeloClient(msg.getString("host"), msg.getInt("port"));
					Log.i("connector.enter ");
					client.init();
					// 真正分配到个服务器，
					client.request("connector.entryHandler.enter", new JSONObject().put("auth_token", authToken), connectorEntertyCallBack);
					client.on("onChat", onChatCallBack);
				}
				catch (JSONException e) {
					gateEntryCallBack.onGateEnterFailed();
					e.printStackTrace();
				}
				
			}
			
		});
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
			client.request("chat.chatHandler.send", new JSONObject().put("content", content).put("name", name).put("gender", gender));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void send(String content, DataCallBack sendCallBack) {
		try {
			Log.i("");
			client.request("chat.chatHandler.send", new JSONObject().put("content", content).put("name", name).put("gender", gender),
					sendCallBack);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
