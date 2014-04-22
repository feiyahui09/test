package com.zmax.app.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.zmax.app.chat.promelo.DataCallBack;
import com.zmax.app.chat.promelo.DataEvent;
import com.zmax.app.chat.promelo.DataListener;
import com.zmax.app.chat.promelo.PomeloClient;
import com.zmax.app.utils.Log;

public class ChatTest extends Activity {
	public PomeloClient client;
	public PomeloClient client2;
	public static Thread t;
	private int countLeft2Finish = 0;
	private Handler handler = new Handler();
	private Runnable sendRunnable = new Runnable() {
		
		@Override
		public void run() {
			String str = "你是猴子请来的逗比么？？            " + new SimpleDateFormat("hhmmss").format(new Date());
			ChatTest.send(client, str);
			
			// if (countLeft2Finish++ > 11) {
			// Log.i("client.disconnect");
			// client.disconnect();
			// }
			// else
			handler.postDelayed(this, 8000);
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if (client != null) client.disconnect();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					pomeloRun();
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 3333);
	}
	
	// public static void main(String args[]) throws JSONException,
	// InterruptedException, IOException {
	// t = Thread.currentThread();
	// ChatTest pa = new ChatTest();
	// pa.pomeloRun();
	// // t.wait();
	// Log.d("T.T");
	//
	// // pa.getCurrentClient().inform("chat.chatHandler.send", new
	// // JSONObject().put("rid", 1).put("content", "xxoo").put("from",
	// // "xingxing").put("target", "*"));
	//
	// }
	
	public PomeloClient getCurrentClient() {
		return client;
		
	}
	
	public void pomeloRun() throws JSONException, InterruptedException {
		client = new PomeloClient("192.168.10.42", 3014);// first
		// client = new PomeloClient("192.168.10.42", 2014);//doc
		Log.i("gate.queryEntry ");
		client.init(null,null);
		// 负债均衡
		client.request("gate.gateHandler.queryEntry", new JSONObject().put("uid", "2"), new DataCallBack() {
			
			@Override
			public void responseData(JSONObject msg) {
				Log.i("gate:response: " + msg.toString());
				client.disconnect();
				try {
					client = new PomeloClient(msg.getString("host"), msg.getInt("port"));
					Log.i("connector.enter ");
					client.init(null,null);
					// 真正分配到个服务器，
					client.request("connector.entryHandler.enter", new JSONObject().put("auth_token", "token2"), new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							Log.i("connector.enter:response : " + msg.toString());
							handler.postDelayed(sendRunnable, 4000);
							
						}
					});
					//
					client.on("onChat", new DataListener() {
						public void receiveData(DataEvent event) {
							JSONObject msg = event.getMessage();
							// handle data from server
							Log.i("onChat: " + msg.toString());
						}
					});
					
					// t.notifyAll();
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		// client.disconnect();
		// Thread.sleep(1000);
		// client.inform("chat.chatHandler.send", new JSONObject().put("rid",
		// 1).put("content", "xxoo").put("from", "xingxing").put("target",
		// "*"));
	}
	
	public static void send(PomeloClient client, String str) {
		try {
			client.request("chat.chatHandler.send", new JSONObject().put("content", str).put("name", "红孩儿").put("gender", "man"),
					new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							Log.i("send: response  " + msg.toString());
						}
					});
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String input() {
		String str = "";
		InputStreamReader stdin = new InputStreamReader(System.in);// 键盘输入
		BufferedReader bufin = new BufferedReader(stdin);
		try {
			System.out.print("请输入字符： ");
			str = bufin.readLine().toString();
			System.out.print(str);
		}
		catch (IOException E) {
			Log.d("发生I/O错误!!!");
		}
		return str;
	}
	
}

class ThreadTest implements Runnable {
	private PomeloClient po;
	private String data;
	
	public ThreadTest(PomeloClient pc, String st) {
		this.po = pc;
		this.data = st;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Log.d("data:" + data);
			po.request("chat.chatHandler.send",
					new JSONObject().put("rid", 1).put("content", data).put("from", "xingxing").put("target", "*"), new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							Log.d(msg.toString() + "~wuwu");
						}
					});
			
			// po.inform("chat.chatHandler.send", new JSONObject().put("rid",
			// 1).put("content", "xxoo").put("from", "xingxing").put("target",
			// "*"));
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
