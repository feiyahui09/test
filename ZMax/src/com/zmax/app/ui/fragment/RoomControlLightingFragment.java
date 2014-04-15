package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.ui.RoomControlActivity.PageChangedCallback;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlLightingFragment extends Fragment {
	/*
	 * parent view
	 */
	protected View view;
	
	private VerticalViewPager vpager;
	
	private RoomControlAdapter adapter;
	
	private VerticalChangedCallback callback;
	
	private PageChangedCallback pageChangedCallback;
	
	/* above views */
	private ImageView big_icon;
	private TextView tv_mode, tv_mode_detail;
	
	/* behind views */
	
	private ImageView iv_img;
	private TextView tv_mode_hint;
	private ImageButton ib_previous, ib_next;
	private Button btn_apply;
	
	public RoomControlLightingFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}
	
	public RoomControlLightingFragment(VerticalChangedCallback callback, PageChangedCallback pageChangedCallback) {
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
		adapter.addViews(getLightingView(getActivity(), inflater));
		
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
	
	private void setTvAnimation(TextView textView) {
		
		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		textView.startAnimation(ani);
	}
	
	public List<View> getLightingView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(getLightingBehind(inflater));
		
		return mList;
	}
	
	private View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_lighting);
		tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("灯光控制");
		tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.VISIBLE);
		return view;
	}
	
	private View getLightingBehind(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_lighting_behind, null);
		iv_img = ((ImageView) view.findViewById(R.id.iv_img));
		iv_img.setImageResource(R.drawable.room_control_lighting_bright_mode);
		tv_mode_hint = (TextView) view.findViewById(R.id.tv_mode_hint);
		tv_mode_hint.setText("明亮模式");
		ib_previous = ((ImageButton) view.findViewById(R.id.ib_previous));
		ib_next = ((ImageButton) view.findViewById(R.id.ib_next));
		ib_previous.setOnClickListener(ImgClickListener);
		ib_next.setOnClickListener(ImgClickListener);
		
		return view;
	}
	
	private int curMode = 0;
	public static String[] mode_names = { "明亮模式", "电视模式", "阅读模式", "睡眠模式" };
	public static int[] mode_imgs = { R.drawable.room_control_lighting_bright_mode, R.drawable.room_control_lighting_tv_mode,
			R.drawable.room_control_lighting_reading_mode, R.drawable.room_control_lighting_sleep_mode };
	
	private OnClickListener ImgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ib_previous) {
				curMode = Math.abs((--curMode) % 4);
				tv_mode_hint.setText(mode_names[curMode]);
				iv_img.setImageResource(mode_imgs[curMode]);
			}
			else if (v.getId() == R.id.ib_next) {
				if (curMode == 3) return;
				curMode = Math.abs((++curMode) % 4);
				tv_mode_hint.setText(mode_names[curMode]);
				iv_img.setImageResource(mode_imgs[curMode]);
			}
		}
	};
}
