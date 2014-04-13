package com.zmax.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.RoomControlActivity;

public class PlayInZmaxFragment extends Fragment implements OnClickListener {
	
	private View view;
	private SeekBar seekbar;
	private Button btn_act, btn_chat, btn_room;
	private TextView tv_start_day, tv_start_week_day, tv_start_month, tv_end_day, tv_end_week_day, tv_end_month;
	
	public PlayInZmaxFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax, null);
		
		// seekbar can't move
		seekbar = (SeekBar) view.findViewById(R.id.sb_date);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int originalProgress;
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				originalProgress = seekBar.getProgress();
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser == true) {
					seekBar.setProgress(originalProgress);
				}
			}
		});
		
		btn_act = (Button) view.findViewById(R.id.btn_act);
		btn_chat = (Button) view.findViewById(R.id.btn_chat);
		btn_room = (Button) view.findViewById(R.id.btn_room);
		btn_act.setOnClickListener(this);
		btn_chat.setOnClickListener(this);
		btn_room.setOnClickListener(this);
		
		tv_start_day = (TextView) view.findViewById(R.id.tv_start_day);
		tv_start_week_day = (TextView) view.findViewById(R.id.tv_start_week_day);
		tv_start_month = (TextView) view.findViewById(R.id.tv_start_month);
		tv_end_day = (TextView) view.findViewById(R.id.tv_end_day);
		tv_end_week_day = (TextView) view.findViewById(R.id.tv_end_week_day);
		tv_end_month = (TextView) view.findViewById(R.id.tv_end_month);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).showLogoutView();
	}
	
	@Override
	public void onDestroyView() {
		// if (callback != null) callback.onLogoutViewDestroy();
		((MainActivity) getActivity()).hideLogoutView();
		super.onDestroyView();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		
		switch (v.getId()) {
			case R.id.btn_act:
				intent.setClass(getActivity(), RoomControlActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_chat:
				intent.setClass(getActivity(), RoomControlActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_room:
				intent.setClass(getActivity(), RoomControlActivity.class);
				startActivity(intent);
				break;
			
			default:
				break;
		}
	}
}
