package com.zmax.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.adapter.WelcomePagerAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.WelcomeFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.widget.VerticalViewPager;

public class WelcomeActivity extends BaseFragmentActivity {
	
	private VerticalViewPager pager;
	private WelcomePagerAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		init();
		initData();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DefaultShared.putBoolean("IS_FIRST_INSTALLED", false);
        MobclickAgent.onPause(this);
	}

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private void init() {
		pager = (VerticalViewPager) findViewById(R.id.pager);
		
		adapter = new WelcomePagerAdapter(this);
		pager.setAdapter(adapter);
		
	}
	
	private void initData() {
		if(1==1)throw  new NullPointerException();
		adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_1));
		adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_2));
        adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_3));
        adapter.addTab(new WelcomeFragment(R.drawable.welcome_img_4, new StartAppCallBack() {
			@Override
			public void onStartAPP() {
				if (getIntent().getAction() != null && getIntent().getAction().equals(Constant.ACTION_WELCOME_FROM_INDEX)) {
					Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
				else {
					finish();
				}
			}
		}));
	}
	
	public interface StartAppCallBack {
		public void onStartAPP();
	}
	
}
