package com.zmax.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zmax.app.R;
import com.zmax.app.ui.RoomControlActivity;

public class PlayInZmaxLoginFragment extends Fragment {
	
	private View view;
	
	public PlayInZmaxLoginFragment() {
		this(R.color.white);
	}
	
	public PlayInZmaxLoginFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax_login, null);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}
