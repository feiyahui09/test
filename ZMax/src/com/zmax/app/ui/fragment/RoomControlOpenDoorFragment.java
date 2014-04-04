package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.ui.RoomControlActivity.PageChangedCallback;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlOpenDoorFragment extends Fragment {
	
	protected View view;
	
	private VerticalViewPager vpager;
	
	private RoomControlAdapter adapter;
	
	private VerticalChangedCallback callback;
	
	private PageChangedCallback pageChangedCallback;
	/**
	 * above views
	 */
	private ImageView iv;
	
	public RoomControlOpenDoorFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}
	
	public RoomControlOpenDoorFragment(VerticalChangedCallback callback, PageChangedCallback pageChangedCallback) {
		this.callback = callback;
		this.pageChangedCallback = pageChangedCallback;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_fragment, null);
		vpager = (VerticalViewPager) view.findViewById(R.id.vpager);
		adapter = new RoomControlAdapter(getActivity(), null);
		vpager.setAdapter(adapter);
		
		adapter.addViews(getTVView(getActivity(), inflater));
		
		vpager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (callback != null) {
					
					callback.onCallBack(position == 0 ? true : false);
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
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			if (callback != null) {
				if (adapter != null && adapter.getCount() > 0)
					callback.onCallBack(vpager.getCurrentItem() == 0 ? true : false);
				else
					callback.onCallBack(true);
				
			}
		}
	}
	
	public static List<View> getTVView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(getTVBehind(inflater));
		
		return mList;
	}
	
	private static View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_tv);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("电视");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);
		
		return view;
	}
	
	private static View getTVBehind(LayoutInflater inflater) {
		final LinearLayout ll_digital, ll_orient;
		RadioGroup rg_model;
		RadioButton rb_orient;
		final View view = inflater.inflate(R.layout.room_control_tv_behind, null);
		
		ll_digital = (LinearLayout) view.findViewById(R.id.ll_digital);
		ll_orient = (LinearLayout) view.findViewById(R.id.ll_orient);
		rg_model = ((RadioGroup) view.findViewById(R.id.rg_model));
		rb_orient = ((RadioButton) view.findViewById(R.id.rb_orient));
		
		rg_model.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_digital) {
					ll_digital.setVisibility(View.VISIBLE);
					ll_orient.setVisibility(View.GONE);
				}
				else if (checkedId == R.id.rb_orient) {
					ll_digital.setVisibility(View.GONE);
					ll_orient.setVisibility(View.VISIBLE);
				}
			}
		});
		
		rb_orient.setChecked(true);
		return view;
	}
	
	private void setTvAnimation(TextView textView) {
		
		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		textView.startAnimation(ani);
	}
	
}
