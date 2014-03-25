package com.zmax.app.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.adapter.WelcomePagerAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;
import com.zmax.app.ui.fragment.WelcomeFragment;
import com.zmax.app.utils.FileUtils;
import com.zmax.app.utils.ShareUtils;

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
