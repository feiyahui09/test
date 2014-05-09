package com.zmax.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.zmax.app.R;
import com.zmax.app.adapter.WelcomePagerAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.WelcomeFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;

public class WelcomeActivity extends BaseFragmentActivity {
	
	private ViewPager pager;
	private WelcomePagerAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (getIntent().getAction() != null && getIntent().getAction().equals(Constant.ACTION_WELCOME_FROM_SETTING)) {
			setContentView(R.layout.welcome);
			init();
			initData();
		}
		else {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DefaultShared.putBoolean("IS_FIRST_INSTALLED", false);
		
	}
	
	private void init() {
		pager = (ViewPager) findViewById(R.id.pager);
		
		adapter = new WelcomePagerAdapter(this);
		pager.setAdapter(adapter);
		
	}
	
	private void initData() {
		
		adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_1));
		adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_2));
		adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_3, new StartAppCallBack() {
			@Override
			public void onStartAPP() {
				if (getIntent().getAction() != null && getIntent().getAction().equals(Constant.ACTION_WELCOME_FROM_SETTING)) {
					finish();
				}
				else {
					startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
					finish();
				}
			}
		}));
	}
	
	public interface StartAppCallBack {
		public void onStartAPP();
	}
	
}
