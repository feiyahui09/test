package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.db.DBAccessor;
import com.zmax.app.model.Hotel;
import com.zmax.app.model.Login;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.LoginPlayZmaxTask;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.utils.Utility;

public class PlayInZmaxLoginFragment extends Fragment {
	
	private View view;
	private Button btn_login;
	private Spinner sp_hotels;
	private LoginPlayZmaxTask loginPlayZmaxTask;
	private String selected_pms_hotel_id;
	
	public PlayInZmaxLoginFragment() {
		this(R.color.white);
	}
	
	public PlayInZmaxLoginFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax_login, null);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goPlayZmax();
			}
		});
		
		sp_hotels = (Spinner) view.findViewById(R.id.sp_hotels);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		fromHotelsSpinner(sp_hotels);
	}
	
	private void goPlayZmax() {
		if (!NetWorkHelper.checkNetState(getActivity())) {
			Utility.toastNetworkFailed(getActivity());
			return;
		}
		
		loginPlayZmaxTask = new LoginPlayZmaxTask(getActivity(), new LoginPlayZmaxTask.TaskCallBack() {
			@Override
			public void onCallBack(Login loginResult) {
				if (getActivity() == null) return;
				if (loginResult != null && loginResult.status == 200) {
					((MainActivity) getActivity()).switchContent(new PlayInZmaxFragment(loginResult));
				}
				else
					Utility.toastFailedResult(getActivity());
			}
		});
		loginPlayZmaxTask.execute();
		
	}
	
	private void fromHotelsSpinner(Spinner spinner) {
		final List<Hotel> hotels = getHotels();
		if (hotels == null || hotels.isEmpty()) return;
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.playzmax_login_spinner_item,
				getHotelNames(hotels));
		adapter.setDropDownViewResource(R.layout.playzmax_login_dropdown_spinner_item);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) ((TextView) view).setText("" + adapter.getItem(position));
				selected_pms_hotel_id = hotels.get(position).name;
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((TextView) view).setText("选择入住酒店");
			}
		});
		spinner.setAdapter(adapter);
	}
	
	private static final List<String> mStrings = Arrays.asList("选择入住酒店", "武汉光谷店", "广州珠江新城店", "北京王府井店");
	
	private List<Hotel> getHotels() {
		List<Hotel> hotels = null;
		Map<String, Object> fieldValues = new HashMap<String, Object>();
		fieldValues.put("isUpcoming", false);
		hotels = DBAccessor.queryAll(Hotel.class, fieldValues);
		return hotels;
	}
	
	private List<String> getHotelNames(List<Hotel> hotels) {
		List<String> strings = new ArrayList<String>();
		for (Hotel hotel : hotels) {
			strings.add(hotel.name);
		}
		return strings;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}
