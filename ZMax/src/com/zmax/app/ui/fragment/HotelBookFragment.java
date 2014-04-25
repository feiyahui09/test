package com.zmax.app.ui.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.j256.ormlite.stmt.Where;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmax.app.R;
import com.zmax.app.adapter.HotelBookListAdapter;
import com.zmax.app.db.DBAccessor;
import com.zmax.app.manage.DataManage;
import com.zmax.app.model.Hotel;
import com.zmax.app.model.HotelList;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetHotelListTask;
import com.zmax.app.ui.HotelDetailActivity;
import com.zmax.app.ui.WebViewActivity;
import com.zmax.app.ui.base.BaseSlidingFragmentActivity.HotelBookVisivleCallback;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DateTimeUtils;
import com.zmax.app.utils.Utility;
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
		setRetainInstance(true);
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
		super.onActivityCreated(savedInstanceState);
		
		getHotelListTask = new GetHotelListTask(getActivity(), new GetHotelListTask.TaskCallBack() {
			
			@Override
			public void onCallBack(HotelList hotelList, HotelList upcomingHotelList) {
				if (getActivity() == null) return;
				if (hotelList != null && hotelList.status == 200) {
					initData(hotelList.hotels, upcomingHotelList.hotels);
					saveHotel(hotelList.hotels, false);
					saveHotel(upcomingHotelList.hotels, true);
					if (hotelList.hotels == null || hotelList.hotels.isEmpty()) Utility.toastNoMoreResult(getActivity());
				}
				else {
					initData(DataManage.getIndexHotellist4DB(false), DataManage.getIndexHotellist4DB(true));
					if (!NetWorkHelper.checkNetState(getActivity())) {
						Utility.toastNetworkFailed(getActivity());
					}
					else if (hotelList != null)
						Utility.toastFailedResult(getActivity(), hotelList.message);
					else
						Utility.toastFailedResult(getActivity());
				}
			}
		});
		getHotelListTask.execute(Constant.CUR_CITY, "1", "" + Constant.PER_NUM_GET_HOTELLIST);
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
	
	private void initData(final List<Hotel> hotelList, List<Hotel> upcomingHotelList) {
		if (hotelList == null || hotelList.isEmpty()) return;
		List<View> views = fromViews(hotelList, upcomingHotelList);
		adapter.addViews(views);
		initPagerIndicator(views, indicator);
		pager.setCurrentItem(mPosition);
		
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
	
	public List<View> fromViews(List<Hotel> hotelList, List<Hotel> upcomingHotelList) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		List<View> mList = new ArrayList<View>();
		
		for (int i = 0; i < hotelList.size(); i++) {
			final Hotel hotel = hotelList.get(i);
			View view = inflater.inflate(R.layout.hotel_book_list_item, null);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), WebViewActivity.class);
					intent.setAction(Constant.WAP.ACTION_HOTEL);
					intent.putExtra(Constant.WAP.HOTEL_ID_KEY, hotel.pms_hotel_id);
					startActivity(intent);
					
				}
			});
			((TextView) view.findViewById(R.id.tv_title)).setText("" + hotel.name);
			ImageLoader.getInstance().displayImage(hotel.poster, ((ImageView) view.findViewById(R.id.iv_img)));
			mList.add(view);
		}
		if (upcomingHotelList != null && !upcomingHotelList.isEmpty()) {
			mList.add(fromUpcomingViews(upcomingHotelList));
		}
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
		View hint_view = getActivity().getLayoutInflater().inflate(R.layout.hotel_book_upcoming_more_hint, null);
		listView.addFooterView(hint_view);
		listView.setAdapter(simpleAdapter);
		
		return view;
	}
}
