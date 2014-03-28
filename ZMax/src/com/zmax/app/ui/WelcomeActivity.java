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
		
		adapter.addTab(new WelcomeFragment(R.drawable.act_detail_icon_test));
		adapter.addTab(new WelcomeFragment(R.drawable.ic_launcher));
		adapter.addTab(new WelcomeFragment(R.drawable.logo_facebook));
	}
	
}
