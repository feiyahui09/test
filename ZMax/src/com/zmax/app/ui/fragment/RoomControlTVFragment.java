package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlTVFragment extends Fragment {
	public interface VerticalChangedCallback {
		public void onCallBack(boolean isCurAbove);
	}

	protected View view;

	private VerticalViewPager vpager;

	private RoomControlAdapter adapter;

	private VerticalChangedCallback callback;

	public RoomControlTVFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_fragment, null);
		vpager = (VerticalViewPager) view.findViewById(R.id.vpager);
		adapter = new RoomControlAdapter(getActivity(),
				Constant.getRoomControllFalseDataView(getActivity(), inflater));
		vpager.setAdapter(adapter);
		vpager.setCurrentItem(0);

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

	private View getAboveView(LayoutInflater inflater) {
		TextView tv_hint_above;
		View view = inflater.inflate(R.layout.room_control_tv_above, null);
		tv_hint_above = (TextView) view.findViewById(R.id.tv_hint_above);
		setTvAnimation(tv_hint_above);
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
