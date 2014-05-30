package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.db.DBAccessor;
import com.zmax.app.manage.DataManage;
import com.zmax.app.model.Hotel;
import com.zmax.app.model.HotelList;
import com.zmax.app.model.Login;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetHotelListTask;
import com.zmax.app.task.LoginPlayZmaxTask;
import com.zmax.app.ui.fragment.MoreMenuFragment.TabSelectedListener;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;

public class PlayInZmaxLoginFragment extends Fragment {
	
	private View view;
	private Button btn_login;
	private Spinner sp_hotels;
	private LoginPlayZmaxTask loginPlayZmaxTask;
	private String selected_pms_hotel_id;
	private GetHotelListTask getHotelListTask;
	private ProgressDialog progressDialog;
	private Button btn_guide;
	private EditText et_room_number, et_namecard, et_password;
	
	public PlayInZmaxLoginFragment() {
		setRetainInstance(true);
		Log.i("@@");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax_login, null);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		et_room_number = (EditText) view.findViewById(R.id.et_room_number);
		et_namecard = (EditText) view.findViewById(R.id.et_namecard);
		et_password = (EditText) view.findViewById(R.id.et_password);
		
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utility.isETNull(et_room_number))
					Utility.toastResult(getActivity(), "房间号不能为空哦！");
				else if (Utility.isETNull(et_namecard))
					Utility.toastResult(getActivity(), "身份证号码不能为空哦！");
				else if (Utility.isETNull(et_password))
					Utility.toastResult(getActivity(), "密码不能为空哦！");
				else if (et_namecard.getText().toString().trim().length() != 4)
					Utility.toastResult(getActivity(), "请填入身份证后四位！");
				else
					goPlayZmax( et_room_number.getText().toString().trim(), et_namecard.getText().toString().trim(),
							et_password.getText().toString().trim());
			}
		});
		sp_hotels = (Spinner) view.findViewById(R.id.sp_hotels);
		Log.i("@@");
		return view;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		// ((MainActivity) getActivity()).hideLogoutView(false);
		Log.i("@@");
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i("@@");
		sp_hotels.setVisibility(View.GONE);
//		final List<Hotel> hotels = getHotels();
//		if (hotels != null && !hotels.isEmpty()) {
//			sp_hotels.setEnabled(true);
//			fromHotelsSpinner(hotels);
//		}
//		else {
//			sp_hotels.setEnabled(false);
//			getHotelListTask = new GetHotelListTask(getActivity(), new GetHotelListTask.TaskCallBack() {
//				@Override
//				public void onCallBack(HotelList hotelList, HotelList upcomingHotelList) {
//					if (getActivity() == null) return;
//					if (hotelList != null && hotelList.status == 200) {
//						fromHotelsSpinner(hotelList.hotels);
//						saveHotel(hotelList.hotels, false);
//						if (hotelList.hotels == null || hotelList.hotels.isEmpty()) {
//							Utility.toastResult(getActivity(), "没有已开业酒店可选！");
//							sp_hotels.setEnabled(false);
//						}
//						
//					}
//					else {
//						if (!NetWorkHelper.checkNetState(getActivity())) {
//							Utility.toastNetworkFailed(getActivity());
//						}
//						else if (hotelList != null)
//							Utility.toastResult(getActivity(), hotelList.message);
//						else
//							Utility.toastFailedResult(getActivity());
//					}
//				}
//			});
//			getHotelListTask.execute(Constant.CUR_CITY, "1", "" + Constant.PER_NUM_GET_HOTELLIST);
//		}
	}
	
	private void goPlayZmax( String room_num, String id_number, String password) {
		if (!NetWorkHelper.checkNetState(getActivity())) {
			Utility.toastNetworkFailed(getActivity());
			return;
		}
		loginPlayZmaxTask = new LoginPlayZmaxTask(getActivity(), new LoginPlayZmaxTask.TaskCallBack() {
			@Override
			public void onCallBack(Login loginResult) {
				if (getActivity() == null) return;
				
				if (loginResult != null && loginResult.status == 200) {
					Constant.saveLogin(loginResult);
					
					((TabSelectedListener) getActivity()).handleSeleceted(R.id.ll_menu_playzmax + 100,true);
				}
				else {
					if (!NetWorkHelper.checkNetState(getActivity())) {
						Utility.toastNetworkFailed(getActivity());
					}
					else if (loginResult != null) {
                        Utility.toastResult(getActivity(), loginResult.message);
                    }else
						Utility.toastFailedResult(getActivity());
				}
			}
		});
		loginPlayZmaxTask.execute(  room_num, id_number, password);
		
	}
	
	private void fromHotelsSpinner(final List<Hotel> hotels) {
		sp_hotels.setEnabled(true);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.playzmax_login_spinner_item,
				getHotelNames(hotels));
		adapter.setDropDownViewResource(R.layout.playzmax_login_dropdown_spinner_item);
		sp_hotels.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) ((TextView) view).setText("" + adapter.getItem(position));
				selected_pms_hotel_id = hotels.get(position).pms_hotel_id;
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((TextView) view).setText("选择入住酒店");
			}
		});
		sp_hotels.setAdapter(adapter);
		sp_hotels.setSelection(0);
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
	
	private void saveHotel(final List<Hotel> hotelList, final boolean isUpcoming) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				List<Hotel> tmpLists = hotelList;
				for (Hotel hotel : tmpLists) {
					hotel.isUpcoming = isUpcoming;
				}
				DataManage.saveIndexHotellist2DB(tmpLists, isUpcoming);
			}
		}).start();
	}
}
