package com.zmax.app.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zmax.app.R;
import com.zmax.app.ui.fragment.ActListFragment;
import com.zmax.app.ui.fragment.DefaultFragment;
import com.zmax.app.ui.fragment.HotelBookFragment;
import com.zmax.app.ui.fragment.MoreMenuFragment;

/**
 * This activity is an example of a responsive Android UI. On phones, the
 * SlidingMenu will be enabled only in portrait mode. In landscape mode, it will
 * present itself as a dual pane layout. On tablets, it will will do the same
 * general thing. In portrait mode, it will enable the SlidingMenu, and in
 * landscape mode, it will be a dual pane layout.
 * 
 * @author jeremyssss
 * 
 */
public class BaseSlidingFragmentActivity extends SlidingFragmentActivity {
	
	protected Fragment mContent;
	protected RadioGroup rg_top_title;
	protected Button btn_more, btn_share;
	
	protected HotelBookVisivleCallback visivleCallback;
	
	// callback解决verticalviewpager初始化时候的bug
	public interface HotelBookVisivleCallback {
		
		public void onCallBack();
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.above_content_frame);
		//
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.behind_menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
		// set the Above View Fragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
			// int checkedId = savedInstanceState.getInt("check_view_id");
			// if (checkedId == R.id.btn_hotel_book)
			// switchContent(new HotelBookFragment(R.color.red));
			// else if (checkedId == R.id.btn_activities)
			// switchContent(new ActListFragment(R.color.red));
		}
		if (mContent == null) mContent = new DefaultFragment(R.color.white);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
		
		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MoreMenuFragment()).commit();
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);
		
		initHeader();
	}
	
	private void initHeader() {
		/*
		 * 头部tab切换 活动和预订酒店
		 */
		rg_top_title = (RadioGroup) findViewById(R.id.above_content_second_header);
		
		rg_top_title.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_hotel_book)
					switchContent(new HotelBookFragment(R.color.red));
				else if (checkedId == R.id.btn_activities) switchContent(new ActListFragment());
			}
		});
		btn_more = (Button) findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggle();
			}
		});
		
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// toggle();
			}
		});
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
		outState.putInt("check_view_id", rg_top_title.getCheckedRadioButtonId());
		
	}
	
	public void switchContent(final Fragment fragment) {
		// if (fragment == null || fragment.getActivity() == null
		// || !fragment.isAdded())
		// return;
		mContent = fragment;
		if (mContent instanceof ActListFragment) {
			findViewById(R.id.above_content_second_header).setVisibility(View.VISIBLE);
			((RadioButton) findViewById(R.id.btn_activities)).setChecked(true);
		}
		else if (mContent instanceof HotelBookFragment) {
			findViewById(R.id.above_content_second_header).setVisibility(View.VISIBLE);
			((RadioButton) findViewById(R.id.btn_hotel_book)).setChecked(true);
			
		}
		else
			findViewById(R.id.above_content_second_header).setVisibility(View.GONE);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
				// if (mContent instanceof HotelBookVisivleCallback) {
				// ((HotelBookVisivleCallback) mContent).onCallBack();
				// }
			}
		}, 50);
		
	}
	
	public void onBirdPressed(int pos) {
		
	}
	
}
