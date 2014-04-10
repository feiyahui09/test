package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.manage.SendFeedbackService;
import com.zmax.app.ui.base.BaseActivity;

public class FeedBackActivity extends BaseActivity {
	private Button btn_Back, btn_share;
	private Context mContext;
	private EditText tv_advise, tv_contacts;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		init();
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
