package com.zmax.app.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
	protected Fragment mContentSecondary;
	private RadioGroup rg_top_title;
	private Button btn_more;

	private LinearLayout above_content_second_header;
	private FrameLayout content_frame_secondary, content_frame;

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
			getSlidingMenu()
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");

			mContentSecondary = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContentSecondary");
		}
		if (mContent == null)
			mContent = new DefaultFragment(R.color.white);
		if (mContentSecondary == null)
			mContentSecondary = new HotelBookFragment(R.color.white);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_secondary, mContentSecondary)
				.commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MoreMenuFragment()).commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		initView();
	}

	private void initView() {

		above_content_second_header = (LinearLayout) findViewById(R.id.above_content_second_header);
		content_frame_secondary = (FrameLayout) findViewById(R.id.content_frame_secondary);
		content_frame = (FrameLayout) findViewById(R.id.content_frame);
		/*
		 * 头部tab切换 活动和预订酒店
		 */
		rg_top_title = (RadioGroup) findViewById(R.id.rg_top_title);

		rg_top_title.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_hotel_book)
					switchContentAct(new HotelBookFragment(R.color.red));
				else if (checkedId == R.id.btn_activities)
					switchContent(new ActListFragment(R.color.red));
			}
		});
		((RadioButton) findViewById(R.id.btn_activities)).setChecked(true);

		btn_more = (Button) findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggle();
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
		getSupportFragmentManager().putFragment(outState, "mContentSecondary",
				mContentSecondary);
	}

	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		if (mContent instanceof ActListFragment
				|| mContent instanceof HotelBookFragment)
			above_content_second_header.setVisibility(View.VISIBLE);
		else {
			above_content_second_header.setVisibility(View.GONE);

		}
		content_frame_secondary.setVisibility(View.GONE);
		content_frame.setVisibility(View.VISIBLE);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	public void switchContentAct(final Fragment fragment) {
		findViewById(R.id.above_content_second_header).setVisibility(
				View.VISIBLE);

		content_frame_secondary.setVisibility(View.VISIBLE);
		content_frame.setVisibility(View.GONE);

		// if (mContentSecondary == null || !mContentSecondary.isAdded()) {
		// mContentSecondary = fragment;
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.content_frame, fragment).commit();
		// }
	}

	public void onBirdPressed(int pos) {

	}

}
