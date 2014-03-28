package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.ui.RoomControlActivity.PageChangedCallback;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlAirConditionFragment extends Fragment {
	
	protected View view;
	
	private VerticalViewPager vpager;
	
	private RoomControlAdapter adapter;
	
	private VerticalChangedCallback callback;
	
	private PageChangedCallback pageChangedCallback;
	
	public RoomControlAirConditionFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}
	
	public RoomControlAirConditionFragment(VerticalChangedCallback callback, PageChangedCallback pageChangedCallback) {
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
		adapter.addViews(getAirconditionView(getActivity(), inflater));
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
	
	public static List<View> getAirconditionView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(geAirconditionBehind(inflater, fragmentActivity));
		
		return mList;
	}
	
	private static View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_aircondition);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("空调");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);
		return view;
	}
	
	private static View geAirconditionBehind(LayoutInflater inflater, Context context) {
		
		Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "font.TTF");
		TextView tv_room_control_air_temperature;
		final View view = inflater.inflate(R.layout.room_control_aircondition_behind, null);
		tv_room_control_air_temperature = (TextView) view.findViewById(R.id.tv_room_control_air_temperature);
		tv_room_control_air_temperature.setTypeface(fontFace);
		tv_room_control_air_temperature.setText("26");
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
