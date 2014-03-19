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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;

public class NetErrorFragment extends Fragment {
	private RadioGroup rg_top_title;
	private Button btn_more;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.net_error, null);
		/*
		 * 头部tab切换 活动和预订酒店
		 */
		rg_top_title = (RadioGroup) view.findViewById(R.id.rg_top_title);

		rg_top_title.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_hotel_book)
					switchFragment(new HotelBookFragment(R.color.red));
				else if (checkedId == R.id.btn_activities)
					switchFragment(new ActListFragment(R.color.red));
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

	private void switchFragment(Fragment fragment) {

		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
