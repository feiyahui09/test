package com.zmax.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.zmax.app.model.Hotel;
import com.zmax.app.ui.fragment.HotelBookItemFragment;
import com.zmax.app.widget.FragmentStatePagerAdapter;

public class CopyOfHotelBookListAdapter extends FragmentStatePagerAdapter {
	
	private Context mContext;
	
	public ArrayList<Hotel> mHotels = new ArrayList<Hotel>();;

	private Activity mActivity;
	
	public CopyOfHotelBookListAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
	}
	
	public void Clear() {
		mHotels.clear();
	}
	
	public void addItem(Fragment fragment) {
//		mFragments.add(fragment);
//		notifyDataSetChanged();
	}
	
	public void addItem(Hotel hotel) {
		mHotels.add(hotel);
		notifyDataSetChanged();
	}
	
	public void addAll(List<Hotel> hotelList) {
		for (Hotel hotel : hotelList) {
			addItem(hotel);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return   HotelBookItemFragment.newInstance(mHotels.get(arg0));
	}
	
	@Override
	public int getCount() {
		return mHotels.size();
	}
	
//	@Override
//	public int getItemPosition(Object object) {
//		return POSITION_NONE;
//	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
	
}
