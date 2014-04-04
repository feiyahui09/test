package com.zmax.app.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.RoomControlAirConditionFragment;
import com.zmax.app.ui.fragment.RoomControlCurtainFragment;
import com.zmax.app.ui.fragment.RoomControlLightingFragment;
import com.zmax.app.ui.fragment.RoomControlTVFragment;
import com.zmax.app.ui.fragment.RoomControlWakeUpFragment;
import com.zmax.app.widget.SmartViewPager;

public class RoomControlActivity extends BaseFragmentActivity {
	
	public interface VerticalChangedCallback {
		public void onCallBack(boolean isCurAbove);
	}
	
	public interface PageChangedCallback {
		public void onPageChanegdCallBack(int index);
	}
	
	private static final String TAG = RoomControlActivity.class.getSimpleName();
	
	private ViewGroup above_content_header;
	private Button btn_Back;
	private TextView tv_title;
	private SmartViewPager pager;
	private ActDetailAdapter adapter;
	private VerticalChangedCallback callback;
	private PageChangedCallback pageChangedCallback;
	public static boolean isCurAbove = true;
	private ImageView iv_right, iv_left;
	private int curPageIndex = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_control);
		initHeader();
		init();
		initData();
	}
	
	private void init() {
		iv_right = (ImageView) findViewById(R.id.iv_right);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_right.setVisibility(View.VISIBLE);
		iv_left.setVisibility(View.VISIBLE);
		iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (curPageIndex > 0) pager.setCurrentItem(--curPageIndex);
			}
		});
		iv_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (curPageIndex < adapter.getCount() - 1) pager.setCurrentItem(++curPageIndex);
			}
		});
		
		pager = (SmartViewPager) findViewById(R.id.pager);
		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		//retained buffer size 6
		pager.setOffscreenPageLimit(6);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				curPageIndex = position;
				showIndicator();
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
		callback = new VerticalChangedCallback() {
			
			@Override
			public void onCallBack(boolean isCurAbove) {
				if (isCurAbove) {
					
					showIndicator();
					pager.setCanScroll(true);
				}
				else {
					hideIndicator();
					pager.setCanScroll(false);
				}
				
			}
		};
		pageChangedCallback = new PageChangedCallback() {
			
			@Override
			public void onPageChanegdCallBack(int index) {
				
				pager.setCurrentItem(index);
				
			}
		};
	}
	
	private void initData() {
		adapter.addTab(new RoomControlLightingFragment(callback));
		adapter.addTab(new RoomControlAirConditionFragment(callback));
		adapter.addTab(new RoomControlTVFragment(callback));
		adapter.addTab(new RoomControlCurtainFragment(callback));
		adapter.addTab(new RoomControlWakeUpFragment(callback));
		pager.setCurrentItem(0);
	}
	
	private void initHeader() {
		above_content_header = (ViewGroup) findViewById(R.id.above_content_header);
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.room_control));
	}
	
	private void hideIndicator() {
		iv_right.setVisibility(View.GONE);
		iv_left.setVisibility(View.GONE);
	}
	
	private void showIndicator() {
		
		if (curPageIndex == 0) {
			iv_right.setVisibility(View.VISIBLE);
			iv_left.setVisibility(View.GONE);
		}
		else if (curPageIndex == adapter.getCount() - 1) {
			
			iv_right.setVisibility(View.GONE);
			iv_left.setVisibility(View.VISIBLE);
		}
		else {
			iv_right.setVisibility(View.VISIBLE);
			iv_left.setVisibility(View.VISIBLE);
		}
		
	}
	
}
