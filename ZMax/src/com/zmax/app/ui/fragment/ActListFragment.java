package com.zmax.app.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.zmax.app.R;
import com.zmax.app.adapter.ActListAdapter;
import com.zmax.app.manage.DataManage;
import com.zmax.app.model.Act;
import com.zmax.app.model.ActList;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActListTask;
import com.zmax.app.ui.ActDetailActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Constant.LOAD_STATE;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.XListView;
import com.zmax.app.widget.XListView.IXListViewListener;

import eu.inmite.android.lib.dialogs.ProgressDialogFragment;

public class ActListFragment extends Fragment implements IXListViewListener, OnItemClickListener {
	
	protected XListView listview;
	protected View view;
	
	private ActListAdapter adapter;
	private GetActListTask getActListTask;
	private int curPage = 1;
	private DialogFragment progressDialog;
	public static LOAD_STATE state = LOAD_STATE.FAILED;
	private Handler handler = new Handler();
	
	public ActListFragment() {
		state = LOAD_STATE.FAILED;
		Log.i("@@");
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
		PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
		listview.setOnScrollListener(listener);
		curPage = 1;
		Log.i("@@");
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		Log.i("@@");
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		int index = 0, top = 0;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (state == LOAD_STATE.LOADING&&getActivity()!=null)
					progressDialog = ProgressDialogFragment.createBuilder(getActivity(), getFragmentManager()).setMessage("正在加载中...")
							.setTitle("提示").setCancelable(true).show();
			}
		}, 350);
		onRefresh();
		Log.i("@@");
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Log.i("@@  isVisibleToUser" + isVisibleToUser);
		
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
		
		Log.i("@@");
		
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
		state = LOAD_STATE.LOADING;
	
		
		getActListTask = new GetActListTask(getActivity(), new GetActListTask.TaskCallBack() {
			
			@Override
			public void onCallBack(ActList result) {
				if (getActivity() == null) {
					return;
				}
				if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismiss();
				if (result != null && result.status == 200) {
					state = LOAD_STATE.SUCCESS;
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
					state = LOAD_STATE.FAILED;
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
		try {
			
			Log.i("[maxMemory]:  " + Runtime.getRuntime().maxMemory() / 1000 + " k");
			Log.i("[totalMemory]:  " + Runtime.getRuntime().totalMemory() / 1000 + " k");
			Log.i("[freeMemory]:   " + Runtime.getRuntime().freeMemory() / 1000 + " k");
			ImageLoader.getInstance().clearMemoryCache();
			System.gc();
			
			Log.i("after  [maxMemory]:  " + Runtime.getRuntime().maxMemory() / 1000 + " k");
			Log.i("after  [totalMemory]:  " + Runtime.getRuntime().totalMemory() / 1000 + " k");
			Log.i("after  [freeMemory]:   " + Runtime.getRuntime().freeMemory() / 1000 + " k");
			Act act = (Act) adapter.getItem(position - 1);
			Intent intent = new Intent();
			intent.setClass(getActivity(), ActDetailActivity.class);
			intent.putExtra(Constant.Acts.ID_KEY, act.id);
			intent.putExtra(Constant.Acts.CITY_KEY, act.cities);
			intent.putExtra(Constant.Acts.DATE_KEY, act.duration);
			getActivity().startActivity(intent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
