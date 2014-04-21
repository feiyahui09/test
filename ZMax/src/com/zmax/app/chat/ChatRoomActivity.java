package com.zmax.app.chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.DataListener;
import com.zmax.app.R;
import com.zmax.app.chat.ChatHelper.ConnectorEntryCallback;
import com.zmax.app.chat.ChatHelper.OnChatCallBack;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;

public class ChatRoomActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	
	private Button btn_Back, btn_Share, btn_send;
	private ImageView iv_pic, iv_emotion;
	private EditText et_edit;
	private ListView lv_chat;
	private ChatListAdapter adapter;
	private ChatHelper chatHelper;
	private DataCallBack connectorEntertyCallBack;
	private DataListener onChatCallBack;
	// private String userName = "红孩儿";
	// private String userid = "2";
	// private String userToken = "token2";
	// private String userGender = "女";
	private String userName = "逗比";
	private String userid = "1";
	private String userToken = "sdtoken1";
	private String userGender = "男";
	//
	private Handler handler = new Handler();
	private Runnable sendRunnable = new Runnable() {
		
		@Override
		public void run() {
			final String str;
			if (userid.equals("2"))
				str = "自动广播：你是猴子请来的逗比么     ";
			else
				str = "自动广播：我是逗比        ";
			
			chatHelper.send(str, new DataCallBack() {
				public void responseData(JSONObject msg) {
					// handle data here
					Log.i("send: response  " + msg.toString());
					
				}
			});
			
			handler.postDelayed(this, 16000);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_room);
		init();
		initChatPomelo();
	}
	
	private void init() {
		mContext = this;
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_emotion = (ImageView) findViewById(R.id.iv_emotion);
		btn_send = (Button) findViewById(R.id.btn_send);
		
		iv_pic.setOnClickListener(this);
		iv_emotion.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_Share.setOnClickListener(this);
		btn_Back.setOnClickListener(this);
		
		et_edit = (EditText) findViewById(R.id.et_edit);
		et_edit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				chatHelper.send(et_edit.getText().toString(), new DataCallBack() {
					@Override
					public void responseData(JSONObject arg0) {
						et_edit.clearComposingText();
						et_edit.setText("");
						Log.i("" + arg0.toString());
					}
				});
				return false;
			}
		});
		
		lv_chat = (ListView) findViewById(R.id.list_view);
		adapter = new ChatListAdapter(mContext);
		lv_chat.setAdapter(adapter);
		
	}
	
	private void initChatPomelo() {
		chatHelper = ChatHelper.getHelper();
		try {
			chatHelper.init(this, "192.168.10.46", 3014, userid, userToken, userName, userGender, null, new ConnectorEntryCallback() {
				
				@Override
				public void onConnect(final JSONObject body) {
					
					// final JSONObject body = msg.optJSONObject("body");
					if (body == null) {
						Toast.makeText(mContext, "错误，请稍后再试!", 2233).show();
						return;
					}
					int code = 200;
					boolean isError;
					code = body.optInt("code", 200);
					final String message = body.optString("message");
					isError = body.optBoolean("error");
					
					if (!isError) {
						// handler.postDelayed(sendRunnable, 4000);
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(mContext, "欢迎进入聊天室，当前登录信息：、\n" + body.toString(), 2233).show();
							}
						});
					}
					else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(mContext, message, 2233).show();
							}
						});
						chatHelper.disConnect();
					}
					
				}
			}, new OnChatCallBack() {
				@Override
				public void onChat(final JSONObject msg) {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							ChatBody chatBody = JsonMapperUtils.toObject(msg.optString("body"), ChatBody.class);
							if (chatBody.from.equals(userName))
								show(chatBody.msg.content, ChatListAdapter.VALUE_RIGHT_TEXT, chatBody.from);
							else
								show(chatBody.msg.content, ChatListAdapter.VALUE_LEFT_TEXT, chatBody.from);
						}
					});
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
			case R.id.btn_send:
				chatHelper.send(et_edit.getText().toString(), new DataCallBack() {
					@Override
					public void responseData(JSONObject arg0) {
						et_edit.clearComposingText();
						et_edit.setText("");
						Log.i("" + arg0.toString());
					}
				});
				break;
			case R.id.iv_pic:
				new AlertDialog.Builder(mContext).setTitle("title")
						.setItems(R.array.pic_dialog_items, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0) {
									
								}
								else if (which == 1) {
									
								}
							}
						}).show();
				break;
			case R.id.iv_emotion:
				
				break;
			
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_share:
				
				break;
			default:
				break;
		}
	}
	
	private void show(String content, int type, String name) {
		
		// adapter.addItem(new Message(ChatListAdapter.MSG_TYPE[new
		// Random().nextInt(ChatListAdapter.MSG_TYPE.length)], content + " \n"
		// + new Date()));
		adapter.addItem(new Message(type, content + " \n" + new SimpleDateFormat("HH:mm:ss").format(new Date()), name));
		lv_chat.setSelection(lv_chat.getCount() - 1);
		Log.d("@@adapter:" + adapter.getCount());
		Log.d("@@lv_chat:" + lv_chat.getCount());
	}
	
	private void show(String content) {
		adapter.addItem(new Message(ChatListAdapter.MSG_TYPE[new Random().nextInt(ChatListAdapter.MSG_TYPE.length)], content + " \n"
				+ new Date(), " null name"));
		lv_chat.setSelection(lv_chat.getCount() - 1);
		Log.d("@@adapter:" + adapter.getCount());
		Log.d("@@lv_chat:" + lv_chat.getCount());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHelper != null) chatHelper.disConnect();
	}
	
	private List<Message> getMyData() {
		
		List<Message> msgList = new ArrayList<Message>();
		Message msg;
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("食堂真难吃啊");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:23");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("我就说食堂的饭难吃吧，你不相信！");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("好吧，这次听你的了。");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_IMAGE);
		msg.setValue("2012-12-23 下午2:25");
		msgList.add(msg);
		//
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("就要圣诞了，有什么安排没有？");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("没有啊，你呢？");
		msgList.add(msg);
		//
		// msg = new Message();
		// msg.setType(ChatListAdapter.VALUE_TIME_TIP);
		// msg.setValue("2012-12-23 下午3:25");
		// msgList.add(msg);
		//
		// msg = new Message();
		// msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		// msg.setValue("7min");
		// msgList.add(msg);
		//
		// msg = new Message();
		// msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		// msg.setValue("高帅富有三宝 木耳 跑车 和名表，" +
		// "黑木耳有三宝 美瞳 备胎 黑丝脚 ，穷矮挫有三宝 AV 手纸 射得早 ，女神有三宝 干嘛 呵呵 去洗澡 ，宅男有三宝 Dota 基友 破电脑 "
		// + "女屌丝有三宝 虎背 熊腰 眼睛小 ， 女屌丝还有三宝 饼脸 花痴 卖萌照");
		// msgList.add(msg);
		//
		// msg = new Message();
		// msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		// msg.setValue("碉堡了");
		// msgList.add(msg);
		//
		// msg = new Message();
		// msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		// msg.setValue("7min");
		// msgList.add(msg);
		//
		// msg = new Message();
		// msg.setType(ChatListAdapter.VALUE_RIGHT_IMAGE);
		// msg.setValue("7min");
		// msgList.add(msg);
		
		return msgList;
		
	}
	
}
