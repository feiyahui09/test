package com.zmax.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.model.ActDetail;
import com.zmax.app.task.GetActDetailTask;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.ShareUtils;

public class ActDetailActivity extends BaseFragmentActivity {
	
	private Button btn_Back, btn_Share;
	
	private ViewPager pager;
	private ActDetailAdapter adapter;
	private ActDetail actDetail;
	private GetActDetailTask getActDetailTask;
	private String city, date;
	
	private int curPosition = 0;
	
	public interface RefreshDataCallBack {
		
		public void onRefresh(ActDetail detail);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_detail);
		init();
		initHeader();
	}
	
	private void init() {
		city = getIntent().getStringExtra(Constant.Acts.CITY_KEY);
		date = getIntent().getStringExtra(Constant.Acts.DATE_KEY);
		
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				curPosition = position;
				Fragment fragment = adapter.getItem(position);
				if (fragment instanceof RefreshDataCallBack) {
					((RefreshDataCallBack) fragment).onRefresh(actDetail);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		adapter.addTab(new ActDetailFirstFragment(city, date));
		adapter.addTab(new ActDetailSecondFragment());
		adapter.addTab(new ActDetailThirdFragment());
		
		getActDetailTask = new GetActDetailTask(this, new GetActDetailTask.TaskCallBack() {
			
			@Override
			public void onCallBack(ActDetail result) {
				if (result != null && result.status == 200) {
					initData(result);
				}
				
			}
		});
		
		int actid = getIntent().getIntExtra(Constant.Acts.ID_KEY, -1);
		if (actid > 0) getActDetailTask.execute(String.valueOf(actid));
	}
	
	private void initData(ActDetail result) {
		// pager.setCurrentItem(curPosition);
	}
	
	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_more);
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ShareUtils().showShare(ActDetailActivity.this, false, null);
				
			}
		});
		
	}
	
}
