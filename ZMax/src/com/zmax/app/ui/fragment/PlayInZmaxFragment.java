package com.zmax.app.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.model.Login;
import com.zmax.app.ui.ActsInHotelActivity;
import com.zmax.app.ui.ChatRoomActivity;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.RoomControlActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DateTimeUtils;
import com.zmax.app.utils.Log;

public class PlayInZmaxFragment extends Fragment implements OnClickListener {
	
	private View view;
	private SeekBar seekbar;
	private Button btn_act, btn_chat, btn_room;
	private TextView tv_start_day, tv_start_week_day, tv_start_month, tv_end_day, tv_end_week_day, tv_end_month;
	private TextView tv_nick_name, tv_hotel, tv_room_num;
	private Login login;
	
	public PlayInZmaxFragment(Login login) {
		this.login = login;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax, null);
		btn_act = (Button) view.findViewById(R.id.btn_act);
		btn_chat = (Button) view.findViewById(R.id.btn_chat);
		btn_room = (Button) view.findViewById(R.id.btn_room);
		btn_act.setOnClickListener(this);
		btn_chat.setOnClickListener(this);
		btn_room.setOnClickListener(this);
		
		tv_nick_name = (TextView) view.findViewById(R.id.tv_nick_name);
		tv_hotel = (TextView) view.findViewById(R.id.tv_hotel);
		tv_room_num = (TextView) view.findViewById(R.id.tv_room_num);
		
		tv_start_day = (TextView) view.findViewById(R.id.tv_start_day);
		tv_start_week_day = (TextView) view.findViewById(R.id.tv_start_week_day);
		tv_start_month = (TextView) view.findViewById(R.id.tv_start_month);
		tv_end_day = (TextView) view.findViewById(R.id.tv_end_day);
		tv_end_week_day = (TextView) view.findViewById(R.id.tv_end_week_day);
		tv_end_month = (TextView) view.findViewById(R.id.tv_end_month);
		
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
		
		tv_nick_name.setText(login.nick_name);
		tv_hotel.setText("ZMAX-" + login.hotel_location + login.hotel_name);
		tv_room_num.setText(login.room_num + "房");
		
		String startStr = login.start_time, endStr = login.end_time;
		 //String startStr = "20140416164431", endStr ="20140417164431";
		
		tv_start_day.setText(startStr.substring(6, 8));
		tv_start_month.setText(startStr.substring(4, 6));
		tv_start_week_day.setText(DateTimeUtils.getWeekOfDate(startStr));
		tv_end_day.setText(endStr.substring(6, 8));
		tv_end_month.setText(endStr.substring(4, 6));
		tv_end_week_day.setText(DateTimeUtils.getWeekOfDate(endStr));
		
		String curStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		Log.d("startStr  : " + startStr);
		Log.d("endStr   : " + endStr);
		Log.d("curStr    : " + curStr);
		
		int progress = 0;
		if (curStr.compareTo(startStr) <= 0)
			progress = 0;
		else if (curStr.compareTo(endStr) >= 0)
			progress = 100;
		else {
			long totallDuration = Long.valueOf(endStr) - Long.valueOf(startStr);
			long curDuration = Long.valueOf(curStr) - Long.valueOf(startStr);
			progress = (int) (curDuration * 1.0 / totallDuration * 100);
			
			Log.d("curDuration    : " + curDuration);
			Log.d("totallDuration : " + totallDuration);
			Log.d("progress         : " + progress);
		}
		seekbar.setProgress(progress);
		
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
				intent.setClass(getActivity(), ActsInHotelActivity.class);
				intent.putExtra(Constant.Acts.HOTEL_ID_KEY, login.pms_hotel_id);
				startActivity(intent);
				break;
			case R.id.btn_chat:
				intent.setClass(getActivity(), ChatRoomActivity.class);
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
