package com.zmax.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.ui.WelcomeActivity;

public class SettingFragment extends Fragment implements OnClickListener {
	
	private Button btn_feedback, btn_check_update, btn_welcome, btn_user_regulation, btn_play_zmax, btn_member, btn_about;
	
	public SettingFragment() {
		this(R.color.white);
	}
	
	public SettingFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, null);
		btn_feedback = (Button) view.findViewById(R.id.btn_feedback);
		btn_feedback.setOnClickListener(this);
		btn_check_update = (Button) view.findViewById(R.id.btn_check_update);
		btn_check_update.setOnClickListener(this);
		btn_welcome = (Button) view.findViewById(R.id.btn_welcome);
		btn_welcome.setOnClickListener(this);
		btn_user_regulation = (Button) view.findViewById(R.id.btn_user_regulation);
		btn_user_regulation.setOnClickListener(this);
		btn_play_zmax = (Button) view.findViewById(R.id.btn_play_zmax);
		btn_play_zmax.setOnClickListener(this);
		btn_member = (Button) view.findViewById(R.id.btn_member);
		btn_member.setOnClickListener(this);
		btn_about = (Button) view.findViewById(R.id.btn_about);
		btn_about.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		
			case R.id.btn_feedback:
				
				break;
			case R.id.btn_check_update:
				
				break;
			case R.id.btn_welcome:
				intent.setClass(getActivity(), WelcomeActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_user_regulation:
				
				break;
			case R.id.btn_play_zmax:
				
				break;
			case R.id.btn_member:
				
				break;
			case R.id.btn_about:
				
				break;
			
			default:
				break;
		}
		
	}
}
