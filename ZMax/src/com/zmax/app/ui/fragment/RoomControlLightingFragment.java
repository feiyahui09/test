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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.manage.RoomControlManage;
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
	public RoomControlLightingFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}

	public RoomControlLightingFragment(VerticalChangedCallback callback,
			PageChangedCallback pageChangedCallback) {
		this.callback = callback;
		this.pageChangedCallback = pageChangedCallback;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
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
					callback.onCallBack(vpager.getCurrentItem() == 0 ? true
							: false);
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

	public List<View> getLightingView(final FragmentActivity fragmentActivity,
			LayoutInflater inflater) {

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
		final View view = inflater.inflate(
				R.layout.room_control_lighting_behind, null);
		((RadioGroup) view.findViewById(R.id.rg_lighting_model))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_lighting:
							tv_mode_detail.setText("明亮模式");
							break;
						case R.id.rb_tv:
							tv_mode_detail.setText("电视模式");
							break;
						case R.id.rb_reading:
							tv_mode_detail.setText("阅读模式");
							break;
						case R.id.rb_sleep:
							tv_mode_detail.setText("睡眠模式");
							break;
						case R.id.rb_self:
							tv_mode_detail.setText("自定义模式");
							break;
						default:
							break;
						}
					}
				});
		((RadioButton) view.findViewById(R.id.rb_lighting)).setChecked(true);
		return view;
	}

}
