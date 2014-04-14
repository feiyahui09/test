package com.zmax.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.manage.SendFeedbackService;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;

public class FeedBackActivity extends BaseActivity {
	private Button btn_Back, btn_share;
	private Context mContext;
	private EditText tv_advise, tv_contacts;
	private ResponseReceiver receiver = new ResponseReceiver();
	
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
		setContentView(R.layout.feedback);
		init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(receiver, new IntentFilter(Constant.FEEDBACK_SENDED_ACTION));
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
		
	}
	
	private void init() {
		mContext = this;
		tv_contacts = (EditText) findViewById(R.id.tv_contacts);
		tv_advise = (EditText) findViewById(R.id.tv_advise);
		
		btn_Back = (Button) findViewById(R.id.btn_back);
		
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setVisibility(View.VISIBLE);
		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vertify();
			}
		});
		
	}
	
	private void vertify() {
		if (tv_advise.getText().toString().isEmpty())
			Toast.makeText(mContext, "反馈意见不能为空", 300).show();
		else if (tv_contacts.getText().toString().isEmpty())
			Toast.makeText(mContext, "联系方式不能为空", 300).show();
		else {
			sendFeedback();
		}
	}
	
	private void sendFeedback() {
		Intent intent = new Intent(mContext, SendFeedbackService.class);
		intent.putExtra(SendFeedbackService.ADVISE_CONTENT, tv_advise.getText().toString());
		intent.putExtra(SendFeedbackService.CONTACT_CONTENT, tv_contacts.getText().toString());
		startService(intent);
	}
	
}
