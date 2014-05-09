package com.zmax.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.utils.DefaultShared;

public class FlashActivity extends BaseFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		setContentView(layout);
		layout.setBackgroundResource(R.drawable.flash);
		
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		layout.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				goNext();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
		});
	}
	
	private void goNext() {
		if (DefaultShared.getBoolean("IS_FIRST_INSTALLED", true)) {
			startActivity(new Intent(this, WelcomeActivity.class));
		}
		else {
			startActivity(new Intent(this, MainActivity.class));
		}
		DefaultShared.putBoolean("IS_FIRST_INSTALLED", false);
		finish();
	}
	
}
