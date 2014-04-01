package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.map.RoutePlanDemo;
import com.zmax.app.ui.base.BaseActivity;

public class ChatRoomActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	
	private Button btn_Back, btn_Share, btn_send;
	private ImageView iv_pic, iv_emotion;
	private EditText et_edit;
	
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
		Intent intent = new Intent();
		
		switch (v.getId()) {
			case R.id.iv_pic:
				
				break;
			case R.id.iv_emotion:
				break;
			
			case R.id.btn_send:
				
				break;
			
			default:
				break;
		}
	}
	
}
