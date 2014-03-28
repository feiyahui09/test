package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmax.app.R;

public class DefaultFragment extends Fragment {
	
	private int mColorRes = -1;
	
	public DefaultFragment() {
		this(R.color.white);
	}
	
	public DefaultFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) mColorRes = savedInstanceState.getInt("mColorRes");
		int color = getResources().getColor(mColorRes);
		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		v.setBackgroundColor(color);
		TextView tv = new TextView(getActivity());
		tv.setText("正在定位中");
		v.addView(tv);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}
	
}
