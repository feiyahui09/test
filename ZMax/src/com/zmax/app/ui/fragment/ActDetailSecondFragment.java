package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zmax.app.R;
import com.zmax.app.adapter.ActDescptionListAdapter;
import com.zmax.app.model.ActDetailContent;
import com.zmax.app.ui.ActDetailActivity.RefreshDataCallBack;

public class ActDetailSecondFragment extends Fragment implements OnItemClickListener, RefreshDataCallBack {
	
	protected ListView listview;
	protected View view;
	
	private ActDescptionListAdapter adapter;
	
	public ActDetailSecondFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.act_detail_second, null);
		listview = (ListView) view.findViewById(R.id.list_view);
		adapter = new ActDescptionListAdapter(getActivity());
		// listview.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
	
	@Override
	public void onDataRefresh(ActDetailContent detailContent) {
		if (detailContent == null    ) return;
		adapter.appendToList(detailContent.description_items);
		listview.setAdapter(adapter);
	}
	
	private boolean isInitialized = false;
}
