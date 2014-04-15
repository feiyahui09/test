package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zmax.app.R;
import com.zmax.app.ui.WelcomeActivity.StartAppCallBack;

public class WelcomeFragment extends Fragment {
	private int drawbleID;
	private RelativeLayout rl_bg, rl_start;
	private StartAppCallBack startAppCallBack;
	
	public WelcomeFragment(int drawbleID) {
		super();
		this.drawbleID = drawbleID;
	}
	
	public WelcomeFragment(int drawbleID, StartAppCallBack callBack) {
		super();
		this.drawbleID = drawbleID;
		this.startAppCallBack = callBack;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.welcome_pager, null);
		rl_bg = (RelativeLayout) view.findViewById(R.id.rl_bg);
		rl_start = (RelativeLayout) view.findViewById(R.id.rl_start);
		
		if (drawbleID > 0)
			rl_bg.setBackgroundResource(drawbleID);
		else
			rl_bg.setBackgroundResource(R.drawable.ic_launcher);
		
		rl_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (startAppCallBack != null) startAppCallBack.onStartAPP();
			}
		});
		return view;
	}
	
}
