package com.zmax.app.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;

public class ActDetailActivity extends BaseFragmentActivity {

	private Button btn_Back, btn_Share;
	
	private ViewPager pager;
	private ActDetailAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_detail);
		initHeader();
		init();
		initData();
	}
	private void init(){
		pager=(ViewPager) findViewById(R.id.pager);
		
		
		adapter =new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		
		
	}
	private void initData(){
		
		
		adapter.addTab(new ActDetailFirstFragment(R.color.red));
		adapter.addTab(new ActDetailSecondFragment());
		adapter.addTab(new ActDetailThirdFragment(R.color.white));
	}
	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Share = (Button) findViewById(R.id.btn_share);

		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

	}

}
