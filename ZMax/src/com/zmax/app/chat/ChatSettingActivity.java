package com.zmax.app.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.manage.SendFeedbackService;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;

public class ChatSettingActivity extends BaseActivity {
	private RadioButton btn_man, btn_feman;
	private Context mContext;
	private EditText et_nick_name;
	private Button btn_Back, btn_Share, btn_login;
	private TextView tv_title;
	
	private class ResponseReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_setting);
		init();
	}
	
	private void init() {
		mContext = this;
		et_nick_name = (EditText) findViewById(R.id.et_nick_name);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("聊天室");
		btn_Back = (Button) findViewById(R.id.btn_back);
		
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		btn_Share.setBackgroundResource(R.drawable.chat_more_menu_sel);
		btn_Share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ChatMoreActivity.class));
			}
		});
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ChatRoomActivity.class));
				
			}
		});
	}
	
}
