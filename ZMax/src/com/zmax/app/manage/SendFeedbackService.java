package com.zmax.app.manage;

import com.zmax.app.model.FeeBack;
import com.zmax.app.net.NetAccessor;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class SendFeedbackService extends IntentService {
	public static final String CONTACT_CONTENT = "contact_content";
	public static final String ADVISE_CONTENT = "advise_content";
	
	public SendFeedbackService() {
		this("SendFeedbackService");
	}
	
	public SendFeedbackService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String contact = intent.getStringExtra(CONTACT_CONTENT);
		String advise = intent.getStringExtra(ADVISE_CONTENT);
		FeeBack feeBack = NetAccessor.sendFeedBack(this, contact, advise);
		if (feeBack != null) {
			Toast.makeText(this, "" + feeBack.message, 300).show();
		}
	}
}
