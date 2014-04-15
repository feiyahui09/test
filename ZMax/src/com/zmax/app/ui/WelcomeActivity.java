package com.zmax.app.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.zmax.app.R;
import com.zmax.app.adapter.WelcomePagerAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.WelcomeFragment;

public class WelcomeActivity extends BaseFragmentActivity {
	
	private ViewPager pager;
	private WelcomePagerAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		init();
		initData();
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
				finish();
			}
		}));
	}
	
	public interface StartAppCallBack {
		public void onStartAPP();
	}
	
}
