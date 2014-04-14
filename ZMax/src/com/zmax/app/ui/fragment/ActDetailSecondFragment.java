package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDescptionListAdapter;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.model.ActDetailContent;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.ActDetailActivity.RefreshDataCallBack;
import com.zmax.app.utils.Constant;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActDetailSecondFragment extends Fragment implements IXListViewListener, OnItemClickListener, RefreshDataCallBack {
	
	protected XListView listview;
	protected View view;
	
	private ActDescptionListAdapter adapter;
	
	public ActDetailSecondFragment() {
		this(R.color.white);
	}
	
	public ActDetailSecondFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.act_detail_second, null);
		listview = (XListView) view.findViewById(R.id.list_view);
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		
		adapter = new ActDescptionListAdapter(getActivity());
		
//		listview.setOnItemClickListener(this);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onRefresh() {
		
	}
	
	@Override
	public void onLoadMore() {
		// adapter.appendToList(Constant.getFalseData(false));
		onLoad();
	}
	
	protected void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
	
	@Override
	public void onDataRefresh(ActDetailContent detailContent) {
		if (detailContent == null) return;
		adapter.appendToList(detailContent.description_items);
		listview.setAdapter(adapter);
	}
	
}
