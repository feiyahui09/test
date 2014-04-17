package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.DataEvent;
import com.netease.pomelo.DataListener;
import com.netease.pomelo.PomeloClient;

public class ChatTest {
	public PomeloClient client;
	public PomeloClient client2;
	public static Thread t;
	
	public static void main(String args[]) throws JSONException, InterruptedException, IOException {
		t = Thread.currentThread();
		ChatTest pa = new ChatTest();
		pa.pomeloRun();
		// t.wait();
		System.out.println("T.T");
		
		// pa.getCurrentClient().inform("chat.chatHandler.send", new
		// JSONObject().put("rid", 1).put("content", "xxoo").put("from",
		// "xingxing").put("target", "*"));
		
	}
	
	public PomeloClient getCurrentClient() {
		return client;
		
	}
	
	public void pomeloRun() throws JSONException, InterruptedException {
		client = new PomeloClient("127.0.0.1", 3014);
		
		client.init();
		
		client.request("gate.gateHandler.queryEntry", new JSONObject().put("uid", "1"), new DataCallBack() {
			
			@Override
			public void responseData(JSONObject msg) {
				System.out.println(msg.toString() + "~hahah");
				client.disconnect();
				try {
					client = new PomeloClient(msg.getString("host"), msg.getInt("port"));
					
					client.init();
					
					client.request("connector.entryHandler.enter", new JSONObject().put("auth_token", "token"), new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							System.out.println(msg.toString() + "~hahah");
							
						}
					});
					//
					client.on("onChat", new DataListener() {
						public void receiveData(DataEvent event) {
							JSONObject msg = event.getMessage();
							// handle data from server
							System.out.println(msg.toString() + "~hehe");
						}
					});
					
					while (true) {
						String str = ChatTest.input();
						// new ThreadTest(client, str).run();
						ChatTest.send(client, str);
					}
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
		System.out.println("okokokok");
	}
	
	public static void send(PomeloClient client, String str) {
		try {
			client.request("chat.chatHandler.send",
					new JSONObject().put("rid", 1).put("content", str).put("from", "xingxing").put("target", "*"), new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							System.out.println(msg.toString() + "~wuwu");
						}
					});
		}
		catch (JSONException e) {
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
			System.out.println("发生I/O错误!!!");
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
			System.out.println("data:" + data);
			po.request("chat.chatHandler.send",
					new JSONObject().put("rid", 1).put("content", data).put("from", "xingxing").put("target", "*"), new DataCallBack() {
						public void responseData(JSONObject msg) {
							// handle data here
							System.out.println(msg.toString() + "~wuwu");
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
