package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.manage.DataInitalService;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.Log;

public class FlashActivity extends BaseFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        MobclickAgent.setDebugMode(false);
        MobclickAgent.updateOnlineConfig( this );

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
		
		startService(new Intent(this, DataInitalService.class));
     }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void goNext() {
		if (DefaultShared.getBoolean("IS_FIRST_INSTALLED", true)) {
			Intent intent = new Intent(this, WelcomeActivity.class);
			intent.setAction(Constant.ACTION_WELCOME_FROM_INDEX);
			startActivity(intent);
		}
		else {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		DefaultShared.putBoolean("IS_FIRST_INSTALLED", false);
		finish();
	}
	
}
