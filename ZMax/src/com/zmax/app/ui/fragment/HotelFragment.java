package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.zmax.app.R;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.adapter.HotelBookListAdapter;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.VerticalViewPager;

public class HotelFragment extends Fragment {

	private int mColorRes = -1;

	protected View view;
	private Button btn_activities_list;
	private Button btn_hotel_book;
	private Button btn_more;

	private HotelBookListAdapter adapter;

	private VerticalViewPager pager;

	public HotelFragment() {
		this(R.color.white);
	}

	public HotelFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mColorRes = savedInstanceState.getInt("mColorRes");
		int color = getResources().getColor(mColorRes);

		view = inflater.inflate(R.layout.act_list_item, null);
		((ImageView) view.findViewById(R.id.iv_img))
				.setImageResource(R.drawable.ic_launcher);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}

}
