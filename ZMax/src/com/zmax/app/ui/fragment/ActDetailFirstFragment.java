package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.model.ActDetail;
import com.zmax.app.ui.ActDetailActivity.RefreshDataCallBack;

public class ActDetailFirstFragment extends Fragment {
	
	private TextView tv_city, tv_date;
	private String city, date;
	
	public ActDetailFirstFragment() {
		
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.act_detail_first, null);
		tv_city = (TextView) v.findViewById(R.id.tv_city);
		tv_date = (TextView) v.findViewById(R.id.tv_date);
		
		if (getArguments().isEmpty()) {
			city = savedInstanceState.getString("city");
			date = savedInstanceState.getString("date");
		}
		else {
			city = getArguments().getString("city");
			date = getArguments().getString("date");
			
		}
		
		tv_city.setText("" + city);
		tv_date.setText("" + date);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("date", date);
		outState.putString("city", city);
		
	}
	
}
