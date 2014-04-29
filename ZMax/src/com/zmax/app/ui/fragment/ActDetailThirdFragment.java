package com.zmax.app.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailHotelListAdapter;
import com.zmax.app.model.ActDetailContent;
import com.zmax.app.model.ActDetailHotel;
import com.zmax.app.ui.ActDetailActivity.RefreshDataCallBack;
import com.zmax.app.utils.Constant;

public class ActDetailThirdFragment extends Fragment implements RefreshDataCallBack {
	
	private ListView listView;
	private Button btn_more;
	private ActDetailHotelListAdapter adapter;
	private View v;
	private TextView tv_start_day, tv_start_year, tv_end_day, tv_end_year, tv_begin_time;
	private ImageView iv_img;
	private List<ActDetailHotel> hotels;
	
	public ActDetailThirdFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.act_detail_third, null);
		tv_start_day = (TextView) v.findViewById(R.id.tv_start_day);
		tv_start_year = (TextView) v.findViewById(R.id.tv_start_year);
		tv_end_day = (TextView) v.findViewById(R.id.tv_end_day);
		tv_end_year = (TextView) v.findViewById(R.id.tv_end_year);
		tv_begin_time = (TextView) v.findViewById(R.id.tv_begin_time);
		iv_img = (ImageView) v.findViewById(R.id.iv_img);
		
		btn_more = (Button) v.findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadMoreHotels(hotels);
			}
		});
		listView = (ListView) v.findViewById(R.id.list_view);
		adapter = new ActDetailHotelListAdapter(getActivity());
		listView.setAdapter(adapter);
		// adapter.appendToList(Constant.getFalseData(2));
		// setListViewHeightBasedOnChildren(listView, false);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private boolean isInitialized = false;
	
	@Override
	public void onDataRefresh(ActDetailContent detailContent) {
		if (detailContent == null || isInitialized) return;
		isInitialized = true;
		tv_begin_time.setText(detailContent.event_begin);
		// ImageLoader.getInstance().displayImage("http://www.120ask.com/static/upload/clinic/index/big/201212/big_201212241738148789.jpg",
		// iv_img);
		// 活动酒店列表显示
		hotels = detailContent.hotels;
		initHotels(hotels);
		// 日期显示
		String start = detailContent.start_date;
		String end = detailContent.end_date;
		if (start.length() != 8 || end.length() != 8) return;
		tv_start_day.setText(start.substring(4, 6) + " - " + start.substring(6, 8));
		tv_start_year.setText(start.substring(0, 4));
		tv_end_day.setText(end.substring(4, 6) + " - " + end.substring(6, 8));
		tv_end_year.setText(end.substring(0, 4));
		
	}
	
	private void initHotels(List<ActDetailHotel> detailHotels) {
		
		if (detailHotels == null || detailHotels.isEmpty()) {
			btn_more.setVisibility(View.GONE);
			return;
		}
		adapter.clear();
		if (detailHotels.size() > 2) {
			btn_more.setVisibility(View.VISIBLE);
			btn_more.setText(String.format("查看其余%d家分店", detailHotels.size() - 2));
			adapter.appendToList(detailHotels.subList(0, 2));
			setListViewHeightBasedOnChildren(listView, false);
		}
		else {
			adapter.appendToList(detailHotels);
			setListViewHeightBasedOnChildren(listView, false);
			
		}
	}
	
	private void loadMoreHotels(List<ActDetailHotel> detailHotels) {
		btn_more.setVisibility(View.GONE);
		adapter.clear();
		adapter.appendToList(detailHotels);
		setListViewHeightBasedOnChildren(listView, true);
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView, boolean isExtra) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			// 再加一次，适配
			if (isExtra && i == 0) totalHeight += listItem.getMeasuredHeight();
		}
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
