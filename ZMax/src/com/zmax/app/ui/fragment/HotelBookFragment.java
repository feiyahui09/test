package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmax.app.R;
import com.zmax.app.adapter.HotelBookListAdapter;
import com.zmax.app.model.Hotel;
import com.zmax.app.model.HotelList;
import com.zmax.app.task.GetHotelListTask;
import com.zmax.app.task.GetActDetailTask;
import com.zmax.app.ui.HotelDetailActivity;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity.HotelBookVisivleCallback;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DateTimeUtils;
import com.zmax.app.widget.VerticalViewPager;
import com.zmax.app.widget.VerticalViewPager.OnPageChangeListener;

public class HotelBookFragment extends Fragment implements OnPageChangeListener, OnItemClickListener, HotelBookVisivleCallback {
	
	private int mPosition = 0;
	
	protected View view;
	
	private HotelBookListAdapter adapter;
	
	private VerticalViewPager pager;
	private LinearLayout indicator;
	
	private GetHotelListTask getHotelListTask;
	
	public HotelBookFragment() {
		this(R.color.white);
	}
	
	public HotelBookFragment(int mosition) {
		mPosition = 0;
		// setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) mPosition = savedInstanceState.getInt("mPosition");
		
		view = inflater.inflate(R.layout.hotel_book_list, null);
		
		pager = (VerticalViewPager) view.findViewById(R.id.vvp_hotel);
		indicator = (LinearLayout) view.findViewById(R.id.indicator);
		adapter = new HotelBookListAdapter(getActivity(), null);
		pager.setAdapter(adapter);
		
		pager.setOnPageChangeListener(this);
		// view.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getHotelListTask = new GetHotelListTask(getActivity(), new GetHotelListTask.TaskCallBack() {
			
			@Override
			public void onCallBack(HotelList hotelList, HotelList upcomingHotelList) {
				if (getActivity() == null) return;
				if (hotelList != null && hotelList.status == 200 && hotelList.hotels != null && !hotelList.hotels.isEmpty()) {
					initData(hotelList.hotels, upcomingHotelList);
				}
			}
		});
		getHotelListTask.execute(Constant.CUR_CITY, "1", "10");
	}
	
	private void initPagerIndicator(List<View> falseDataView, LinearLayout indicator) {
		if (falseDataView == null || falseDataView.size() <= 0) return;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		for (int i = 0; i < falseDataView.size(); i++) {
			View view = inflater.inflate(R.layout.vpager_indicator_item, null);
			((ImageView) view.findViewById(R.id.iv_img)).setImageResource(R.drawable.hotel_list_indicator_normal);
			
			indicator.addView(view);
			
		}
		switchInidcator(0);
	}
	
	private void switchInidcator(int position) {
		mPosition = position;
		ImageView imageView;
		for (int i = 0; i < indicator.getChildCount(); i++) {
			imageView = (ImageView) indicator.getChildAt(i).findViewById(R.id.iv_img);
			imageView.setImageResource(R.drawable.hotel_list_indicator_normal);
		}
		imageView = (ImageView) indicator.getChildAt(position).findViewById(R.id.iv_img);
		imageView.setImageResource(R.drawable.hotel_list_indicator_activited);
		
	}
	
	private void toggleMenu() {
		
		if (getActivity() == null) return;
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.toggle();
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPosition", mPosition);
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}
	
	@Override
	public void onPageSelected(int position) {
		switchInidcator(position);
		
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
	
	@Override
	public void onCallBack() {
		
		// getHotelListTask = new GetHotelListTask(getActivity(), new
		// GetHotelListTask.TaskCallBack() {
		//
		// @Override
		// public void onCallBack(HotelList hotelList, HotelList
		// upcomingHotelList) {
		// if (getActivity() == null) return;
		// if (hotelList != null && hotelList.status == 200 && hotelList.hotels
		// != null && !hotelList.hotels.isEmpty()) {
		// initData(hotelList.hotels, upcomingHotelList);
		// }
		// }
		// });
		// getHotelListTask.execute("武汉", "1", "10");
		
	}
	
	private void initData(List<Hotel> hotelList, HotelList upcomingHotelList) {
		
		List<View> views = fromViews(hotelList, upcomingHotelList);
		adapter.addViews(views);
		initPagerIndicator(views, indicator);
		pager.setCurrentItem(mPosition);
		
	}
	
	public List<View> fromViews(List<Hotel> hotelList, HotelList upcomingHotelList) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		List<View> mList = new ArrayList<View>();
		
		for (int i = 0; i < hotelList.size(); i++) {
			Hotel hotel = hotelList.get(i);
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().startActivity(new Intent(getActivity(), HotelDetailActivity.class));
				}
			});
			((TextView) view.findViewById(R.id.tv_title)).setText(hotel.name);
			ImageLoader.getInstance().displayImage(hotel.poster, ((ImageView) view.findViewById(R.id.iv_img)));
			mList.add(view);
		}
		if (upcomingHotelList != null && upcomingHotelList.hotels != null && !upcomingHotelList.hotels.isEmpty())
			mList.add(fromUpcomingViews(upcomingHotelList.hotels));
		
		return mList;
	}
	
	public View fromUpcomingViews(List<Hotel> hotelList) {
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		for (int i = 0; i < hotelList.size(); i++) {
			Hotel hotel = hotelList.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", DateTimeUtils.friendly_time(hotel.open_date));
			map.put("name", hotel.name);
			lists.add(map);
		}
		View view = getActivity().getLayoutInflater().inflate(R.layout.hotel_book_list_upcoming, null);
		ListView listView = (ListView) view.findViewById(R.id.list_view);
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), lists, R.layout.hotel_book_upcoming_list_item, new String[] {
				"date", "name" }, new int[] { R.id.tv_date, R.id.tv_name });
		listView.setAdapter(simpleAdapter);
		return view;
		
	}
	
}
