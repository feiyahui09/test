package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.adapter.HotelBookListAdapter;
import com.zmax.app.widget.CounterPullDoorView;
import com.zmax.app.widget.PullDoorView;
import com.zmax.app.widget.CounterPullDoorView.CounterPullCallBack;
import com.zmax.app.widget.PullDoorView.PullCallBack;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlAirConditionFragment extends Fragment implements
		PullCallBack {

	private int mColorRes = -1;

	protected View view;
	private Button btn_more;
	private CounterPullDoorView pdv_bottom;
	private PullDoorView pdv_top;

	private HotelBookListAdapter adapter;

	private VerticalViewPager pager;

	public RoomControlAirConditionFragment() {
		this(R.color.white);
	}

	public RoomControlAirConditionFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mColorRes = savedInstanceState.getInt("mColorRes");
		view = inflater.inflate(R.layout.room_control_fragment, null);
//		pdv_bottom = ((CounterPullDoorView) view.findViewById(R.id.pdv_bottom));
		pdv_top = ((PullDoorView) view.findViewById(R.id.pdv_top));
//		pdv_top.setCallBack(this);
//		pdv_bottom.setCallBack(this);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}

	@Override
	public void onClosed(PullDoorView pdv) {
		pdv_top.setVisibility(View.GONE);
		pdv_bottom.setVisibility(View.VISIBLE);
	}

	 

}
