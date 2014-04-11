package com.zmax.app.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zmax.app.R;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.model.Act;
import com.zmax.app.model.ActList;
import com.zmax.app.task.GetActListTask;
import com.zmax.app.ui.ActDetailFlashActivity;
import com.zmax.app.utils.DateTimeUtils;
import com.zmax.app.utils.Log;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActListFragment extends Fragment implements IXListViewListener, OnItemClickListener {
	
	protected XListView listview;
	protected View view;
	private int mColorRes = -1;
	
	private ActListAdapter adapter;
	private GetActListTask getActListTask;
	private int curPage = 1;
	
	public ActListFragment() {
		this(R.color.white);
	}
	
	public ActListFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) mColorRes = savedInstanceState.getInt("mColorRes");
		int color = getResources().getColor(mColorRes);
		
		view = inflater.inflate(R.layout.act_list, null);
		listview = (XListView) view.findViewById(R.id.list_view);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		
		adapter = new ActListAdapter(getActivity());
		// adapter.appendToList(Constant.getFalseData(false));
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setXListViewListener(this);
		curPage = 1;
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		onRefresh();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
	}
	
	@Override
	public void onRefresh() {
		curPage = 1;
		getActList(curPage);
		onLoad();
	}
	
	@Override
	public void onLoadMore() {
		getActList(curPage);
		
	}
	
	protected void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime(DateTimeUtils.formatTime(System.currentTimeMillis()));
	}
	
	private void getActList(int page) {
		
		getActListTask = new GetActListTask(getActivity(), new GetActListTask.TaskCallBack() {
			
			@Override
			public void onCallBack(ActList result) {
				if (getActivity() == null) return;
				if (result != null && result.status == 200) {
					List<Act> actList = result.events;
					if (actList != null && !actList.isEmpty()) {
						if (curPage == 1) adapter.Clear();
						adapter.appendToList(actList);
						curPage++;
					}
				}
				onLoad();
			}
		});
		getActListTask.execute("wuhan", String.valueOf(page), "2");
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActDetailFlashActivity.class);
		getActivity().startActivity(intent);
		
	}
	
}
