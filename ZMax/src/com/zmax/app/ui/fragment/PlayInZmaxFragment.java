package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmax.app.R;

public class PlayInZmaxFragment extends Fragment {
	
	public interface PlayZmaxLogoutCallback {
		
		public void onLogoutViewCreate();
		
		public void onLogoutViewDestroy();
	}
	
	private View view;
	private PlayZmaxLogoutCallback callback;
	
	public PlayInZmaxFragment(PlayZmaxLogoutCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax, null);
		if (callback != null) callback.onLogoutViewCreate();
		return view;
	}
	
	@Override
	public void onDestroyView() {
		if (callback != null) callback.onLogoutViewDestroy();
		super.onDestroyView();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}
