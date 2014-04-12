package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailHotelListAdapter;
import com.zmax.app.utils.Constant;

public class ActDetailThirdFragment extends Fragment {
	
	private int mColorRes = -1;
	private ListView listView;
	private Button btn_more;
	private ActDetailHotelListAdapter adapter;
	private View v;
	
	public ActDetailThirdFragment() {
		this(R.color.white);

	}
	
	public ActDetailThirdFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) mColorRes = savedInstanceState.getInt("mColorRes");
		v = inflater.inflate(R.layout.act_detail_third, null);
		btn_more = (Button) v.findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_more.setVisibility(View.GONE);
				adapter.appendToList(Constant.getFalseData(12));
				setListViewHeightBasedOnChildren(listView, true);
			}
		});
		listView = (ListView) v.findViewById(R.id.list_view);
		adapter = new ActDetailHotelListAdapter(getActivity());
		listView.setAdapter(adapter);
		
		adapter.appendToList(Constant.getFalseData(2));
		setListViewHeightBasedOnChildren(listView, false);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
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
