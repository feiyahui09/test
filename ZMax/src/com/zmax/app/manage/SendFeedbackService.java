package com.zmax.app.manage;

import com.zmax.app.model.FeeBack;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.utils.Constant;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class SendFeedbackService extends IntentService {
	public static final String CONTACT_CONTENT = "contact_content";
	public static final String ADVISE_CONTENT = "advise_content";
	Handler handler;
	
	public SendFeedbackService() {
		this("SendFeedbackService");
	}
	
	public SendFeedbackService(String name) {
		super(name);
		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String contact = intent.getStringExtra(CONTACT_CONTENT);
		String advise = intent.getStringExtra(ADVISE_CONTENT);
		final FeeBack feeBack = NetAccessor.sendFeedBack(this, contact, advise);
		handler = new Handler(getMainLooper());
		if (feeBack != null) {
			
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(SendFeedbackService.this, "" + feeBack.message, 300).show();
				}
			}, 100);
			
		}
		else {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(SendFeedbackService.this, "提交内容失败，请稍后重试！", 300).show();
					
				}
			});
			
		}
		
		Intent it = new Intent(Constant.FEEDBACK_SENDED_ACTION);
		sendBroadcast(it);
	}
	
}
