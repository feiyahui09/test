package com.zmax.app.ui.base;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zmax.app.R;
import com.zmax.app.ui.DocumentsActivity;
import com.zmax.app.ui.fragment.AccountFragment;
import com.zmax.app.ui.fragment.ActListFragment;
import com.zmax.app.ui.fragment.DefaultFragment;
import com.zmax.app.ui.fragment.HotelBookFragment;
import com.zmax.app.ui.fragment.MoreMenuFragment;
import com.zmax.app.ui.fragment.PlayInZmaxFragment;
import com.zmax.app.ui.fragment.PlayInZmaxLoginFragment;
import com.zmax.app.ui.fragment.SettingFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Constant.LOAD_STATE;
import com.zmax.app.utils.Log;

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
public class BaseSlidingFragmentActivity extends SlidingFragmentActivity implements MoreMenuFragment.TabSelectedListener {
	
	protected Fragment mContent;
	protected RadioGroup rg_top_title;
	protected Button btn_more, btn_share;
	
	protected HotelBookVisivleCallback visivleCallback;
	protected Handler handler = new Handler();
	
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
		// if (savedInstanceState != null) {
		// mContent =
		// getSupportFragmentManager().getFragment(savedInstanceState,
		// "mContent");
		// // int checkedId = savedInstanceState.getInt("check_view_id");
		// // if (checkedId == R.id.btn_hotel_book)
		// // switchContent(new HotelBookFragment(R.color.red));
		// // else if (checkedId == R.id.btn_activities)
		// // switchContent(new ActListFragment(R.color.red));
		// }
		if (mContent == null) mContent = new DefaultFragment();
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
		initHashMap();
	}
	
	TabListener<ActListFragment> actTabListener = new TabListener<ActListFragment>(this, "ActListFragment", ActListFragment.class);
	TabListener<HotelBookFragment> hotelbookTabListener = new TabListener<HotelBookFragment>(this, "HotelBookFragment",
			HotelBookFragment.class);
	TabListener<PlayInZmaxLoginFragment> playInZmaxLoginTabListener = new TabListener<PlayInZmaxLoginFragment>(this,
			"PlayInZmaxLoginFragment", PlayInZmaxLoginFragment.class);
	TabListener<SettingFragment> settingTabListener = new TabListener<SettingFragment>(this, "SettingFragment", SettingFragment.class);
	TabListener<AccountFragment> accountTabListener = new TabListener<AccountFragment>(this, "AccountFragment", AccountFragment.class);
	TabListener<PlayInZmaxFragment> playInZmaxTabListener = new TabListener<PlayInZmaxFragment>(this, "PlayInZmaxFragment",
			PlayInZmaxFragment.class);
	
	HashMap<Integer, TabListener> hashMap = new HashMap<Integer, TabListener>();
	
	private void initHashMap() {
		hashMap.put(R.id.btn_activities, actTabListener);
		hashMap.put(R.id.btn_hotel_book, hotelbookTabListener);
		
		hashMap.put(R.id.ll_menu_playzmax, playInZmaxLoginTabListener);
		hashMap.put(R.id.ll_menu_playzmax + 100, playInZmaxTabListener);
		
		hashMap.put(R.id.ll_menu_myaccount, accountTabListener);
		hashMap.put(R.id.ll_menu_setting, settingTabListener);
		
	}
	
	@Override
	public void handleSeleceted(int seleted_id, boolean isInitial) {
		
		for (Integer item_id : hashMap.keySet()) {
			boolean _isInitial = true;
			if (item_id == seleted_id) {
				if (item_id == R.id.btn_activities) {
					_isInitial = false;
					findViewById(R.id.above_content_second_header).setVisibility(View.VISIBLE);
					((RadioButton) findViewById(R.id.btn_activities)).setChecked(true);
				}
				else if (item_id == R.id.btn_hotel_book) {
					findViewById(R.id.above_content_second_header).setVisibility(View.VISIBLE);
					((RadioButton) findViewById(R.id.btn_hotel_book)).setChecked(true);
					_isInitial = false;
				}
				else
					findViewById(R.id.above_content_second_header).setVisibility(View.GONE);
				
				if (item_id == R.id.ll_menu_playzmax) {
					hideLogoutView(false);
				}
				else if (item_id == R.id.ll_menu_playzmax + 100) {
					showLogoutView();
				}
				else {
					hideLogoutView(true);
				}
				
				hashMap.get(item_id).onTabSelected((isInitial || _isInitial) ? new Object() : null,
						getSupportFragmentManager().beginTransaction());
			}
			else {
				hashMap.get(item_id).onTabUnselected(null, getSupportFragmentManager().beginTransaction());
			}
		}
		getSlidingMenu().showContent();
		
	}
	
	public void showLogoutView() {
		btn_share.setVisibility(View.VISIBLE);
		btn_share.setBackgroundResource(R.drawable.playzmax_quit_btn_sel);
		
		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constant.saveLogin(null);
				
				// switchContent(new PlayInZmaxLoginFragment());
				handleSeleceted(R.id.ll_menu_playzmax, true);
			}
		});
	}
	
	public void hideLogoutView(boolean isGone) {
		if (isGone) {
			btn_share.setVisibility(View.GONE);
			return;
		}
		btn_share.setVisibility(View.VISIBLE);
		btn_share.setBackgroundResource(R.drawable.menu_guide_btn_sel);
		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BaseSlidingFragmentActivity.this, DocumentsActivity.class);
				intent.putExtra(Constant.Documents.DOCUMENTS_TYPE_KEY, Constant.Documents.GUIDE_TYPE);
				startActivity(intent);
			}
		});
	}
	
	private void initHeader() {
		/*
		 * 头部tab切换 活动和预订酒店
		 */
		rg_top_title = (RadioGroup) findViewById(R.id.above_content_second_header);
		rg_top_title.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// if (checkedId == R.id.btn_hotel_book)
				// switchContent(new HotelBookFragment());
				// else if (checkedId == R.id.btn_activities) switchContent(new
				// ActListFragment());
				if (checkedId == R.id.btn_hotel_book) {
					handleSeleceted(checkedId, HotelBookFragment.state==LOAD_STATE.FAILED);
				}
				else if (checkedId == R.id.btn_activities) {
					handleSeleceted(checkedId, ActListFragment.state==LOAD_STATE.FAILED);
				}
			}
		});
		btn_more = (Button) findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggle();
				hideInput();
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
	
	public void hideInput() {
		
		if (getCurrentFocus() != null) { // 是否存在焦点
		
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// getSupportFragmentManager().putFragment(outState, "mContent",
		// mContent);
		outState.putInt("check_view_id", rg_top_title.getCheckedRadioButtonId());
		
	}
	
	public void switchContent(final Fragment fragment) {
		if (1 == 1) {
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
			
			getSlidingMenu().showContent();
			
			return;
		}
		// Log.i("[maxMemory]:  " + Runtime.getRuntime().maxMemory()/1000+"k");
		// Log.i("[totalMemory]:  " +
		// Runtime.getRuntime().totalMemory()/1000+"k");
		// Log.i("[freeMemory]:   " +
		// Runtime.getRuntime().freeMemory()/1000+"k");
		//
		// System.gc();
		//
		// Log.i("after  [maxMemory]:  " +
		// Runtime.getRuntime().maxMemory()/1000+"k");
		// Log.i("after  [totalMemory]:  " +
		// Runtime.getRuntime().totalMemory()/1000+"k");
		// Log.i("after  [freeMemory]:   " +
		// Runtime.getRuntime().freeMemory()/1000+"k");
		
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
	
	public static class TabListener<T extends Fragment> implements OnCheckedChangeListener {
		private final FragmentActivity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;
		
		public TabListener(FragmentActivity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}
		
		public TabListener(FragmentActivity activity, String tag, Class<T> clz, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;
			
			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
			detachOld(mFragment);
		}
		
		private void detachOld(Fragment fragment) {
			// if (fragment != null && !fragment.isDetached()) {
			if (fragment != null) {
				Log.i("@@");
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.remove(fragment);
				ft.commit();
			}
		}
		
		public void onTabSelected(Object tab, FragmentTransaction ft) {
			if (mFragment == null || tab != null) {
				if (tab != null) detachOld(mFragment);
				Log.i("@@  " + mClass.getSimpleName() + " -- instantiate,new ");
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(R.id.content_frame, mFragment, mTag);
				ft.commit();
			}
			else {
				Log.i("@@  " + mClass.getSimpleName() + " --  not instantiate,use old ");
				ft.show(mFragment);
				// ft.attach(mFragment);
				ft.commit();
			}
			
		}
		
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// ft.detach(mFragment);
				ft.hide(mFragment);
				ft.commit();
			}
			
		}
		
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			
		}
	}
	
}
