package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmax.app.R;

public class ActDetailFirstFragment extends Fragment {
	
	private int mColorRes = -1;
	
	public ActDetailFirstFragment() {
		this(R.color.white);
	}
	
	public ActDetailFirstFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) mColorRes = savedInstanceState.getInt("mColorRes");
		int color = getResources().getColor(mColorRes);
		View v = inflater.inflate(R.layout.act_detail_first, null);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}
	
}
