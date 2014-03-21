package com.zmax.app.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;

public class ActDetailFlashActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_detail_flash);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				goActDetail();
			}
		};
		timer.schedule(task, 900);
	}

	private void goActDetail() {
		startActivity(new Intent(this, ActDetailActivity.class));
		finish();
	}

}
