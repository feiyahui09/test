package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import com.zmax.app.widget.FragmentStatePagerAdapter;

public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
	
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();;
	
	private Activity mActivity;
	
	public WelcomePagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
	}
	
	/**
	 * 只加载listview不包含 tabs
	 * 
	 * @param listObject
	 */
	public void addFragment(List<Object> listObject) {
		
		/*
		 * for (int i = 0; i < listObject.size(); i++) { Object object =
		 * listObject.get(i); if (object instanceof Objec) { addTab(new
		 * NewsFragment(mActivity, ((NewsCategoryListEntity)
		 * listObject.get(i)))); } }
		 */
	}
	
	/*
	 * public void addNullFragment() { CategorysEntity cate = new
	 * CategorysEntity(); cate.setName("连接错误"); tabs.add(cate); addTab(new
	 * HttpErrorFragment()); }
	 */
	
	public void Clear() {
		mFragments.clear();
	}
	
	public void addTab(Fragment fragment) {
		mFragments.add(fragment);
		notifyDataSetChanged();
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}
	
	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	//
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}
}
