package com.zmax.app.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;

public class ChatRoomActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	
	private Button btn_Back, btn_Share, btn_send;
	private ImageView iv_pic, iv_emotion;
	private EditText et_edit;
	private ListView lv_chat;
	private ChatListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_room);
		init();
		initHeader();
	}
	
	private void init() {
		mContext = this;
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_emotion = (ImageView) findViewById(R.id.iv_emotion);
		btn_send = (Button) findViewById(R.id.btn_send);
		et_edit = (EditText) findViewById(R.id.et_edit);
		et_edit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Toast.makeText(mContext, String.valueOf(actionId), Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		iv_pic.setOnClickListener(this);
		iv_emotion.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		
		lv_chat = (ListView) findViewById(R.id.list_view);
		adapter = new ChatListAdapter(mContext, getMyData());
		lv_chat.setAdapter(adapter);
		
	}
	
	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
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
			
			case R.id.btn_send:
				
				break;
			
			default:
				break;
		}
	}
	
	private List<Message> getMyData() {
		
		List<Message> msgList = new ArrayList<Message>();
		Message msg;
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("食堂真难吃啊");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("2012-12-23 下午2:23");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("我就说食堂的饭难吃吧，你不相信！");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("好吧，这次听你的了。");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("2012-12-23 下午2:25");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("就要圣诞了，有什么安排没有？");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("没有啊，你呢？");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("2012-12-23 下午3:25");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("7min");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("高帅富有三宝 木耳 跑车 和名表，" + "黑木耳有三宝 美瞳 备胎 黑丝脚 ，穷矮挫有三宝 AV 手纸 射得早 ，女神有三宝 干嘛 呵呵 去洗澡 ，宅男有三宝 Dota 基友 破电脑 "
				+ "女屌丝有三宝 虎背 熊腰 眼睛小 ， 女屌丝还有三宝 饼脸 花痴 卖萌照");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("碉堡了");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_LEFT_TEXT);
		msg.setValue("7min");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatListAdapter.VALUE_RIGHT_IMAGE);
		msg.setValue("7min");
		msgList.add(msg);
		
		return msgList;
		
	}
	
}
