package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;

public class WelcomeFragment extends Fragment {
	private int drawbleID;
	private LinearLayout ll_bg;
	
	public WelcomeFragment(int drawbleID) {
		super();
		this.drawbleID = drawbleID;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.welcome_pager, null);
		ll_bg = (LinearLayout) view.findViewById(R.id.ll_bg);
		if (drawbleID > 0)
			ll_bg.setBackgroundResource(drawbleID);
		else
			ll_bg.setBackgroundResource(R.drawable.ic_launcher);
		
		ll_bg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return false;
			}
		});
		return view;
	}
	
}
