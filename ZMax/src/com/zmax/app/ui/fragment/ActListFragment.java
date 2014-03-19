package com.zmax.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zmax.app.R;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.ui.ActDetailFlashActivity;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActListFragment extends Fragment implements IXListViewListener,
		OnItemClickListener {

	protected XListView listview;
	protected View view;
	private int mColorRes = -1;

	private Button btn_more;
	private RadioGroup rg_top_title;

	private ActListAdapter adapter;

	public ActListFragment() {
		this(R.color.white);
	}

	public ActListFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mColorRes = savedInstanceState.getInt("mColorRes");
		int color = getResources().getColor(mColorRes);

		view = inflater.inflate(R.layout.act_list, null);
		listview = (XListView) view.findViewById(R.id.list_view);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);

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
				toggleMenu();
			}
		});

		adapter = new ActListAdapter(getActivity());
		adapter.appendToList(Constant.getFalseData(false));
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);

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

	private void toggleMenu() {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.toggle();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		adapter.appendToList(Constant.getFalseData(false));
		onLoad();
	}

	protected void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActDetailFlashActivity.class);
		// getActivity().startActivity(intent);

	}

}
