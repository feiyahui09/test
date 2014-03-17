package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;

public class NetErrorFragment extends Fragment {
	private Button btn_hotel_book;
	private Button btn_activities_list;
	private Button btn_more;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.net_error, null);
		/*
		 * 头部tab切换 活动和预订酒店
		 */
		btn_activities_list = (Button) view.findViewById(R.id.btn_activities);
		btn_hotel_book = (Button) view.findViewById(R.id.btn_hotel_book);

		btn_activities_list.setBackgroundResource(R.drawable.ic_launcher);
		btn_hotel_book.setBackgroundResource(R.drawable.xlistview_arrow);

		btn_hotel_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getActivity() != null && isAdded()) {
					((MainActivity) getActivity())
							.switchContent(new ActListFragment(R.color.red));
				}
			}
		});

		btn_more = (Button) view.findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivity() != null && isAdded())
					((MainActivity) getActivity()).toggle();
			}
		});

		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getActivity() != null && isAdded()) {
					((MainActivity) getActivity())
							.switchContent(new ActListFragment(R.color.red));
				}
				return false;
			}
		});
		return view;
	}

}
