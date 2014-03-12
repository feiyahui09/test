package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zmax.app.R;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActvitesListFragment extends Fragment implements
		IXListViewListener {

	protected XListView listview;
	protected View view;
	private int mColorRes = -1;

	private Button btn_hotel_book;
	private Button btn_activities_list;
	private Button btn_more;

	public ActvitesListFragment() {
		this(R.color.white);
	}

	public ActvitesListFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mColorRes = savedInstanceState.getInt("mColorRes");
		int color = getResources().getColor(mColorRes);

		view = inflater.inflate(R.layout.activities_list, null);
		listview = (XListView) view.findViewById(R.id.list_view);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);

		btn_activities_list = (Button) view.findViewById(R.id.btn_activities);
		btn_hotel_book = (Button) view.findViewById(R.id.btn_hotel_book);

		btn_activities_list.setBackgroundResource(R.drawable.ic_launcher);
		btn_hotel_book.setBackgroundResource(R.drawable.xlistview_arrow);

		btn_hotel_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchFragment(new HotelBookFragment(R.color.red));
			}
		});

		btn_more = (Button) view.findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleMenu();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

}
