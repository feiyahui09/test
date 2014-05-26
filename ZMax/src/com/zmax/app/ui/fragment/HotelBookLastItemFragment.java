package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zmax.app.R;
import com.zmax.app.model.Hotel;
import com.zmax.app.utils.DateTimeUtils;

public class HotelBookLastItemFragment extends Fragment {
	List<Hotel> hotels;
	ImageView iv_img;
	
	public HotelBookLastItemFragment(List<Hotel> hotels) {
		this.hotels = hotels;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.hotel_book_list_upcoming, null);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
		
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		for (int i = 0; i < hotels.size(); i++) {
			Hotel hotel = hotels.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", DateTimeUtils.friendly_time(hotel.open_date));
			map.put("name", hotel.name);
			lists.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), lists, R.layout.hotel_book_upcoming_list_item, new String[] {
				"date", "name" }, new int[] { R.id.tv_date, R.id.tv_name });
		View hint_view = getActivity().getLayoutInflater().inflate(R.layout.hotel_book_upcoming_more_hint, null);
		listView.addFooterView(hint_view);
		listView.setAdapter(simpleAdapter);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
}
