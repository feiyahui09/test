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
import com.zmax.app.manage.DataManage;
import com.zmax.app.model.Act;
import com.zmax.app.model.ActList;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActListTask;
import com.zmax.app.ui.ActDetailActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

public class ActListFragment extends Fragment implements IXListViewListener, OnItemClickListener {
	
	protected XListView listview;
	protected View view;
	
	private ActListAdapter adapter;
	private GetActListTask getActListTask;
	private int curPage = 1;
	
	public ActListFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// if (savedInstanceState != null) mColorRes =
		// savedInstanceState.getInt("mColorRes");
		// int color = getResources().getColor(mColorRes);
		view = inflater.inflate(R.layout.act_list, null);
		listview = (XListView) view.findViewById(R.id.list_view);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this);
		adapter = new ActListAdapter(getActivity());
		// adapter.appendToList(Constant.getFalseData(false));
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
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
		int index = 0, top = 0;
		/*
		 * if (savedInstanceState != null) {
		 * // Restore last state for checked position.
		 * index = savedInstanceState.getInt("index", -1);
		 * top = savedInstanceState.getInt("top", 0);
		 * if (index != -1) {
		 * listview.setSelectionFromTop(index, top);
		 * }
		 * }
		 */
		
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
		/*
		 * int index = listview.getFirstVisiblePosition();
		 * View v = listview.getChildAt(0);
		 * int top = (v == null) ? 0 : v.getTop();
		 * Log.d("@@");
		 * outState.putInt("index", index);
		 * outState.putInt("top", top);
		 */
	}
	
	@Override
	public void onRefresh() {
		curPage = 1;
		getActList(curPage);
		listview.onLoad();
	}
	
	@Override
	public void onLoadMore() {
		getActList(curPage);
		
	}
	
	private void getActList(int page) {
		
		getActListTask = new GetActListTask(getActivity(), new GetActListTask.TaskCallBack() {
			
			@Override
			public void onCallBack(ActList result) {
				if (getActivity() == null) {
					return;
				}
				if (result != null && result.status == 200) {
					final List<Act> actList = result.events;
					if (curPage == 1) {
						adapter.Clear();
						new Thread(new Runnable() {
							@Override
							public void run() {
								DataManage.saveIndexActlist2DB(actList);
							}
						}).start();
					}
					if (!actList.isEmpty()) {
						adapter.appendToList(actList);
						listview.onLoad();
						curPage++;
					}
					else {
						Utility.toastNoMoreResult(getActivity());
						listview.onLoads();
					}
				}
				else {
					if (curPage == 1) adapter.appendToList(DataManage.getIndexActlist4DB());
					
					if (!NetWorkHelper.checkNetState(getActivity())) {
						Utility.toastNetworkFailed(getActivity());
					}
					else if (result != null)
						Utility.toastResult(getActivity(), result.message);
					else
						Utility.toastFailedResult(getActivity());
					listview.onLoads();
				}
			}
		});
		getActListTask.execute(Constant.CUR_CITY, String.valueOf(curPage), "" + Constant.PER_NUM_GET_ACTLIST);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Act act = (Act) adapter.getItem(position - 1);
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActDetailActivity.class);
		intent.putExtra(Constant.Acts.ID_KEY, act.id);
		intent.putExtra(Constant.Acts.CITY_KEY, act.cities);
		intent.putExtra(Constant.Acts.DATE_KEY, act.duration);
		getActivity().startActivity(intent);
		
	}
	
}
