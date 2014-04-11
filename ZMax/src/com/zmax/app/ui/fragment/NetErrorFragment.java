package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;

public class NetErrorFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.net_error, null);
		
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getActivity() != null && isAdded()) {
					((MainActivity) getActivity()).switchContent(new ActListFragment( ));
				}
				return false;
			}
		});
		return view;
	}
	
	private void switchFragment(Fragment fragment) {
		
		if (getActivity() == null) return;
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}
	
}
